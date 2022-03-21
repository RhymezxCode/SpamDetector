package com.kofo.spamdetector.data.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.telephony.SmsMessage
import androidx.annotation.RequiresApi
import com.kofo.spamdetector.data.preferences.SharedPreference


class SmsReceiver : BroadcastReceiver() {

    private var mLastTimeReceived = System.currentTimeMillis()


    @RequiresApi(Build.VERSION_CODES.M)
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
                        if (p0 != null) {
                            SharedPreference(p0).saveValue("MessageText", msgText)
                            SharedPreference(p0).saveValue("MessageFrom", msgFrom)

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                p0.startForegroundService(Intent(p0, SmsService::class.java))
                            } else {
                                p0.startService(Intent(p0, SmsService::class.java))
                            }
                        }
                        //
                        // Do some thing here
                        //
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

}