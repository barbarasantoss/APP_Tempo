package com.example.tempo.services

import com.example.tempo.model.Main
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

//https://api.openweathermap.org/data/2.5/weather?q={city name}&appid={API key}
//268e404f7f601361cf91bb8d914258b3

interface Api {

    @GET("weather")

    fun weatherMap(
        @Query("q") cityName: String,
        @Query("appid") api_id: String,

    ): Call<Main>
}