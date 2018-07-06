package ldp.example.com.ldpweather.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;


/**
 * okhttp 网络访问请求
 */
public class Httputil {
    public static void sendOkhttpRequest(String address, okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(address)
                .build();
        client.newCall(request).enqueue(callback);
    }
}
