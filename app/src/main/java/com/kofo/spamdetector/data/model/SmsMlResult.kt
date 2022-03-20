package com.kofo.spamdetector.data.model

data class SmsMlResult(
    val is_spam: Boolean,
    val result: String,
    val score: Double,
    val text: String
)