package com.sidnio.siyucloud.core.network;

import android.annotation.SuppressLint;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

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

    private String url;
    private String username;
    private String password;
    private String rootDirectory;


    public static class Builder {
        private String url;
        private String username;
        private String password;
        private String rootDirectory;

        public void setUrl(String url) {
            this.url = url;
        }

        public void setUsername(String username) {
            this.username = username;

        }

        public void setPassword(String password) {
            this.password = password;
        }

        public void setRootDirectory(String rootDirectory) {
            this.rootDirectory = rootDirectory;
        }

        public Webdav build() {
            Webdav webdav = new Webdav();
            webdav.url = url;
            webdav.rootDirectory = rootDirectory;
            webdav.username = username;
            webdav.password = password;

            webdav.request();
            return webdav;
        }
    }


    private List<FileData> files;

    public List<FileData> getFiles() {
        return files;
    }


    private void request() {

        OkHttpClient client = getUnsafeOkHttpClient();
        String credential = Credentials.basic(username, password);

        Request request = new Request.Builder()
                .url(url + rootDirectory)
                .method("PROPFIND", RequestBody.create(new byte[0], null))
                .header("Depth", "1")
                .header("Authorization", credential)
                .build();

        try (Response response = client.newCall(request).execute()) {

            Log.d(TAG, "code: " + response.code());

            if (response.body() != null) {
                String responseBody = response.body().string();
                parseWebDavResponse(responseBody);
            } else {
                Log.e(TAG, "响应体为空");
            }

        } catch (IOException e) {
            Log.e(TAG, "请求失败", e);
            throw new RuntimeException(e);
        }
    }


    @SuppressLint("CustomX509TrustManager,TrustAllX509TrustManager")
    private OkHttpClient getUnsafeOkHttpClient() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        public void checkClientTrusted(X509Certificate[] chain, String authType) {
                        }

                        public void checkServerTrusted(X509Certificate[] chain, String authType) {
                        }

                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[]{};
                        }
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
        Log.d(TAG, "parseWebDavResponse: " + xml);
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(xml));

            int eventType = parser.getEventType();
            String currentTag = null;
            ArrayList<FileData> fileDataArrayList = new ArrayList<>();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        currentTag = parser.getName();
                        break;
                    case XmlPullParser.TEXT:
                        if (currentTag != null) {
                            String name="";
                            FileData fileData = new FileData();

                            if (currentTag.endsWith(Parser.href.name())) {
                                String textHref = decodeHref(parser.getText());
                                fileData.setPath(textHref);
                                name = textHref
                                        .replace(rootDirectory, "")
                                        .replace("/", "");
                                fileData.setName(name);
                                Log.d(TAG, "文件名称: " + name);
                                Log.d(TAG, "文件路径: " + textHref);
                            } else if (currentTag.endsWith(Parser.getcontentlength.name())) {
                                String textContentLength = decodeHref(parser.getText());
                                fileData.setSize(textContentLength);

                                Log.d(TAG, "文件大小: " + textContentLength);
                            } else if (currentTag.endsWith(Parser.getcontenttype.name())) {
                                String textContentType = decodeHref(parser.getText());
                                fileData.setType(textContentType);

                                Log.d(TAG, "文件类型: " + textContentType);
                                Log.d(TAG, "---------------------------------------------------------");
                            }
                            if (!name.isEmpty()) {
                                fileDataArrayList.add(fileData);
                            }

                        }
                        break;
                    case XmlPullParser.END_TAG:
                        currentTag = null;
                        break;
                }
                eventType = parser.next();
            }
            files = fileDataArrayList;
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


    private enum Parser {
        href,
        getcontentlength,
        getcontenttype
    }

}
