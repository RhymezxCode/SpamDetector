package com.kofo.spamdetector.ui.messages.spam

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.kofo.spamdetector.data.model.SmsMlResult
import com.kofo.spamdetector.databinding.FragmentSpamBinding
import com.kofo.spamdetector.ui.messages.SmsListAdapter
import com.kofo.spamdetector.ui.messages.SmsViewModel

class SpamFragment : Fragment() {

    private var _binding: FragmentSpamBinding? = null
    private var smsViewModel: SmsViewModel? = null
    private var smsListAdapter: SmsListAdapter? = null
    private var spamSmsList: ArrayList<SmsMlResult> = ArrayList()
    // requireContext() property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSpamBinding.inflate(inflater, container, false)

        smsViewModel = ViewModelProvider(this)[SmsViewModel::class.java]
        smsListAdapter = SmsListAdapter()

        smsViewModel?.getAllSms()?.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {

                for(sms in it){
                    if(sms.is_spam){
                        spamSmsList.add(sms)
                    }
                }

                setupListRecyclerView()
                smsListAdapter!!.setAllSms(requireActivity(), spamSmsList.toList())

            }

        }

        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setupListRecyclerView() {
        with(binding.messagesRecycler) {
            layoutManager = LinearLayoutManager(context)
            adapter = smsListAdapter
            smsListAdapter!!.notifyDataSetChanged()
        }
    }

}