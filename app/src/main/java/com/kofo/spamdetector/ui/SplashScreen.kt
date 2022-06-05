package com.kofo.spamdetector.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kofo.spamdetector.R
import com.kofo.spamdetector.data.service.ActivityStarter
import com.kofo.spamdetector.databinding.ActivityAllSmsBinding
import com.kofo.spamdetector.databinding.ActivitySplashScreenBinding

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