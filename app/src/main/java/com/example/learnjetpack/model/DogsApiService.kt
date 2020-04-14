package com.example.learnjetpack.model

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DogsApiService
{
    private  val  BASE_URL ="https://raw.githubusercontent.com/"
    private  var api = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(DogsApi::class.java)
    fun getDogs():Single<List<DogBreed>>{
        return api.getDogs()
    }
}