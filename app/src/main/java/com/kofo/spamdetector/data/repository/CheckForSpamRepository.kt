package com.kofo.spamdetector.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kofo.spamdetector.data.model.SmsMlResult
import com.kofo.spamdetector.data.providers.dbProvider.Database
import com.kofo.spamdetector.data.providers.dbProvider.SmsMlResultDao
import com.kofo.spamdetector.data.providers.networkProvider.ApiService
import retrofit2.http.Body
import retrofit2.http.Query


class CheckForSpamRepository {

    fun checkForSpam(
        @Body body: String,
        @Query("threshold") threshold: Double
    ) = ApiService.apiCall().checkForSpam(body, threshold)


    inner class DbRepository(application: Application?) {
        private var resultDao: SmsMlResultDao? = null
        private var database: Database? = null
        private var resultList: LiveData<List<SmsMlResult>>? = null

        init {
            database = Database.getDatabase(application)
            resultDao = database!!.smsMlResultDao()
            resultList = resultDao!!.getAllSmsMlResult()
        }

        fun getAllSms(): LiveData<List<SmsMlResult>> {
            return resultDao!!.getAllSmsMlResult()
        }

        fun insertSms(result: SmsMlResult) {
            database!!.smsMlResultDao()!!.insertSmsMlResult(result)
        }

        fun checkSms(text: String?, realText: String?): LiveData<Int> {
            val data: MutableLiveData<Int> = MutableLiveData()
            val count: Int = resultDao?.checkSms(text, realText)!!
            data.postValue(count)
            return data
        }
    }


}