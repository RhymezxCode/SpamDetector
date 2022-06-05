package com.kofo.spamdetector.ui.messages.search

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kofo.spamdetector.R
import com.kofo.spamdetector.databinding.ActivityOptionBinding
import com.kofo.spamdetector.databinding.ActivitySearchBinding
import com.kofo.spamdetector.ui.messages.option.OptionActivity

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding

    fun getSearchActivityIntent(context: Context?): Intent {
        return Intent(context, SearchActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            back.setOnClickListener {
                finish()
            }
        }
    }
}