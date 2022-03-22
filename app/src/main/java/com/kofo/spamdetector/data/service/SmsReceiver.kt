package com.kofo.spamdetector.data.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.telephony.SmsMessage
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.kofo.spamdetector.R
import com.kofo.spamdetector.data.model.SmsMlResult
import com.kofo.spamdetector.data.preferences.SharedPreference
import com.kofo.spamdetector.data.repository.CheckForSpamRepository
import com.kofo.spamdetector.ui.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SmsReceiver : BroadcastReceiver() {

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onReceive(p0: Context?, intent: Intent?) {

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

                    SharedPreference(p0!!).saveValue("MessageText", msgText.toString())
                    SharedPreference(p0).saveValue("MessageFrom", msgFrom.toString())


                    val call = CheckForSpamRepository().checkForSpam(
                        SharedPreference(p0)
                            .getStringPreference(p0, "MessageText")!!, 2.5
                    )

                    call.enqueue(object : Callback<SmsMlResult> {
                        override fun onResponse(
                            call: Call<SmsMlResult>,
                            response: Response<SmsMlResult>
                        ) {
                            if (response.code() == 200) {
                                SharedPreference(p0).saveValue(
                                    "TextResult",
                                    response.body()!!.result
                                )
                                SharedPreference(p0).saveValue("IsSpam", response.body()!!.is_spam)
                                SharedPreference(p0).saveValue(
                                    "Score",
                                    response.body()!!.score.toString()
                                )
                                SharedPreference(p0).saveValue("SmsText", response.body()!!.text)

                                if (SharedPreference(p0).getBoolPreference(p0, "IsSpam")!!) {
                                    showNotification(p0, "isSpam")
                                    toast(p0, "The Message recieved just now is a spam")
                                } else {
                                    showNotification(p0, "isNotSpam")
                                    toast(p0, "The Message recieved just now is not a spam")
                                }

                            }
                        }

                        override fun onFailure(call: Call<SmsMlResult>, t: Throwable) {
                            toast(p0, "failed to fetch data!")
                            showNotification(p0, "failed")
                        }
                    })

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

    private fun showNotification(context: Context, status: String) {
        lateinit var mBuilder: NotificationCompat.Builder

        val contentIntent = PendingIntent.getActivity(
            context, 123,
            Intent(context, MainActivity::class.java), 0
        )
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        when (status) {
            "isSpam" -> {
                mBuilder = NotificationCompat.Builder(context, "com.kofo.spamdetector")
                    .setSmallIcon(R.drawable.ic_baseline_textsms_24)
                    .setContentTitle(context.resources.getString(R.string.app_name))
                    .setContentText("The Message recieved just now is a spam.")
            }
            "isNotSpam" -> {
                mBuilder = NotificationCompat.Builder(context, "com.kofo.spamdetector")
                    .setSmallIcon(R.drawable.ic_baseline_textsms_24)
                    .setContentTitle(context.resources.getString(R.string.app_name))
                    .setContentText("The Message recieved just now is not a spam.")
            }
            "failed" -> {
                mBuilder = NotificationCompat.Builder(context, "com.kofo.spamdetector")
                    .setSmallIcon(R.drawable.ic_baseline_textsms_24)
                    .setContentTitle(context.resources.getString(R.string.app_name))
                    .setContentText("Please, check your internet connection.")
            }
        }
        mBuilder.setContentIntent(contentIntent)
        mBuilder.setDefaults(Notification.DEFAULT_SOUND)
        mBuilder.setAutoCancel(true)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val notificationChannel = NotificationChannel(
                "com.kofo.spamdetector",
                "NOTIFICATION_CHANNEL_NAME",
                importance
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }
        notificationManager.notify(123, mBuilder.build())
    }

}