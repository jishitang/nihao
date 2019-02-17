package cn.webank.blockchain.request.http;

import lombok.Getter;
import lombok.Setter;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import javax.net.ssl.*;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateFactory;
import java.util.Arrays;

//import cn.webank.blockchain.spi.common.convert.RequestBuilder;

/**
 * Created by Junqi Zhang on 27/08/2017.
 */
public class RetrofitService<T> {

    private static final Logger logger = LoggerFactory.getLogger(RetrofitService.class);
    Class<T> serviceClass;
    @Getter
    T service;
    @Getter
    private String apiBaseUrl;
    private Retrofit.Builder builder;
    private Retrofit retrofit;
    @Getter
    @Setter
    private boolean isHealth;

//    private static OkHttpClient.Builder httpClient =
//            new OkHttpClient.Builder();

    public RetrofitService(final String apiBaseUrl, Class<T> serviceClass) {
        this.isHealth = true;
        this.serviceClass = serviceClass;
        changeApiBaseUrl(apiBaseUrl);
    }

    protected static HostnameVerifier getHostnameVerifier(final String[] hostUrls) {

        return new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                /*
                 * TODO : Do not trust everyone
				 */
                return true;
//				boolean ret = false;
//				for (String host : hostUrls) {
//					if (host.equalsIgnoreCase(hostname)) {
//						ret = true;
//					}
//				}
//				return ret;
            }
        };
    }

    public void changeApiBaseUrl(String newApiBaseUrl) {
        this.apiBaseUrl = newApiBaseUrl;
        OkHttpClient okHttpClient = null;
        try {
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init((KeyStore) null);
            TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
            if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
                throw new IllegalStateException("Unexpected default trust managers:" + Arrays.toString(trustManagers));
            }
            X509TrustManager trustManager = (X509TrustManager) trustManagers[0];
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, new TrustManager[]{trustManager}, null);
//			SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            SSLSocketFactory sslSocketFactory = getSSLSocketFactory();
//            sslSocketFactory.
//            getSSLSocketFactory()).hostnameVerifier(getHostnameVerifier(null);

            okHttpClient = new OkHttpClient.Builder().sslSocketFactory(sslSocketFactory, trustManager).hostnameVerifier(getHostnameVerifier(null)).build();

            builder = new Retrofit.Builder()
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .client(okHttpClient) //添加支持https的client
                    .baseUrl(apiBaseUrl);
            retrofit = builder.build();
            service = retrofit.create(serviceClass);

        } catch (NoSuchAlgorithmException e) {
            logger.error("exception : ", e);
        } catch (KeyStoreException e) {
            logger.error("exception : ", e);
        } catch (KeyManagementException e) {
            logger.error("exception : ", e);
        }

        //
//		TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(
//				TrustManagerFactory.getDefaultAlgorithm());
//		trustManagerFactory.init((KeyStore) null);
//		TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
//		if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
//			throw new IllegalStateException("Unexpected default trust managers:"
//					+ Arrays.toString(trustManagers));
//		}
//		X509TrustManager trustManager = (X509TrustManager) trustManagers[0];
//
//		SSLContext sslContext = SSLContext.getInstance("TLS");
//		sslContext.init(null, new TrustManager[] { trustManager }, null);
//		SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
//
//		OkHttpClient client = new OkHttpClient.Builder()
//				.sslSocketFactory(sslSocketFactory, trustManager).build();


        //


        //使用SSLSocketFactory初始化okHttpClient以支持https请求
//        OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder().sslSocketFactory(
//        		getSSLSocketFactory()).hostnameVerifier(getHostnameVerifier(null)
//		);
    }

    private SSLSocketFactory getSSLSocketFactory() {
        SSLSocketFactory sslSocketFactory = null;

        try {
            //在SDK客户端创建KeyStore
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);

            //将前置server证书tomcat.cer放到classpath下并导入创建的KeyStore
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            InputStream inputStream = null;
            try {
                inputStream = RetrofitService.class.getClassLoader().getResourceAsStream("tomcat.cer");
                keyStore.setCertificateEntry("tomcat", certificateFactory.generateCertificate(inputStream));
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
            }

            //创建TrustManagerFactory来信任KeyStore里的导入的证书
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);

            //基于创建的TrustManagerFactory来创建SSLContext
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());

            sslSocketFactory = sslContext.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return sslSocketFactory;
    }

//    public static <S> S createService(Class<S> serviceClass, AccessToken token) {
//        String authToken = token.getTokenType().concat(token.getAccessToken());
//        return createService(serviceClass, authToken);
//    }
}