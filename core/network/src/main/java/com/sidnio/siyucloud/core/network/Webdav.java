package com.sidnio.siyucloud.core.network;

import android.annotation.SuppressLint;
import android.util.Log;

import com.sidnio.siyucloud.utils.error.ErrorCallback;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
        Webdav webdav = new Webdav();
        private ErrorCallback errorCallback;
        public Builder setUrl(String url) {
            webdav.url = url;
            return this;
        }

        public Builder setUsername(String username) {
            webdav.username = username;
            return this;
        }

        public Builder setPassword(String password) {
            webdav.password = password;
            return this;
        }

        public Builder setRootDirectory(String rootDirectory) {
            webdav.rootDirectory = rootDirectory;
            return this;
        }

        public Builder setErrorCallback(ErrorCallback errorCallback) {
            this.errorCallback = errorCallback;
            return this;
        }

        public Webdav build() {
            try {
                webdav.request();
            } catch (Exception e) {
                webdav.files = new ArrayList<>();
                if (errorCallback != null){
                    errorCallback.onError(TAG, e);
                }
            }
            return webdav;
        }
    }


    private List<FileData> files;

    public List<FileData> getFiles() {
        return files;
    }


    private void request() throws Exception {

        OkHttpClient client = getUnsafeOkHttpClient();
        String credential = Credentials.basic(username, password);

        Request request = new Request.Builder()
                .url(url + rootDirectory)
                .method("PROPFIND", RequestBody.create(new byte[0], null))
                .header("Depth", "1")
                .header("Authorization", credential)
                .build();

        try (Response response = client.newCall(request).execute()) {

            if (response.code() != 207) {
                throw new RuntimeException("code: 207 请求失败");
            }

            if (response.body() != null) {
                String responseBody = response.body().string();
                parseWebDavResponse(responseBody);
            } else {
                throw new RuntimeException("响应体为空");
            }

        } catch (Exception e) {
            throw new RuntimeException("请求失败", e);
        }
    }


    @SuppressLint("CustomX509TrustManager,TrustAllX509TrustManager")
    private OkHttpClient getUnsafeOkHttpClient() throws Exception {
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
            throw new RuntimeException("SSL 认证失败", e);
        }
    }


    private void parseWebDavResponse(String xml) throws Exception {
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(xml));

            int eventType = parser.getEventType();
            String currentTag = null;
            FileData currentFileData = null;
            ArrayList<FileData> fileDataArrayList = new ArrayList<>();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        currentTag = parser.getName();
                        if (Tag.Response.string.equalsIgnoreCase(currentTag)) {
                            currentFileData = new FileData(); // 开始一个新的文件条目
                        }
                        break;

                    case XmlPullParser.TEXT:
                        if (currentTag != null && currentFileData != null) {
                            String text = parser.getText().trim();
                            if (currentTag.endsWith(Parser.href.name())) {
                                String textHref = decodeHref(text);
                                currentFileData.setPath(textHref);
                                String name = textHref.replace(rootDirectory, "").replace("/", "");
                                currentFileData.setName(name);
                                Log.d(TAG, "文件名称: " + name);
                                Log.d(TAG, "文件路径: " + textHref);
                            } else if (currentTag.endsWith(Parser.getcontentlength.name())) {
                                currentFileData.setSize(decodeHref(text));
                                Log.d(TAG, "文件大小: " + text);
                            } else if (currentTag.endsWith(Parser.getcontenttype.name())) {
                                currentFileData.setType(decodeHref(text));
                                Log.d(TAG, "文件类型: " + text);
                            }
                        }
                        break;

                    case XmlPullParser.END_TAG:
                        if (Tag.Response.string.equalsIgnoreCase(parser.getName()) && currentFileData != null) { // 结束一个文件条目

                            if (Objects.equals(currentFileData.getType(), Type.directory.string)) { // 判断是否是目录

                                if (!currentFileData.getName().isEmpty()) { //目录名称不为null
                                    fileDataArrayList.add(currentFileData); // 收集完整的文件条目
                                    Log.d(TAG, "添加文件条目: " + currentFileData.getName() + " " + currentFileData.getType());
                                    Log.d(TAG, "---------------------------------------------------------");
                                }

                            }

                            currentFileData = null;
                        }
                        currentTag = null;
                        break;
                }
                eventType = parser.next();
            }

            files = fileDataArrayList;
        } catch (Exception e) {
            throw new Exception("XML解析失败", e);
        }
    }

    private String decodeHref(String text) {
        try {
            return android.net.Uri.decode(text);
        } catch (Exception e) {
            return text;
        }
    }

    /**
     * 解析器
     */

    private enum Parser {
        href,
        getcontentlength,
        getcontenttype
    }

    /**
     * 文件类型
     */
    private enum Type {
        directory("httpd/unix-directory");
        private final String string;

        Type(String string) {
            this.string = string;
        }
    }

    private enum Tag {

        Response("D:response");


        private final String string;

        Tag(String string) {
            this.string = string;
        }
    }
}
