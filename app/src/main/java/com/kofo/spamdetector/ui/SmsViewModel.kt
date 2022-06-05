package com.kofo.spamdetector.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.kofo.spamdetector.data.model.SmsMlResult
import com.kofo.spamdetector.data.repository.CheckForSpamRepository
import kotlinx.coroutines.launch

class SmsViewModel(application: Application) : AndroidViewModel(application) {

    private var smsRepository: CheckForSpamRepository.DbRepository? = null
    private var resultList: LiveData<List<SmsMlResult>>? = null
    private var count: LiveData<Int>? = null

    init {
        smsRepository = CheckForSpamRepository().DbRepository(application)
        resultList = smsRepository?.getAllSms()
    }

    fun insertSmsNow(result: SmsMlResult?) = viewModelScope.launch {
        insertSms(result)
    }

    fun getAllSms(): LiveData<List<SmsMlResult>>? {
        return smsRepository?.getAllSms()
    }

    private fun insertSms(result: SmsMlResult?) {
        if (result != null) {
            smsRepository?.insertSms(result)
        }
    }

    fun checkSms(text: String?, realText: String?) =
        viewModelScope.launch {
            count = smsRepository?.checkSms(text, realText)
        }

    fun getCheckedSms(): LiveData<Int>? {
        return count
    }
}

