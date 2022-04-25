package com.kofo.spamdetector.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kofo.spamdetector.R
import com.kofo.spamdetector.data.model.SmsMlResult


class SmsListAdapter :
    RecyclerView.Adapter<SmsListAdapter.SmsHolder>() {
    private var allSms: List<SmsMlResult> = ArrayList()
    var activity: Activity? = Activity()
    private var clickListener: ClickListener? = null

    fun setAllSms(activity: Activity, AllSms: List<SmsMlResult>) {
        this.allSms = AllSms
        this.activity = activity
        this.notifyDataSetChanged()
    }

    fun setClickListener(clickListener: ClickListener?) {
        this.clickListener = clickListener
    }


    interface ClickListener {
        fun gotoSmsPage(result: SmsMlResult)
    }

    override fun getItemCount(): Int {
        return allSms.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SmsHolder {
        val listItem =
            LayoutInflater.from(parent.context).inflate(R.layout.one_sms, parent, false)
        return SmsHolder(listItem)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: SmsHolder, position: Int) {
        val oneSms = allSms[position]
        /* bind the variables here
        */

        if(oneSms.is_spam){
            holder.messageIcon.setImageResource(R.drawable.error_icon)
        }else{
            holder.messageIcon.setImageResource(R.drawable.pass_icon)
        }
        holder.messageFrom.text = "From: ${oneSms.from}"
        holder.messageText.text = oneSms.realText

    }


    inner class SmsHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        var messageFrom: TextView = itemView.findViewById(R.id.message_from)
        var messageText: TextView = itemView.findViewById(R.id.message_text)
        var messageIcon: ImageView = itemView.findViewById(R.id.message_icon)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            try {
                val oneSms = allSms[adapterPosition]
                when (v.id) {
                    R.id.root_layout -> {
                        clickListener!!.gotoSmsPage(oneSms)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


}