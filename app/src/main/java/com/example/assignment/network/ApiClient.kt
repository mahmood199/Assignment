package com.examlounge.examloungeapp.network

import android.content.Context
import com.example.assignment.network.UserService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(Constants.PRODUCTION_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(getLoggingClient())
        .build()

    private fun getLoggingClient():OkHttpClient{
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level=HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    fun getUserService(context: Context): UserService {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level=HttpLoggingInterceptor.Level.BODY
        val okHttpClient=OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
        val authRetrofit:Retrofit = Retrofit.Builder()
            .baseUrl(Constants.PRODUCTION_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
        return authRetrofit.create(UserService::class.java)
    }
}