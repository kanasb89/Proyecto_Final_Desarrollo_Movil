package com.example.perfilapp.data.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    // Emulador de Android Studio: 10.0.2.2 apunta al localhost de tu PC.
    // Celular físico: usa la IP LAN de tu PC, p. ej. "http://192.168.1.50:3000/"
    // (ambos en la misma red Wi-Fi y con el firewall permitiendo el puerto 3000).
    private const val BASE_URL = "http://10.0.2.2:3000/"

    private val client = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
        .build()

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
