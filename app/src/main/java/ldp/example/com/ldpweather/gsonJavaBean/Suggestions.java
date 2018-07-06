package ldp.example.com.ldpweather.gsonJavaBean;

/**
 * @author Adminstrator
 * des             ${TODO}
 * @version $Rev$
 * @updateAuthor $Author$
 * @updateDes ${TODO}
 */
public class Suggestions {

    /**
     * iname : 空调指数
     * ivalue : 较少开启
     * detail : 您将感到很舒适，一般不需要开启空调。
     */

    private String iname;
    private String ivalue;
    private String detail;

    public String getIname() {
        return iname;
    }

    public void setIname(String iname) {
        this.iname = iname;
    }

    public String getIvalue() {
        return ivalue;
    }

    public void setIvalue(String ivalue) {
        this.ivalue = ivalue;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
