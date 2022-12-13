package com.example.kurly.data.source

import android.content.Context
import com.example.kurly.BuildConfig
import com.example.kurly.R
import com.example.kurly.data.remote.NetworkDataSource
import com.example.kurly.data.remote.NetworkService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.kurly.android.mockserver.MockInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.inject.Named
import javax.inject.Singleton
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

/**
 * BCMProject
 * Class: RetrofitUtil
 * Created by 박수연 on 2021-04-21.
 *
 * Description: 통신 Util(ViewModel 에서 호출해서 사용)
 */

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Provides
    @Singleton
    fun provideDefaultRepository(
        @ApplicationContext context: Context,
        remoteDataSource: RemoteDataSource
    ): DefaultRepository {
        val repository = DefaultRepository(context, remoteDataSource)
        Timber.d("hrepository = $repository")
        return repository
    }

    @Provides
    @Singleton
    fun provideRemoteDataSource(
        networkService: NetworkService
    ): RemoteDataSource {
        return NetworkDataSource(networkService, Dispatchers.IO)
    }

    @Provides
    @Singleton
    fun provideNetworkService(
        okHttpClient: OkHttpClient,
        gson: Gson,
        baseUrl: String
    ): NetworkService {
        return Retrofit.Builder().apply {
            addConverterFactory(GsonConverterFactory.create(gson))
            baseUrl(baseUrl)
            client(okHttpClient)
        }.build().create(NetworkService::class.java)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        @ApplicationContext context: Context,
        @Named("HttpLoggingInterceptor") httpLoggingInterceptor: Interceptor,
        @Named("HeaderInterceptor") headerInterceptor: Interceptor
    ): OkHttpClient {
        return OkHttpClient.Builder().apply {
            addInterceptor(MockInterceptor(context))
            addInterceptor(httpLoggingInterceptor)
            addInterceptor(headerInterceptor)
            try {
                // Create a trust manager that does not validate certificate chains
                val trustAllCerts = arrayOf<TrustManager>(
                    object : X509TrustManager {
                        @Throws(CertificateException::class)
                        override fun checkClientTrusted(
                            chain: Array<X509Certificate>,
                            authType: String
                        ) {
                        }

                        @Throws(CertificateException::class)
                        override fun checkServerTrusted(
                            chain: Array<X509Certificate>,
                            authType: String
                        ) {
                        }

                        override fun getAcceptedIssuers(): Array<X509Certificate> {
                            return arrayOf()
                        }
                    }
                )

                // Install the all-trusting trust manager
                val sslContext = SSLContext.getInstance("SSL")
                sslContext.init(null, trustAllCerts, SecureRandom())

                // Create an ssl socket factory with our all-trusting manager
                val sslSocketFactory = sslContext.socketFactory
                if (sslSocketFactory != null) {
                    sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.build()
    }

    @Provides
    @Singleton
    @Named("HeaderInterceptor")
    fun provideHeaderInterceptor(@ApplicationContext applicationContext: Context): Interceptor {
        return Interceptor { chain: Interceptor.Chain ->
            val original = chain.request()
            val headers = original.headers
            if (headers.size == 0) {
                val request = original.newBuilder().apply {
                    header("Content-Type", "application/json; charset=utf-8")
                    method(original.method, original.body)
                }.build()

                Timber.d("headers = $request")

                chain.proceed(request)
            } else chain.proceed(original)
        }
    }

    @Provides
    @Singleton
    @Named("HttpLoggingInterceptor")
    fun provideHttpLoggingInterceptor(): Interceptor {
        val logging = HttpLoggingInterceptor()
        logging.level =
            if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        return logging
    }

    @Provides
    fun provideBaseUrl(@ApplicationContext applicationContext: Context) =
        applicationContext.getString(R.string.base_url)

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
            .setLenient()
            .create()
    }
}