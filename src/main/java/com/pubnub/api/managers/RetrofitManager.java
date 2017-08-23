package com.pubnub.api.managers;


import com.pubnub.api.PubNub;
import com.pubnub.api.enums.PNLogVerbosity;
import lombok.Getter;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

public class RetrofitManager {

    private PubNub pubnub;

    @Getter
    private Retrofit transactionInstance;
    @Getter
    private Retrofit subscriptionInstance;

    public RetrofitManager(PubNub pubNubInstance) {
        this.pubnub = pubNubInstance;

        this.transactionInstance = createRetrofit(
                this.pubnub.getConfiguration().getNonSubscribeRequestTimeout(),
                this.pubnub.getConfiguration().getConnectTimeout()
        );

        this.subscriptionInstance = createRetrofit(
                this.pubnub.getConfiguration().getSubscribeTimeout(),
                this.pubnub.getConfiguration().getConnectTimeout()
        );

    }

    protected final Retrofit createRetrofit(int requestTimeout, int connectTimeOut) {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.readTimeout(requestTimeout, TimeUnit.SECONDS);
        httpClient.connectTimeout(connectTimeOut, TimeUnit.SECONDS);

        if (pubnub.getConfiguration().getLogVerbosity() == PNLogVerbosity.BODY) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClient.addInterceptor(logging);
        }

        if (pubnub.getConfiguration().getHttpLoggingInterceptor() != null) {
            httpClient.addInterceptor(pubnub.getConfiguration().getHttpLoggingInterceptor());
        }

        if (pubnub.getConfiguration().getSslSocketFactory() != null && pubnub.getConfiguration().getX509ExtendedTrustManager() != null) {
            httpClient.sslSocketFactory(pubnub.getConfiguration().getSslSocketFactory(), pubnub.getConfiguration().getX509ExtendedTrustManager());
        }

        if (pubnub.getConfiguration().getConnectionSpec() != null) {
            httpClient.connectionSpecs(Collections.singletonList(pubnub.getConfiguration().getConnectionSpec()));
        }

        if (pubnub.getConfiguration().getHostnameVerifier() != null) {
            httpClient.hostnameVerifier(pubnub.getConfiguration().getHostnameVerifier());
        }

        if (pubnub.getConfiguration().getProxy() != null) {
            httpClient.proxy(pubnub.getConfiguration().getProxy());
        }

        if (pubnub.getConfiguration().getProxySelector() != null) {
            httpClient.proxySelector(pubnub.getConfiguration().getProxySelector());
        }

        if (pubnub.getConfiguration().getProxyAuthenticator() != null) {
            httpClient.proxyAuthenticator(pubnub.getConfiguration().getProxyAuthenticator());
        }

        return new Retrofit.Builder()
                .baseUrl(pubnub.getBaseUrl())
                .addConverterFactory(JacksonConverterFactory.create())
                .client(httpClient.build())
                .build();
    }

}
