package com.example.havadurumuuygulamasi.retrofitServis

class apiUtils {
    companion object{
        val base_url = "https://api.openweathermap.org/"
        fun getretrofitInterface():retrofitInterface{
            return retrofitClient.getClient(base_url).create(retrofitInterface::class.java)
        }
    }
}