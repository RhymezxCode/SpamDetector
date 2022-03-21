package com.kofo.spamdetector.data.repository

import com.kofo.spamdetector.data.providers.ApiService
import retrofit2.http.Body
import retrofit2.http.Query


class CheckForSpamRepository {

    fun checkForSpam(
        @Body body: String,
        @Query("threshold") threshold: Double
    ) = ApiService.apiCall().checkForSpam(body, threshold)


}