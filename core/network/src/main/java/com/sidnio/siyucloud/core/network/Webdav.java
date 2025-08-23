package com.sidnio.siyucloud.core.network;

import android.annotation.SuppressLint;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Webdav {
private static final String TAG = "Webdav";

    public Webdav() {
        try {
            OkHttpClient client = getUnsafeOkHttpClient();
            String credential = Credentials.basic("root", "123456");

            Request request = new Request.Builder()
                    .url("https://192.168.31.40/webdav")
                    .method("PROPFIND", RequestBody.create(new byte[0], null))
                    .header("Depth", "1")
                    .header("Authorization", credential)
                    .build();

            Response response = client.newCall(request).execute();
            Log.d(TAG, "code: " + response.code());

            if (response.body() != null) {
                String responseBody = response.body().string();
                parseWebDavResponse(responseBody);
            } else {
                Log.e(TAG, "响应体为空");
            }
        } catch (Exception e) {
            Log.e(TAG, "请求失败", e);
        }

    }

    @SuppressLint("CustomX509TrustManager")
    private OkHttpClient getUnsafeOkHttpClient() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        public void checkClientTrusted(X509Certificate[] chain, String authType) {}
                        public void checkServerTrusted(X509Certificate[] chain, String authType) {}
                        public X509Certificate[] getAcceptedIssuers() { return new X509Certificate[]{}; }
                    }
            };

            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new SecureRandom());

            return new OkHttpClient.Builder()
                    .sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) trustAllCerts[0])
                    .hostnameVerifier((hostname, session) -> true)
                    .build();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void parseWebDavResponse(String xml) {
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(xml));

            int eventType = parser.getEventType();
            String currentTag = null;

            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        currentTag = parser.getName();
                        break;
                    case XmlPullParser.TEXT:
                        if (currentTag != null) {
                            if (currentTag.endsWith("href")) {
                                Log.d(TAG, "文件路径: " + decodeHref(parser.getText()));
                            } else if (currentTag.endsWith("getcontentlength")) {
                                Log.d(TAG, "文件大小: " + decodeHref(parser.getText()));
                            } else if (currentTag.endsWith("getcontenttype")) {
                                Log.d(TAG, "文件类型: " + decodeHref(parser.getText()));
                                Log.d(TAG, "---------------------------------------------------------");
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        currentTag = null;
                        break;
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            Log.e(TAG, "XML解析失败", e);
        }
    }

    private String decodeHref(String text) {
        try {
            return java.net.URLDecoder.decode(text, StandardCharsets.UTF_8.name());
        } catch (Exception e) {
            Log.e(TAG, "decodeHref error: " + text, e);
            return text;
        }
    }



}
