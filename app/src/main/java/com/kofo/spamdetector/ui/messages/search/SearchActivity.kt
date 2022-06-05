package com.kofo.spamdetector.ui.messages.search

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.SearchView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.kofo.spamdetector.data.model.SmsMlResult
import com.kofo.spamdetector.databinding.ActivitySearchBinding
import com.kofo.spamdetector.ui.messages.MainActivity
import com.kofo.spamdetector.ui.messages.SmsListAdapter
import com.kofo.spamdetector.ui.messages.SmsViewModel

class SearchActivity : AppCompatActivity(), SmsListAdapter.ClickListener {
    private lateinit var binding: ActivitySearchBinding
    private var smsViewModel: SmsViewModel? = null
    private var smsListAdapter: SmsListAdapter? = null

    fun getSearchActivityIntent(context: Context?): Intent {
        return Intent(context, SearchActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        smsViewModel = ViewModelProvider(this)[SmsViewModel::class.java]

        smsListAdapter = SmsListAdapter()
        smsListAdapter!!.setClickListener(this)

        binding.searchView.isSubmitButtonEnabled
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.toString() != "") {

                    smsViewModel?.findSms(newText)

                    smsViewModel?.getResult()?.observe(this@SearchActivity) {

                        if (it.isNotEmpty()) {
                            setupListRecyclerView()
                            smsListAdapter!!.setAllSms(this@SearchActivity, it)
                        }

                    }
                    if (binding.messagesRecycler.visibility == View.GONE) {
                        binding.messagesRecycler.visibility = View.VISIBLE
                    } else {
                        binding.messagesRecycler.visibility = View.VISIBLE
                    }
                } else {
                    if (binding.messagesRecycler.visibility == View.VISIBLE) {
                        binding.messagesRecycler.visibility = View.INVISIBLE
                    }
                }

                return false
            }

        })

        val closeBtnId = binding.searchView.context.resources.getIdentifier(
            "android:id/search_close_btn", null, null
        )
        val closeBtn = binding.searchView.findViewById<ImageView>(closeBtnId)
        closeBtn?.setOnClickListener {
            binding.messagesRecycler.visibility = View.GONE
            binding.searchView.setQuery("", false)
        }

        with(binding) {
            back.setOnClickListener {
                finish()
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setupListRecyclerView() {
        with(binding.messagesRecycler) {
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
            "Status" to result.is_spam
        )
        intent.putExtras(extra)
        startActivity(intent)
    }

    private fun toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT)
            .show()
    }
}