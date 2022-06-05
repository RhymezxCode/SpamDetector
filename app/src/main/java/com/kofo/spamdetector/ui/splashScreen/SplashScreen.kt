package com.kofo.spamdetector.ui.splashScreen

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kofo.spamdetector.application.SpamDetectorApplication
import com.kofo.spamdetector.data.model.SmsMlResult
import com.kofo.spamdetector.data.preference.SharedPreference
import com.kofo.spamdetector.data.repository.CheckForSpamRepository
import com.kofo.spamdetector.data.service.ActivityStarter
import com.kofo.spamdetector.databinding.ActivitySplashScreenBinding
import com.kofo.spamdetector.ui.messages.MainActivity

class SplashScreen : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding

    fun getSplashScreenActivityIntent(context: Context?): Intent {
        return Intent(context, SplashScreen::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding){
            go.setOnClickListener {
                ActivityStarter.startActivity(
                    this@SplashScreen,
                    MainActivity().getMainActivityIntent(this@SplashScreen),
                    false
                )
            }
        }
    }
}