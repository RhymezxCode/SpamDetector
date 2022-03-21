package com.kofo.spamdetector.data.providers

import com.kofo.spamdetector.data.model.SmsMlResult
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query


interface ApiList {

    @POST("/spamchecker")
    fun checkForSpam(
        @Body body: String,
        @Query("threshold") threshold: Double
    ): Response<SmsMlResult> // body data

}

