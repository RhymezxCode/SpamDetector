package com.kofo.spamdetector.ui.messages.ham

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.kofo.spamdetector.R
import com.kofo.spamdetector.data.model.SmsMlResult
import com.kofo.spamdetector.databinding.FragmentHamBinding
import com.kofo.spamdetector.ui.messages.SmsListAdapter
import com.kofo.spamdetector.ui.messages.SmsViewModel

class HamFragment : Fragment() {

    private var _binding: FragmentHamBinding? = null
    private var smsViewModel: SmsViewModel? = null
    private var smsListAdapter: SmsListAdapter? = null
    private var hamSmsList: ArrayList<SmsMlResult> = ArrayList()
    // requireContext() property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHamBinding.inflate(inflater, container, false)

        smsViewModel = ViewModelProvider(this)[SmsViewModel::class.java]
        smsListAdapter = SmsListAdapter()

        smsViewModel?.getAllSms()?.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {

                for(sms in it){
                    if(!sms.is_spam){
                        hamSmsList.add(sms)
                    }
                }

                setupListRecyclerView()
                smsListAdapter!!.setAllSms(requireActivity(), hamSmsList.toList())

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