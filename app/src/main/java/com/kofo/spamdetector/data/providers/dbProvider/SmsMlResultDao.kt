package com.kofo.spamdetector.data.providers.dbProvider

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.kofo.spamdetector.data.model.SmsMlResult

@Dao
interface SmsMlResultDao {
    @Insert
    fun insertSmsMlResult(result: SmsMlResult?)

    @Query("SELECT * from SmsMlResult")
    fun getAllSmsMlResult(): LiveData<List<SmsMlResult>>

    @Query("select Count() from SmsMlResult where text =:text and realText =:realText")
    fun checkSms(text: String?, realText: String?): Int
}