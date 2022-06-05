package com.kofo.spamdetector.ui.messages.option

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kofo.spamdetector.R
import com.kofo.spamdetector.data.service.ActivityStarter
import com.kofo.spamdetector.databinding.ActivityOptionBinding
import com.kofo.spamdetector.databinding.ActivitySplashScreenBinding
import com.kofo.spamdetector.ui.messages.AllSmsActivity
import com.kofo.spamdetector.ui.messages.about.AboutActivity
import com.kofo.spamdetector.ui.splashScreen.SplashScreen

class OptionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOptionBinding

    fun getOptionActivityIntent(context: Context?): Intent {
        return Intent(context, OptionActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOptionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            about.setOnClickListener {
                ActivityStarter.startActivity(
                    this@OptionActivity,
                    AboutActivity().getAboutActivityIntent(this@OptionActivity),
                    false
                )
            }

            back.setOnClickListener {
                finish()
            }
        }
    }
}