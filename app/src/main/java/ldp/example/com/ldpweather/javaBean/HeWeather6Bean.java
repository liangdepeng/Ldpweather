package ldp.example.com.ldpweather.javaBean;

import java.util.List;

/**
 * created by ldp at 2018/11/18
 */
public class HeWeather6Bean {
    /**
     * basic : {"cid":"CN101010100","location":"北京","parent_city":"北京","admin_area":"北京","cnty":"中国","lat":"39.90498734","lon":"116.4052887","tz":"+8.00"}
     * update : {"loc":"2018-11-18 18:45","utc":"2018-11-18 10:45"}
     * status : ok
     * now : {"cloud":"0","cond_code":"100","cond_txt":"晴","fl":"3","hum":"20","pcpn":"0.0","pres":"1022","tmp":"6","vis":"25","wind_deg":"276","wind_dir":"西风","wind_sc":"1","wind_spd":"4"}
     * daily_forecast : [{"cond_code_d":"100","cond_code_n":"100","cond_txt_d":"晴","cond_txt_n":"晴","date":"2018-11-18","hum":"18","mr":"14:34","ms":"01:22","pcpn":"0.0","pop":"0","pres":"1024","sr":"07:03","ss":"16:55","tmp_max":"11","tmp_min":"-3","uv_index":"4","vis":"10","wind_deg":"-1","wind_dir":"无持续风向","wind_sc":"1-2","wind_spd":"4"},{"cond_code_d":"100","cond_code_n":"101","cond_txt_d":"晴","cond_txt_n":"多云","date":"2018-11-19","hum":"24","mr":"15:02","ms":"02:22","pcpn":"0.0","pop":"0","pres":"1026","sr":"07:04","ss":"16:54","tmp_max":"10","tmp_min":"-2","uv_index":"2","vis":"20","wind_deg":"-1","wind_dir":"无持续风向","wind_sc":"1-2","wind_spd":"4"},{"cond_code_d":"101","cond_code_n":"101","cond_txt_d":"多云","cond_txt_n":"多云","date":"2018-11-20","hum":"28","mr":"15:31","ms":"03:23","pcpn":"0.0","pop":"0","pres":"1023","sr":"07:06","ss":"16:54","tmp_max":"10","tmp_min":"-1","uv_index":"4","vis":"20","wind_deg":"359","wind_dir":"北风","wind_sc":"1-2","wind_spd":"2"}]
     * lifestyle : [{"type":"comf","brf":"较不舒适","txt":"今天夜间天气晴好，但仍会使您感觉偏冷，不很舒适，请注意适时添加衣物，以防感冒。"},{"type":"drsg","brf":"冷","txt":"天气冷，建议着棉服、羽绒服、皮夹克加羊毛衫等冬季服装。年老体弱者宜着厚棉衣、冬大衣或厚羽绒服。"},{"type":"flu","brf":"较易发","txt":"天气较凉，较易发生感冒，请适当增加衣服。体质较弱的朋友尤其应该注意防护。"},{"type":"sport","brf":"较不宜","txt":"天气较好，但考虑天气寒冷，推荐您进行室内运动，户外运动时请注意保暖并做好准备活动。"},{"type":"trav","brf":"较适宜","txt":"天气较好，同时又有微风伴您一路同行。稍冷，较适宜旅游，您仍可陶醉于大自然的美丽风光中。"},{"type":"uv","brf":"弱","txt":"紫外线强度较弱，建议出门前涂擦SPF在12-15之间、PA+的防晒护肤品。"},{"type":"cw","brf":"较适宜","txt":"较适宜洗车，未来一天无雨，风力较小，擦洗一新的汽车至少能保持一天。"},{"type":"air","brf":"较差","txt":"气象条件较不利于空气污染物稀释、扩散和清除，请适当减少室外活动时间。"}]
     */
    private BasicBean basic;
    private UpdateBean update;
    private String status;
    private NowBean now;
    private List<DailyForecastBean> daily_forecast;
    private List<LifestyleBean> lifestyle;

    public BasicBean getBasic() {
        return basic;
    }

    public void setBasic(BasicBean basic) {
        this.basic = basic;
    }

    public UpdateBean getUpdate() {
        return update;
    }

    public void setUpdate(UpdateBean update) {
        this.update = update;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public NowBean getNow() {
        return now;
    }

    public void setNow(NowBean now) {
        this.now = now;
    }

    public List<DailyForecastBean> getDaily_forecast() {
        return daily_forecast;
    }

    public void setDaily_forecast(List<DailyForecastBean> daily_forecast) {
        this.daily_forecast = daily_forecast;
    }

    public List<LifestyleBean> getLifestyle() {
        return lifestyle;
    }

    public void setLifestyle(List<LifestyleBean> lifestyle) {
        this.lifestyle = lifestyle;
    }
}
