package com.kofo.spamdetector.ui.messages.about

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.kofo.spamdetector.R
import com.kofo.spamdetector.databinding.ActivityAboutBinding
import com.kofo.spamdetector.databinding.ActivitySearchBinding
import com.kofo.spamdetector.ui.messages.search.SearchActivity

class AboutActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAboutBinding

    fun getAboutActivityIntent(context: Context?): Intent {
        return Intent(context, AboutActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            back.setOnClickListener {
                finish()
            }
        }
    }



}