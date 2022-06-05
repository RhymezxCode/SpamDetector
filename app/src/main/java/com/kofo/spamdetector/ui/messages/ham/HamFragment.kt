package com.kofo.spamdetector.ui.messages.ham

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kofo.spamdetector.R

class HamFragment : Fragment() {

    companion object {
        fun newInstance() = HamFragment()
    }

    private lateinit var viewModel: HamViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_ham, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(HamViewModel::class.java)
        // TODO: Use the ViewModel
    }

}