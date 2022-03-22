package com.kofo.spamdetector.data.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.telephony.SmsMessage
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.kofo.spamdetector.data.model.SmsMlResult
import com.kofo.spamdetector.data.preferences.SharedPreference
import com.kofo.spamdetector.data.repository.CheckForSpamRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SmsReceiver : BroadcastReceiver() {

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onReceive(p0: Context?, intent: Intent?) {

        toast(p0!!, "recieved")


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
                    Log.e("MessageFrom", msgFrom.toString())
                    Log.e("MessageText", msgText.toString())
                    toast(p0, msgFrom.toString())
                    toast(p0, msgText.toString())

                    SharedPreference(p0).saveValue("MessageText", msgText.toString())
                    SharedPreference(p0).saveValue("MessageFrom", msgFrom.toString())


                    val call = CheckForSpamRepository().checkForSpam(SharedPreference(p0)
                        .getStringPreference(p0, "MessageText")!!, 2.5)

                    call.enqueue(object : Callback<SmsMlResult> {
                        override fun onResponse(call: Call<SmsMlResult>, response: Response<SmsMlResult>) {
                            if (response.code() == 200) {
                                SharedPreference(p0).saveValue("TextResult", response.body()!!.result)
                                SharedPreference(p0).saveValue("IsSpam", response.body()!!.is_spam)
                                SharedPreference(p0).saveValue("Score", response.body()!!.score.toString())
                                SharedPreference(p0).saveValue("SmsText", response.body()!!.text)

                                if(SharedPreference(p0).getBoolPreference(p0, "IsSpam")!!){
                                    toast(p0, "The Message recieved just now is a spam")
                                }else{
                                    toast(p0, "The Message recieved just now is not a spam")
                                }

                            }
                        }
                        override fun onFailure(call: Call<SmsMlResult>, t: Throwable) {
                            toast(p0, "failed to fetch data!")
                        }
                    })


//                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
//                                startMyOwnForeground() else
//                                startServiceWithNotification()
//
//                            syncFinished()



//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                        p0.startForegroundService(Intent(p0, SmsService::class.java))
//                    } else {
//                        p0.startService(Intent(p0, SmsService::class.java))
//                    }
                    //
                    // Do some thing here
                    //
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun toast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT)
            .show()
    }

}