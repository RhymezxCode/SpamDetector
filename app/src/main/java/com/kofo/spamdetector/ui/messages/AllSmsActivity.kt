package com.kofo.spamdetector.ui.messages

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.kofo.spamdetector.data.model.SmsMlResult
import com.kofo.spamdetector.databinding.ActivityAllSmsBinding

class AllSmsActivity : AppCompatActivity(), SmsListAdapter.ClickListener {
    private lateinit var binding: ActivityAllSmsBinding
    private var smsViewModel: SmsViewModel? = null
    private var smsListAdapter: SmsListAdapter? = null

    fun getAllSmsActivityIntent(context: Context?): Intent {
        return Intent(context, AllSmsActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllSmsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        smsViewModel = ViewModelProvider(this)[SmsViewModel::class.java]
        smsListAdapter = SmsListAdapter()
        smsListAdapter!!.setClickListener(this)

        smsViewModel?.getAllSms()?.observe(this) {
            if (it.isEmpty()) {

                binding.noData.visibility = View.VISIBLE

            } else {

                binding.noData.visibility = View.GONE
                setupListRecyclerView()
                smsListAdapter!!.setAllSms(this@AllSmsActivity, it)

            }

        }

        binding.back.setOnClickListener {
            finish()
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setupListRecyclerView() {
        with(binding.smsRecycler) {
            layoutManager = LinearLayoutManager(context)
            adapter = smsListAdapter
            smsListAdapter!!.notifyDataSetChanged()
        }
    }

    override fun gotoSmsPage(result: SmsMlResult) {
        val intent = Intent(this, MainActivity::class.java)
        val extra = bundleOf(
            "MessageFrom" to result.from,
            "Score" to result.score,
            "SmsText" to result.text,
            "TextResult" to result.result,
            "Status" to result.is_spam)
        intent.putExtras(extra)
        startActivity(intent)
    }
}