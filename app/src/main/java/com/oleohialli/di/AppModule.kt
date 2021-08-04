package com.oleohialli.di

import com.oleohialli.BuildConfig
import com.oleohialli.api.LessonApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {

        //Building Http Client
        val httpClientBuilder: OkHttpClient.Builder = OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)

        // Adding logging capabilities
        if (BuildConfig.DEBUG) {
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            val loggingInterceptor =
                httpLoggingInterceptor.apply {
                    httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
                }
            httpClientBuilder.addInterceptor(loggingInterceptor)
        }
        return Retrofit.Builder()
            .baseUrl(LessonApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClientBuilder.build())
            .build()
    }

    @Provides
    @Singleton
    fun provideLessonsApi(retrofit: Retrofit): LessonApi =
        retrofit.create(LessonApi::class.java)


//    @Provides
//    @Singleton
//    fun provideDatabase(application: Application,
//                        callback: LessonDatabase.Callback) =
//        Room.databaseBuilder(application, LessonDatabase::class.java, Constants.DATABASE_NAME)
//            .fallbackToDestructiveMigration()
//            .addCallback(callback)
//            .build()

    //@Provides
//    fun provideFarmerDao(database: LessonDatabase) = database.lessonDao()

}
