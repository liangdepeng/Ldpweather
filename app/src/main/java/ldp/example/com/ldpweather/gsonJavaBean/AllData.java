package ldp.example.com.ldpweather.gsonJavaBean;

import com.google.gson.annotations.SerializedName;

/**
 * @author Adminstrator
 * des             ${TODO}
 * @version $Rev$
 * @updateAuthor $Author$
 * @updateDes ${TODO}
 */
public class AllData {

    /**
     * status : 0
     * msg : ok
     */

    public String status;
    public String msg;

    @SerializedName("result")
    public Allweather allweather;

    public Allweather getAllweather() {
        return allweather;
    }

    public void setAllweather(Allweather allweather) {
        this.allweather = allweather;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
