package ldp.example.com.ldpweather.gsonJavaBean;

/**
 * @author Adminstrator
 * des             ${TODO}
 * @version $Rev$
 * @updateAuthor $Author$
 * @updateDes ${TODO}
 */
public class Forecastsevendays {


    /**
     * date : 2018-07-06
     * week : 星期五
     * sunrise : 05:55
     * sunset : 19:54
     * night : {"weather":"小雨","templow":"23","img":"7","winddirect":"微风","windpower":"3级"}
     * day : {"weather":"大雨","temphigh":"27","img":"9","winddirect":"微风","windpower":"3级"}
     */

    private String date;
    private String week;
    private String sunrise;
    private String sunset;
    private NightBean night;
    private DayBean day;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getSunrise() {
        return sunrise;
    }

    public void setSunrise(String sunrise) {
        this.sunrise = sunrise;
    }

    public String getSunset() {
        return sunset;
    }

    public void setSunset(String sunset) {
        this.sunset = sunset;
    }

    public NightBean getNight() {
        return night;
    }

    public void setNight(NightBean night) {
        this.night = night;
    }

    public DayBean getDay() {
        return day;
    }

    public void setDay(DayBean day) {
        this.day = day;
    }

    public static class NightBean {
        /**
         * weather : 小雨
         * templow : 23
         * img : 7
         * winddirect : 微风
         * windpower : 3级
         */

        private String weather;
        private String templow;
        private String img;
        private String winddirect;
        private String windpower;

        public String getWeather() {
            return weather;
        }

        public void setWeather(String weather) {
            this.weather = weather;
        }

        public String getTemplow() {
            return templow;
        }

        public void setTemplow(String templow) {
            this.templow = templow;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getWinddirect() {
            return winddirect;
        }

        public void setWinddirect(String winddirect) {
            this.winddirect = winddirect;
        }

        public String getWindpower() {
            return windpower;
        }

        public void setWindpower(String windpower) {
            this.windpower = windpower;
        }
    }

    public static class DayBean {
        /**
         * weather : 大雨
         * temphigh : 27
         * img : 9
         * winddirect : 微风
         * windpower : 3级
         */

        private String weather;
        private String temphigh;
        private String img;
        private String winddirect;
        private String windpower;

        public String getWeather() {
            return weather;
        }

        public void setWeather(String weather) {
            this.weather = weather;
        }

        public String getTemphigh() {
            return temphigh;
        }

        public void setTemphigh(String temphigh) {
            this.temphigh = temphigh;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getWinddirect() {
            return winddirect;
        }

        public void setWinddirect(String winddirect) {
            this.winddirect = winddirect;
        }

        public String getWindpower() {
            return windpower;
        }

        public void setWindpower(String windpower) {
            this.windpower = windpower;
        }
    }
}
