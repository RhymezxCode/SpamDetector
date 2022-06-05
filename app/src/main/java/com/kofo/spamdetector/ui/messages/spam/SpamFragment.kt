package com.kofo.spamdetector.ui.messages.spam

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kofo.spamdetector.R

class SpamFragment : Fragment() {

    companion object {
        fun newInstance() = SpamFragment()
    }

    private lateinit var viewModel: SpamViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_spam, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SpamViewModel::class.java)
        // TODO: Use the ViewModel
    }

}