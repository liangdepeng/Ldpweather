package ldp.example.com.ldpweather.db;


import org.litepal.crud.DataSupport;

/**
 *数据库 各省份 市 的表
 */
public class City extends DataSupport{
    private int id;
    private String cityName;
    private int cityCoe;
    private int provinceId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getCityCoe() {
        return cityCoe;
    }

    public void setCityCoe(int cityCoe) {
        this.cityCoe = cityCoe;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }
}
