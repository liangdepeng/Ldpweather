package ldp.example.com.ldpweather.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ldp.example.com.ldpweather.MainActivity;
import ldp.example.com.ldpweather.R;
import ldp.example.com.ldpweather.WeatherActivity;
import ldp.example.com.ldpweather.db.City;
import ldp.example.com.ldpweather.db.County;
import ldp.example.com.ldpweather.db.Province;
import ldp.example.com.ldpweather.util.AddressJsontoJava;
import ldp.example.com.ldpweather.util.Httputil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * @author Adminstrator
 * des             ${TODO}
 * @version $Rev$
 * @updateAuthor $Author$
 * @updateDes ${TODO}
 */
public class ChooseAreaFragment extends Fragment {

    /**
     * 级别分类 省/市/县
     */
    private static final int LEVEL_PROVINCE = 0;
    private static final int LEVEL_CITY = 1;
    private static final int LEVEL_COUNTY = 2;


    private TextView mTitle_text;
    private Button mBack_btn;
    private ListView mLiseView;
    private ArrayAdapter<String> adapter;
    private List<String> dataList = new ArrayList<>();
    private ProgressDialog progressDialog;
    /**
     * 选中的级别
     */
    private int currentLevel;

    private List<Province> mProvinceList;
    private List<City> mCityList;
    private List<County> mCountyList;
    private Province selectProvince;
    private City selectCity;
    private County selectCounty;
    private String mCountyName;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_area, container, false);
        mTitle_text = view.findViewById(R.id.title_text);
        mBack_btn = view.findViewById(R.id.back_btn);
        mLiseView = view.findViewById(R.id.list_view);


        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, dataList);

        mLiseView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mLiseView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (currentLevel == LEVEL_PROVINCE) {
                    selectProvince = mProvinceList.get(position);
                    queryCities();
                } else if (currentLevel == LEVEL_CITY) {
                    selectCity = mCityList.get(position);
                    queryCounties();
                }
                    /**
                     * 查询到相应地址，去查询天气，传递地址名称
                     */
                else if (currentLevel == LEVEL_COUNTY) {
                    mCountyName = mCountyList.get(position).getCountyName();
                    System.out.println("选择时 ： " + mCountyName);
                    /**
                     * instanceof 关键字 可以判断 一个对象是否属于某个类的实例
                     */
                    if (getActivity() instanceof MainActivity) {
                        Intent intent = new Intent(getActivity(), WeatherActivity.class);
                        intent.putExtra("countyName", mCountyName);
                        startActivity(intent);
                        getActivity().finish();
                    }else if (getActivity() instanceof WeatherActivity){
                        WeatherActivity activity = (WeatherActivity) getActivity();
                        activity.mDrawerLayout.closeDrawers();
                        activity.mSwipeRefreshLayout.setRefreshing(true);
                        activity.queryCityCode(mCountyName);
                    }
                }
            }
        });

        mBack_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentLevel == LEVEL_COUNTY) {
                    queryCities();
                } else if (currentLevel == LEVEL_CITY) {
                    queryProvinces();
                }
            }
        });
        queryProvinces();
    }

    /**
     * 查询省 优先查询数据库，找不到就去联网查找
     */
    private void queryProvinces(){
        mTitle_text.setText("中国");
        mBack_btn.setVisibility(View.GONE);
        mProvinceList = DataSupport.findAll(Province.class);
        if (mProvinceList.size()>0){
            dataList.clear();
            for (Province province:mProvinceList){
                dataList.add(province.getProvinceName());
            }
            adapter.notifyDataSetChanged();
            mLiseView.setSelection(0);
            currentLevel = LEVEL_PROVINCE;
        }else{
            String address = "http://guolin.tech/api/china";
            queryFromServer(address,"province");
        }
    }

    /**
     * 查询市 优先查询数据库，找不到就去联网查找
     */

    private void queryCities(){
        mTitle_text.setText(selectProvince.getProvinceName());
        mBack_btn.setVisibility(View.VISIBLE);

        mCityList = DataSupport.where("provinceid = ?",
                String.valueOf(selectProvince.getId())).find(City.class);
        if (mCityList.size()>0){
            dataList.clear();
            for (City city : mCityList){
                dataList.add(city.getCityName());
            }
            adapter.notifyDataSetChanged();
            mLiseView.setSelection(0);
            currentLevel = LEVEL_CITY;
        }else{
            int provinceCode = selectProvince.getProvinceCode();
            String address = "http://guolin.tech/api/china/" + provinceCode;
            queryFromServer(address,"city");
        }
    }

    /**
     * 查询县 优先查询数据库，找不到就去联网查找
     */
    private void queryCounties(){
        mTitle_text.setText(selectCity.getCityName());
        mBack_btn.setVisibility(View.VISIBLE);

        mCountyList = DataSupport.where("cityid = ? ",
                String.valueOf(selectCity.getId())).find(County.class);
        if (mCountyList.size()>0){
            dataList.clear();
            for(County county : mCountyList){
                dataList.add(county.getCountyName());
            }
            adapter.notifyDataSetChanged();
            mLiseView.setSelection(0);
            currentLevel = LEVEL_COUNTY;
        }else{
            int provinceCode = selectProvince.getProvinceCode();
            int cityCode = selectCity.getCityCoe();
            String address = "http://guolin.tech/api/china/" +provinceCode + "/" + cityCode;
            queryFromServer(address,"county");
        }
    }

    private void queryFromServer(String address, final String type){
        showProgressDialog();
        Httputil.sendOkhttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(getActivity(),"加载失败",Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                boolean result = false;
                if ("province".equals(type)){
                    result = AddressJsontoJava.handleProvinceResponse(responseText);
                }else if ("city".equals(type)){
                    result = AddressJsontoJava.handleCityResponse(responseText,selectProvince.getId());
                }else if("county".equals(type)){
                    result = AddressJsontoJava.handleCountyResponse(responseText,selectCity.getId());
                }

                if (result){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            if ("province".equals(type)){
                                queryProvinces();
                            }else if ("city".equals(type)){
                                queryCities();
                            }else if ("county".equals(type)){
                                queryCounties();
                            }
                        }
                    });
                }
            }
        });
    }


    /**
     * 显示进度对话框
     */
    private void showProgressDialog() {
        if (progressDialog == null){
            progressDialog =new ProgressDialog(getActivity());
            progressDialog.setMessage("正在加载...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    /**
     * 关闭进度对话框
     */
    private void closeProgressDialog(){
        if (progressDialog != null){
            progressDialog.dismiss();
        }
    }
}
