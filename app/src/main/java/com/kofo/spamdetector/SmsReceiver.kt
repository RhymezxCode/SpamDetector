package com.kofo.spamdetector

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsMessage

class SmsReceiver : BroadcastReceiver() {

    private var mLastTimeReceived = System.currentTimeMillis()

    override fun onReceive(p0: Context?, intent: Intent?) {
        val currentTimeMillis = System.currentTimeMillis()
        if (currentTimeMillis - mLastTimeReceived > 200) {
            mLastTimeReceived = currentTimeMillis

            val pdus: Array<*>
            val msgs: Array<SmsMessage?>
            var msgFrom: String?
            var msgText: String?
            val strBuilder = StringBuilder()
            intent?.extras?.let {
                try {
                    pdus = it.get("pdus") as Array<*>
                    msgs = arrayOfNulls(pdus.size)
                    for (i in msgs.indices) {
                        msgs[i] = SmsMessage.createFromPdu(pdus[i] as ByteArray)
                        strBuilder.append(msgs[i]?.messageBody)
                    }

                    msgText = strBuilder.toString()
                    msgFrom = msgs[0]?.originatingAddress

                    if (!msgFrom.isNullOrBlank() && !msgText.isNullOrBlank()) {
                        //
                        // Do some thing here
                        //
                    }
                } catch (e: Exception) {
                }
            }
        }
    }
}