package com.example.evenlychallenge

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Utils {

    fun provideHttpClient(): OkHttpClient {

        // build okhttpclient
        val builder = OkHttpClient.Builder()
        builder.interceptors().add(AuthenticationInterceptor())

        return builder.build()
    }

    fun retrofitInstance(client: OkHttpClient): PlacesApiSearch {

        // use retrofit to get API data
        val api: PlacesApiSearch by lazy {
            Retrofit.Builder()
                .baseUrl("https://api.foursquare.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
                .create(PlacesApiSearch::class.java)
        }
        return api
    }

    fun retrofitInstanceDetails(client: OkHttpClient): PlacesApiDetails {

        // use retrofit to get API data
        val api: PlacesApiDetails by lazy {
            Retrofit.Builder()
                .baseUrl("https://api.foursquare.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
                .create(PlacesApiDetails::class.java)
        }
        return api
    }
}