package ldp.example.com.ldpweather.db;

import org.litepal.crud.DataSupport;




/**
 *   数据库 省份  的表
 */
public class Province extends DataSupport{

    /**
     * id : 1
     * name : 北京
     */
    private int provinceCode;
    private int id;
    private String provinceName;



    public int getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(int provinceCode) {
        this.provinceCode = provinceCode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }
}
