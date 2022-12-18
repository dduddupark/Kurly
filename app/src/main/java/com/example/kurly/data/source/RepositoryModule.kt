package com.example.kurly.data.source

import android.content.Context
import androidx.room.Room
import com.example.kurly.BuildConfig
import com.example.kurly.R
import com.example.kurly.data.local.AppDatabase
import com.example.kurly.data.local.ProductDao
import com.example.kurly.data.remote.NetworkDataSource
import com.example.kurly.data.remote.NetworkService
import com.example.kurly.data.remote.RemoteDataSource
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
 * Kurly
 * Class: NetworkModule
 * Created by bluepark on 2022/12/13.
 *
 * Description:
 */
@InstallIn(SingletonComponent::class)
@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext applicationContext: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "product-database"
        ).build()
    }

    @Provides
    fun provideProductDao(appDatabase: AppDatabase) = appDatabase.productDao()

    @Provides
    @Singleton
    fun provideDefaultRepository(
        productDao: ProductDao,
        remoteDataSource: RemoteDataSource
    ): DefaultRepository {
        return DefaultRepository(productDao, remoteDataSource)
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