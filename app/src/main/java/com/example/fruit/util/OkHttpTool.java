package com.example.fruit.util;

import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.NonNull;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.BitmapCallback;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * 便于使用okhttp3的工具类
 */
public class OkHttpTool {
    //日志标志
    private static String TAG = "OkHttpTool";
    //OkHttpClient类
    private static final OkHttpClient myOkHttpClient;

    static {
        //========日志拦截器=========
        //Log拦截器
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(@NonNull String message) {
                Log.i(TAG, message);
            }
        });
        //设置日志显示级别
        HttpLoggingInterceptor.Level level = HttpLoggingInterceptor.Level.BODY;
        loggingInterceptor.setLevel(level);

        //========cookie处理--让服务端记住app
        //这里是设置cookie的,但是并没有做持久化处理;只是把cookie保存在内存中
        CookieJar cookieJar=new CookieJar() {
            private final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();
            //保存cookie
            @Override
            public void saveFromResponse(@NonNull HttpUrl url, @NonNull List<Cookie> cookies) {
                cookieStore.put(url.host(), cookies);
            }
            //获取cookie
            @Override
            public List<Cookie> loadForRequest(@NonNull HttpUrl url) {
                List<Cookie> cookies = cookieStore.get(url.host());
                return cookies != null ? cookies : new ArrayList<Cookie>();
            }
        };

        //创建OkHttpClient
        myOkHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)//连接超时
                .writeTimeout(20, TimeUnit.SECONDS)//写入超时
                .readTimeout(20, TimeUnit.SECONDS)//读取超时
                .addInterceptor(loggingInterceptor)//添加日志处理拦截器
                .cookieJar(cookieJar)
                .build();
    }

    //================对外方法=====================

    /**
     * Get 请求
     * @param url{String} 请求地址
     * @param parameters{Map<String, Object>} 请求参数
     * @param responseCallback{ResponseCallback} 请求回调
     */
    @SuppressWarnings("unused")
    public static void httpGet(String url, Map<String, Object> parameters, ResponseCallback responseCallback) {
        Request request = createGetRequest(url, parameters);
        doRequest(request, responseCallback);
    }

    /**
     * POST 请求
     * @param url{String} 请求地址
     * @param parameters{Map<String, Object>} 请求参数
     * @param responseCallback{ResponseCallback} 请求回调
     */
    public static void httpPost(String url, Map<String, Object> parameters, ResponseCallback responseCallback) {
        Request request = createPostRequest(url, parameters);
        doRequest(request, responseCallback);
    }

    /**
     * POST 请求 JSON格式参数
     * @param url{String} 请求地址
     * @param json{String} JSON格式参数
     * @param responseCallback{ResponseCallback} 请求回调
     */
    @SuppressWarnings("unused")
    public static void httpPostJson(String url, String json, ResponseCallback responseCallback) {
        Request request = createPostRequestJson(url, json);
        doRequest(request, responseCallback);
    }

    /**
     * POST 请求 文件上传
     * @param url{String} 请求地址
     * @param parameters{Map<String, Object>} 请求参数
     * @param files{Map<String, File>} 上传的文件列表
     * @param responseCallback{ResponseCallback} 请求回调
     */
    @SuppressWarnings("unused")
    public static void httpPostWithFile(String url, Map<String, Object> parameters, Map<String, File> files, ResponseCallback responseCallback) {
        Request request = createPostRequestWithFile(url, parameters, files);
        doRequest(request, responseCallback);
    }

    /**
     * POST 请求 文件上传 byte数组
     * @param url{String} 请求地址
     * @param parameters{Map<String, Object>} 请求参数
     * @param files{Map<String, byte[]>}上传的文件列表
     * @param responseCallback{ResponseCallback} 请求回调
     */
    @SuppressWarnings("unused")
    public static void httpPostWithFileByte(String url, Map<String, Object> parameters, Map<String, byte[]> files, ResponseCallback responseCallback) {
        Request request = createPostRequestWithFileByte(url, parameters, files);
        doRequest(request, responseCallback);
    }

    /**
     * 获取图片  返回
     * @param url
     * @return Bitmap
     */
    public void getImage(String url)
    {
        Bitmap response;
        OkHttpUtils
                .get()//
                .url(url)//
                .tag(this)//
                .build()//
                .connTimeOut(20000)//链接超时
                .readTimeOut(20000)//读取超时
                .writeTimeOut(20000)//写入超时
                .execute(new BitmapCallback()
                {

                    @Override
                    public void onError(com.squareup.okhttp.Request request, Exception e) {

                    }

                    @Override
                    public void onResponse(Bitmap response) {
                    }
                });
    }
    //=====回调接口======
    public interface ResponseCallback {
        void onResponse(boolean isSuccess, int responseCode, String response, Exception exception);
    }


    //===========私有方法===============
    //====构建请求====

    //构建 Get请求
    private static Request createGetRequest(String url, Map<String, Object> parameters) {
        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append(url);
        if (url.indexOf('?') <= -1) {
            //未拼接参数
            urlBuilder.append("?");
        }

        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            urlBuilder.append("&");
            urlBuilder.append(entry.getKey());
            urlBuilder.append("=");
            urlBuilder.append(entry.getValue().toString());
        }
        return getBaseRequest().url(urlBuilder.toString()).build();
    }

    //构建 POST 请求
    private static Request createPostRequest(String url, Map<String, Object> parameters) {
        @SuppressWarnings("all")
        FormBody.Builder builder = new FormBody.Builder(Charset.forName("UTF-8"));
        if (parameters != null) {
            for (Map.Entry<String, Object> entry : parameters.entrySet()) {
                builder.add(entry.getKey(), entry.getValue().toString());
            }
        }
        FormBody formBody = builder.build();
        return getBaseRequest().url(url).post(formBody).build();
    }

    //构建 POST JSON格式参数请求
    private static Request createPostRequestJson(String url, String json) {
        MediaType mediaType = MediaType.parse("application/json;charset=utf-8");
        RequestBody body = RequestBody.create(json,mediaType);
        return getBaseRequest().url(url).post(body).build();
    }

    //构建 POST带文件的 请求
    private static Request createPostRequestWithFile(String url, Map<String, Object> parameters, Map<String, File> files) {
        // form 表单形式上传
        MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        if (files != null) {
            for (Map.Entry<String, File> fileEntry : files.entrySet()) {
                File file = fileEntry.getValue();
                if (file != null) {
                    // MediaType.parse() 里面是上传的文件类型。
                    RequestBody body = RequestBody.create(file,MediaType.parse("application/octet-stream"));
                    String filename = file.getName();
                    // 参数分别为， 请求key ，文件名称 ， RequestBody
                    requestBody.addFormDataPart(fileEntry.getKey(), filename, body);
                }
            }
        }
        if (parameters != null) {
            // map 里面是请求中所需要的 key 和 value
            for (Map.Entry<String, Object> entry : parameters.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue().toString();
                requestBody.addFormDataPart(key, value);
            }
        }
        return getBaseRequest().url(url).post(requestBody.build()).build();
    }

    //构建 POST带文件的 请求
    private static Request createPostRequestWithFileByte(String url, Map<String, Object> parameters, Map<String, byte[]> files) {
        // form 表单形式上传
        MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        if (files != null) {
            for (Map.Entry<String, byte[]> fileEntry : files.entrySet()) {
                byte[] file = fileEntry.getValue();
                if (file != null) {
                    // MediaType.parse() 里面是上传的文件类型。
                    RequestBody body = RequestBody.create(file,MediaType.parse("application/octet-stream"));
                    // 参数分别为， 请求key ，文件名称 ， RequestBody
                    requestBody.addFormDataPart(fileEntry.getKey(), fileEntry.getKey(), body);
                }
            }
        }
        if (parameters != null) {
            // map 里面是请求中所需要的 key 和 value
            for (Map.Entry<String, Object> entry : parameters.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue().toString();
                requestBody.addFormDataPart(key, value);
            }
        }
        return getBaseRequest().url(url).post(requestBody.build()).build();
    }

    //===实际进行请求的方法
    private static void doRequest(final Request request, final ResponseCallback responseCallback) {
        //使用okhttp3的异步请求
        myOkHttpClient.newCall(request).enqueue(new Callback() {
            //失败回调
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                //回调
                responseCallback.onResponse(false, -1, null, e);
                //用于输出错误调试信息
                if (e.getMessage()!=null){
                    Log.e(TAG, e.getMessage());
                }
            }
            //成功回调
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                int responseCode = response.code();//获取响应码
                ResponseBody responseBody = response.body();//获取 ResponseBody
                if (response.isSuccessful() && responseBody != null) {
                    String strResponse = responseBody.string();
                    //回调
                    responseCallback.onResponse(true, responseCode, strResponse, null);
                } else {
                    //回调
                    responseCallback.onResponse(false, responseCode, null, null);
                }
            }
        });
    }

    //获取请求 指定client为Android
    private static Request.Builder getBaseRequest() {
        Request.Builder builder = new Request.Builder();
        builder.addHeader("client", "Android");
        return builder;
    }
}


//.addInterceptor(new Interceptor() {
//    @Override
//    public Response intercept(@NonNull Chain chain) throws IOException {
//        Request request = chain.request().newBuilder()
//            .addHeader("Connection", "close").build();
//        return chain.proceed(request);
//    }
//})


/*以前自己写的日志拦截器*/
//添加日志处理拦截器
//.addInterceptor(new Interceptor() {
//    @Override
//    public Response intercept(@NonNull Chain chain) throws IOException {
//        Request request = chain.request();
//        long startTime = System.currentTimeMillis();
//        Response response = chain.proceed(chain.request());
//        long endTime = System.currentTimeMillis();
//        long duration = endTime - startTime;
//        MediaType mediaType = response.body().contentType();
//        String content = response.body().string();
//        Log.d(TAG, "\n");
//        Log.d(TAG, "----------Start----------------");
//        Log.d(TAG, "| " + request.toString());
//        String method = request.method();
//        if ("POST".equals(method)) {
//            StringBuilder sb = new StringBuilder();
//            if (request.body() instanceof FormBody) {
//                FormBody body = (FormBody) request.body();
//                for (int i = 0; i < body.size(); i++) {
//                    sb.append(body.encodedName(i) + "=" + body.encodedValue(i) + ",");
//                }
//                if (sb.length() > 0) {
//                    //在参数不为空的情况下处理最后的 “,”
//                    sb.delete(sb.length() - 1, sb.length());
//                }
//                Log.d(TAG, "| RequestParams:{" + sb.toString() + "}");
//            }
//        }
//        Log.d(TAG, "| Response:" + content);
//        Log.d(TAG, "----------End:" + duration + "毫秒----------");
//        //由于前面的代码已经获取了响应结果，会导致后续代码无法获取到响应结果
//        // 需要重新构建一个响应结果并返回
//        return response.newBuilder()
//                .body(ResponseBody.create(mediaType, content))
//                .build();
//    }
//})