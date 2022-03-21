package com.kofo.spamdetector.data.providers

import com.kofo.spamdetector.data.providers.URL.BASE_URL

import retrofit2.Retrofit

object ApiService {

    fun apiCall(): ApiList = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(ApiWorker.gsonConverter)
        .client(ApiWorker.client)
        .build()
        .create(ApiList::class.java)

}