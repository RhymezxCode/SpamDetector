package com.kofo.spamdetector.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.kofo.spamdetector.R
import com.kofo.spamdetector.application.SpamDetectorApplication
import com.kofo.spamdetector.data.repository.CheckForSpamRepository

class MainActivity : AppCompatActivity(){
    private lateinit var checkForSpamViewModel: CheckForSpamViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkForSpamViewModel = ViewModelProvider(
            this, CheckForSpamViewModelFactory(
                SpamDetectorApplication(),
                CheckForSpamRepository()
            )
        )[CheckForSpamViewModel::class.java]
    }
}