package com.kxjsj.doctorassistant.Net;

import android.util.Log;

import com.kxjsj.doctorassistant.Constant.Constance;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;


/**
 * Created by vange on 2017/9/26.
 */

public class HttpClientUtils {

    private static final int READ_TIMEOUT = 6000;
    private static final int REQUST_TIMEOUT = 6000;

    /**
     * post请求
     *
     * @param url
     * @param
     * @return
     */
    public static String post(String url, MyNameValue myNameValue) {
        return post(url, myNameValue, null);
    }


    /**
     * 注册证书
     */
    public static void addkeyStore(InputStream... cers) {
        try {
            registKeyStore(HttpClientHolder.client, cers);
        } catch (Exception e) {
            e.printStackTrace();
            if (Constance.DEBUGTAG)
                Log.i(Constance.DEBUG, "addkeyStore: "+e.getMessage());
        }
    }

    /**
     * post请求
     *
     * @param url
     * @param
     * @return
     */
    public static String post(String url, MyNameValue myNameValue, MyHeader headers) {
        HttpPost httpPost = new HttpPost(url);
        try {
            //添加参数
            if (headers != null)
                httpPost.getParams().setParameter("http.default-headers", headers.get());
            if (myNameValue != null)
                httpPost.setEntity(new UrlEncodedFormEntity(myNameValue.get(), HTTP.UTF_8));
            HttpResponse httpResponse = wrapClient().execute(httpPost);
            String result = null;
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = httpResponse.getEntity();
                result = EntityUtils.toString(entity);
            }
            if (Constance.DEBUGTAG)
                Log.i(Constance.DEBUG, "post: " + result);
            return result;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * get请求
     *
     * @param url
     * @param
     * @return
     */
    public static String get(String url, MyNameValue myNameValue) {
        return get(url, myNameValue, null);
    }

    /**
     * get请求
     *
     * @param url
     * @param
     * @return
     */
    public static String get(String url, MyNameValue myNameValue, MyHeader headers) {
        /**
         * 拼接参数
         */
        if (myNameValue != null) {
            url = url + "?";
            for (NameValuePair nameValuePair : myNameValue.get()) {
                url += nameValuePair.getName() + "=" + nameValuePair.getValue() + "&";
            }
            if (url.endsWith("&")) {
                url = url.substring(0, url.length() - 1);
            }
        }
        HttpGet httpget = new HttpGet(url);
        try {
            //添加参数
            if (headers != null)
                httpget.getParams().setParameter("http.default-headers", headers.get());
            HttpResponse httpResponse = wrapClient().execute(httpget);
            String result = null;
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = httpResponse.getEntity();
                result = EntityUtils.toString(entity);
            }
            if (Constance.DEBUGTAG)
                Log.i(Constance.DEBUG, "get: " + result);
            return result;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 一些默认参数设置
     *
     * @return
     */
    private static HttpClient wrapClient() {
        //请求超时
        HttpClientHolder.client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, REQUST_TIMEOUT);
        //读取超时
        HttpClientHolder.client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, READ_TIMEOUT);
        return HttpClientHolder.client;
    }

    /**
     * client单例
     */
    private static class HttpClientHolder {
        private static HttpClient client = new DefaultHttpClient();
    }

    /**
     * 配置证书
     *
     * @param client   要设置证书的client
     * @param keystore 证书
     * @param
     * @param /        443 80
     * @throws Exception
     */
    private static void registKeyStore(HttpClient client, InputStream... keystore) throws Exception {
        java.security.KeyStore trustStore = java.security.KeyStore.getInstance(java.security.KeyStore.getDefaultType());
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        trustStore.load(null,null);
        if (keystore != null) {
            for (int i = 0; i < keystore.length; i++) {
                String certificateAlias = "server" + i;
                trustStore.setCertificateEntry(certificateAlias, certificateFactory.generateCertificate(keystore[i]));
                try {
                    if (keystore[i] != null)
                        keystore[i].close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        /**
         * 有证书加载证书，无证书信任所有
         */
        SSLSocketFactory socketFactory = keystore == null?new SSLSocketFactoryEx(trustStore):new SSLSocketFactory(trustStore);
        socketFactory.setHostnameVerifier(keystore == null ? SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER : SSLSocketFactory.STRICT_HOSTNAME_VERIFIER);
        //和被访问端约定的端口，一般为443
        org.apache.http.conn.scheme.Scheme sch = new org.apache.http.conn.scheme.Scheme("https", socketFactory, 443);
        org.apache.http.conn.scheme.Scheme sch2 = new org.apache.http.conn.scheme.Scheme("http", PlainSocketFactory.getSocketFactory(), 80);
        client.getConnectionManager().getSchemeRegistry().register(sch);
        client.getConnectionManager().getSchemeRegistry().register(sch2);
    }

    /**
     * okhhtp builder.setSSLSocketFactory
     *
     * @param cers
     * @return
     */
    private static javax.net.ssl.SSLSocketFactory getSSlSslSocketFactory(InputStream... cers) {
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);
            for (int i = 0; i < cers.length; i++) {
                String certificateAlias = "server" + i;
                keyStore.setCertificateEntry(certificateAlias, certificateFactory.generateCertificate(cers[i]));
                try {
                    if (cers[i] != null)
                        cers[i].close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            SSLContext sslContext = SSLContext.getInstance("TLS");
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
            return sslContext.getSocketFactory();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * namevaluePair封装
     */
    static class MyNameValue {
        private List<NameValuePair> pairs;

        public MyNameValue() {
            pairs = new ArrayList<>(3);
        }

        public MyNameValue add(String key, String value) {
            NameValuePair pair = new BasicNameValuePair(key, value);
            pairs.add(pair);
            return this;
        }

        public List<NameValuePair> get() {
            return pairs;
        }
    }

    /**
     * header封装类
     */
    static class MyHeader {
        private List<Header> headers;

        public MyHeader() {
            headers = new ArrayList<>(3);
        }

        public MyHeader add(String key, String value) {
            Header header = new BasicHeader(key, value);
            headers.add(header);
            return this;
        }

        public List<Header> get() {
            return headers;
        }
    }

    /**
     * @param urlPath     下载路径
     * @param downloadDir 下载存放目录
     * @return 返回下载文件
     */
    public static File downloadFile(String urlPath, String downloadDir, ProgressCallBack callBack) {
        File file = null;
        try {
            // 统一资源
            URL url = new URL(urlPath);
            // 连接类的父类，抽象类
            URLConnection urlConnection = url.openConnection();
            // http的连接类
            HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
            // 设定请求的方法，默认是GET
            httpURLConnection.setRequestMethod("POST");
            // 设置字符编码
            httpURLConnection.setRequestProperty("Charset", "UTF-8");
//            httpURLConnection.setRequestProperty();
            // 打开到此 URL 引用的资源的通信链接（如果尚未建立这样的连接）。
            httpURLConnection.connect();

            // 文件大小
            int fileLength = httpURLConnection.getContentLength();

            // 文件名
            String filePathUrl = httpURLConnection.getURL().getFile();
            String fileFullName = filePathUrl.substring(filePathUrl.lastIndexOf(File.separatorChar) + 1);

            System.out.println("file length---->" + fileLength);

            BufferedInputStream bin = new BufferedInputStream(httpURLConnection.getInputStream());

            String path = downloadDir + File.separatorChar + fileFullName;
            file = new File(path);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            OutputStream out = new FileOutputStream(file);
            int size = -1;
            int len = 0;
            byte[] buf = new byte[1024];
            while ((size = bin.read(buf)) != -1) {
                len += size;
                out.write(buf, 0, size);

                callBack.progress(len, fileLength);
                // 打印下载百分比
                // System.out.println("下载了-------> " + len * 100 / fileLength +
                // "%\n");
            }
            bin.close();
            out.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            return file;
        }

    }

    /**
     * 多文件上传的方法
     *
     * @param actionUrl：上传的路径
     * @param uploadFilePaths：需要上传的文件路径，数组
     * @return
     */
    @SuppressWarnings("finally")
    public static String uploadFile(String actionUrl, String[] uploadFilePaths, ProgressCallBack callBack) {
        String end = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";

        DataOutputStream ds = null;
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;
        StringBuffer resultBuffer = new StringBuffer();
        String tempLine = null;

        try {
            // 统一资源
            URL url = new URL(actionUrl);
            // 连接类的父类，抽象类
            URLConnection urlConnection = url.openConnection();
            // http的连接类
            HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;

            // 设置是否从httpUrlConnection读入，默认情况下是true;
            httpURLConnection.setDoInput(true);
            // 设置是否向httpUrlConnection输出
            httpURLConnection.setDoOutput(true);
            // Post 请求不能使用缓存
            httpURLConnection.setUseCaches(false);
            // 设定请求的方法，默认是GET
            httpURLConnection.setRequestMethod("POST");
            // 设置字符编码连接参数
            httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
            // 设置字符编码
            httpURLConnection.setRequestProperty("Charset", "UTF-8");
            // 设置请求内容类型
            httpURLConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

            // 设置DataOutputStream

            ds = new DataOutputStream(httpURLConnection.getOutputStream());
            for (int i = 0; i < uploadFilePaths.length; i++) {
                String uploadFile = uploadFilePaths[i];

                File file = new File(uploadFile);
                long totalSpace = file.getTotalSpace();
                long current = 0;


                String filename = uploadFile.substring(uploadFile.lastIndexOf("//") + 1);
                ds.writeBytes(twoHyphens + boundary + end);
                ds.writeBytes("Content-Disposition: form-data; " + "name=\"file" + i + "\";filename=\"" + filename
                        + "\"" + end);
                ds.writeBytes(end);
                FileInputStream fStream = new FileInputStream(uploadFile);
                int bufferSize = 1024;
                byte[] buffer = new byte[bufferSize];
                int length = -1;
                while ((length = fStream.read(buffer)) != -1) {
                    ds.write(buffer, 0, length);
                    current += length;
                    callBack.multilProgress(i, current, totalSpace);
                }
                ds.writeBytes(end);
                /* close streams */
                fStream.close();
            }
            ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
            /* close streams */
            ds.flush();
            if (httpURLConnection.getResponseCode() >= 300) {
                throw new Exception(
                        "HTTP Request is not success, Response code is " + httpURLConnection.getResponseCode());
            }

            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = httpURLConnection.getInputStream();
                inputStreamReader = new InputStreamReader(inputStream);
                reader = new BufferedReader(inputStreamReader);
                resultBuffer = new StringBuffer();
                while ((tempLine = reader.readLine()) != null) {
                    resultBuffer.append(tempLine);
                    resultBuffer.append("\n");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ds != null) {
                try {
                    ds.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStreamReader != null) {
                try {
                    inputStreamReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return resultBuffer.toString();
        }
    }

    /**
     * 上传下载进度监听
     */
    abstract class ProgressCallBack {
        /**
         * 单文件下载
         *
         * @param current
         * @param total
         */
        void progress(long current, long total) {
        }

        /**
         * 多文件上传
         *
         * @param part    第一个
         * @param current 当前大小
         * @param total   总大小
         */
        void multilProgress(int part, long current, long total) {
        }
    }

    static class SSLSocketFactoryEx extends SSLSocketFactory {



        SSLContext sslContext = SSLContext.getInstance("TLS");



        public SSLSocketFactoryEx(KeyStore truststore)

                throws NoSuchAlgorithmException, KeyManagementException,

                KeyStoreException, UnrecoverableKeyException {

            super(truststore);



            TrustManager tm = new X509TrustManager() {

                public java.security.cert.X509Certificate[] getAcceptedIssuers() {return null;}



                @Override

                public void checkClientTrusted(

                        java.security.cert.X509Certificate[] chain, String authType)

                        throws java.security.cert.CertificateException {}



                @Override

                public void checkServerTrusted(

                        java.security.cert.X509Certificate[] chain, String authType)

                        throws java.security.cert.CertificateException {}

            };

            sslContext.init(null, new TrustManager[] { tm }, null);

        }



        @Override

        public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException, UnknownHostException {

            return sslContext.getSocketFactory().createSocket(socket, host, port,autoClose);

        }



        @Override

        public Socket createSocket() throws IOException {

            return sslContext.getSocketFactory().createSocket();

        }

    }
}
