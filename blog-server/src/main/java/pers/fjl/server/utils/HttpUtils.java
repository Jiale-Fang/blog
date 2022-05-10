package pers.fjl.server.utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

public class HttpUtils {
    private static class ResultData {
        int status;
        String result;

        public ResultData() {
            this.status = 400;
        }
    }

//    public static int post(String url) {
//        return post(url ,null);
//    }
//
//    public static int post(String url, String json) {
//        ResultData rd = postIml(url, json);
//        return rd.status;
//    }

    public static String goPost(String url, String json, String appId, String appSecret) {
        ResultData rd = postIml(url, json, appId, appSecret);
        return rd.result;
    }

    public static ResultData postIml(String url, String json, String appId, String appSecret) {
        ResultData rd = new ResultData();

        SSLContext sslContext = null;
        try {
            sslContext = SSLContexts.custom()
                    .loadTrustMaterial((chain, authType) -> true)
                    .build();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        SSLConnectionSocketFactory sslSF = new SSLConnectionSocketFactory(
                sslContext,
                NoopHostnameVerifier.INSTANCE);
        PlainConnectionSocketFactory plainConnectionSocketFactory = new PlainConnectionSocketFactory();
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("https", sslSF)
                .register("http", plainConnectionSocketFactory)
                .build();
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(registry);
        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(connectionManager)
                .build();

        HttpPost post = new HttpPost();
        try {
            post.setHeader("Content-Type", "application/json;charset=UTF-8");
            post.setHeader("App-Id", appId);
            post.setHeader("App-Secret", appSecret);
            post.setURI(new URI(url));

            if (null != json) {
                StringEntity requestEntity = new StringEntity(json,"utf-8");
                requestEntity.setContentEncoding("UTF-8");
                post.setEntity(requestEntity);
            }

            HttpResponse resp = httpClient.execute(post);

            int status = resp.getStatusLine().getStatusCode();
            rd.status = status;
            HttpEntity httpEntity = resp.getEntity();
            rd.result = EntityUtils.toString(httpEntity);

            httpClient.close();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return rd;
    }
}
