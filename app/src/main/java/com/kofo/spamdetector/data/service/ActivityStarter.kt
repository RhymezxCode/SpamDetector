package com.kofo.spamdetector.data.service

import android.content.Intent

import androidx.appcompat.app.AppCompatActivity

object ActivityStarter {
    fun startActivity(activity: AppCompatActivity, intent: Intent?, finishCurrent: Boolean) {
        activity.startActivity(intent)
        if (finishCurrent) activity.finish()
    }
}