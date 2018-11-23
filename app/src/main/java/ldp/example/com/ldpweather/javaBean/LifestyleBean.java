package ldp.example.com.ldpweather.javaBean;

/**
 * created by ldp at 2018/11/18
 */
public  class LifestyleBean {
    /**
     * type : comf
     * brf : 较不舒适
     * txt : 今天夜间天气晴好，但仍会使您感觉偏冷，不很舒适，请注意适时添加衣物，以防感冒。
     */

    private String type;
    private String brf;
    private String txt;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBrf() {
        return brf;
    }

    public void setBrf(String brf) {
        this.brf = brf;
    }

    public String getTxt() {
        return txt;
    }

    public void setTxt(String txt) {
        this.txt = txt;
    }
}
