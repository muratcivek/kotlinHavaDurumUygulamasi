package com.example.havadurumuuygulamasi.retrofitServis

import com.example.havadurumuuygulamasi.havaDurumuModel.havaDurumModel
import com.example.havadurumuuygulamasi.sehirModel.sehirModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface retrofitInterface {
    @GET("data/2.5/weather")
    fun havaDurumuGetir(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") key: String,
        @Query("lang") dil: String
    ): Call<havaDurumModel>

    @GET("geo/1.0/direct")
    fun sehirGetir(
        @Query("q") sehir: String,
        @Query("appid") key: String,


    ): Call<sehirModel>
}