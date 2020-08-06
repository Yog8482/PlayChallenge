package com.yogendra.playapplication.di

import android.app.Application
import com.google.gson.Gson
import com.yogendra.playapplication.BuildConfig
import com.yogendra.playapplication.LOGIN_URL
import com.yogendra.playapplication.apis.LoginApi
import com.yogendra.playapplication.database.AppDatabase
import com.yogendra.playapplication.datasource.remote.LoginRemoteDataSource
import com.yogendra.playapplication.interceptor.NetworkConnectionInterceptor
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class])
class AppModule {
    @Provides
    fun provideOkHttpClient(interceptor: HttpLoggingInterceptor, app: Application): OkHttpClient =
        OkHttpClient.Builder().addInterceptor(interceptor)
            .addNetworkInterceptor(NetworkConnectionInterceptor(app.applicationContext))
            .build()


    @Provides
    fun provideLoggingInterceptor() =
        HttpLoggingInterceptor().apply {
            level =
                if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        }


    @Provides
    @Singleton
    fun provideGson(): Gson = Gson()


    @Provides
    @Singleton
    fun provideGsonConverterFactory(gson: Gson): GsonConverterFactory =
        GsonConverterFactory.create(gson)

    @Singleton
    @Provides
    fun provideLoginDataService(
        okhttpClient: OkHttpClient,
        converterFactory: GsonConverterFactory
    ) = provideLoginService(okhttpClient, converterFactory, LoginApi::class.java)

    @Singleton
    @Provides
    fun provideLoginRemoteDataSource(loginApi: LoginApi) =
        LoginRemoteDataSource(loginApi)


//    @Singleton
//    @Provides
//    fun provideUsersRemoteDataSource(fetchDataService: FetchDataService) =
//        UsersRemoteDataSource(fetchDataService)
//

    @Singleton
    @Provides
    fun provideDb(app: Application) = AppDatabase.getInstance(app)

    @Singleton
    @Provides
    fun provideContext(app: Application) = app.applicationContext


    @Singleton
    @Provides
    fun provideItemDetailsDao(db: AppDatabase) = db.detailsDao()


    @Singleton
    @Provides
    fun provideKeysDao(db: AppDatabase) = db.keysDao()

    @CoroutineScopeIO
    @Provides
    fun provideCoroutineScopeIO() = CoroutineScope(Dispatchers.IO)


    private fun createRetrofit(
        okhttpClient: OkHttpClient,
        converterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(LOGIN_URL)
            .client(okhttpClient)
            .addConverterFactory(converterFactory)
            .build()
    }

    private fun <T> provideLoginService(
        okhttpClient: OkHttpClient,
        converterFactory: GsonConverterFactory, service: Class<T>
    ): T {
        return createRetrofit(okhttpClient, converterFactory).create(service)
    }
}