package com.kofo.spamdetector.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "SmsMlResult")
data class SmsMlResult(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val is_spam: Boolean,
    val result: String,
    val score: Double,
    val text: String,
    val from: String,
    val realText: String,
    val error: String
)