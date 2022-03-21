package com.kofo.spamdetector.data.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.kofo.spamdetector.R
import com.kofo.spamdetector.data.preferences.SharedPreference
import com.kofo.spamdetector.data.repository.CheckForSpamRepository
import com.kofo.spamdetector.ui.MainActivity

class SmsService : Service() {
    val NOTIFICATION_ID = 123
    var local: Intent? = null

    @Nullable
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate() {

    }

    override fun onDestroy() {
        super.onDestroy()

    }

//    private fun sendMessage(value: Double) {
//        Log.d("sender", "Broadcasting message")
//        val intent = Intent("Sms")
//        // You can also include some extra data.
//        intent.putExtra("data", value.toString())
//        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
//    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null) {
            downloadData()
        } else syncFinished()
        return START_STICKY
    }

    private fun syncFinished() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
//            stopForeground(true)
//        }
//        stopSelf()
//        isServiceRunning = false

        toast("Click on the notification to see the result inside the application")
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun downloadData() {
        toast("Checking message for spam!!!")
        try {
            val response = SharedPreference(this)
                .getStringPreference(this, "MessageText")
                ?.let { CheckForSpamRepository().checkForSpam(it, 2.5) }

            if (response != null) {
                if (response.isSuccessful) {

                    SharedPreference(this).saveValue("TextResult", response.body()!!.result)
                    SharedPreference(this).saveValue("IsSpam", response.body()!!.is_spam)
                    SharedPreference(this).saveValue("Score", response.body()!!.score.toString())
                    SharedPreference(this).saveValue("SmsText", response.body()!!.text)

                    if(SharedPreference(this).getBoolPreference(this, "IsSpam")!!){
                        toast("The Message recieved just now is a spam")
                    }else{
                        toast("The Message recieved just now is not a spam")
                    }

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                        startMyOwnForeground() else
                        startServiceWithNotification()

                    syncFinished()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    @RequiresApi(Build.VERSION_CODES.M)
    fun startServiceWithNotification() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent
            .getActivity(
                this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT
            )

        val notification: Notification = NotificationCompat.Builder(this)
            .setContentTitle(resources.getString(R.string.app_name))
            .setTicker(resources.getString(R.string.app_name))
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setContentText(
                "Message From: " + SharedPreference(this)
                    .getStringPreference(this, "MessageFrom") +
                        "Text Message: " + SharedPreference(this)
                    .getStringPreference(this, "SmsText") +
                        "Result: " + SharedPreference(this)
                    .getStringPreference(this, "TextResult")
            )
            .setSmallIcon(R.drawable.ic_baseline_textsms_24)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .build()
        notification.flags =
            notification.flags or Notification.FLAG_NO_CLEAR // NO_CLEAR makes the notification stay when the user performs a "delete all" command
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            startForeground(NOTIFICATION_ID, notification)
        }
    }

    private fun toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT)
            .show()
    }

    private fun feedback(a: String) {
        local!!.putExtra("message", a)
        sendBroadcast(local)
    }

    private fun logShow(message: String) {
        Log.e("Sync Log", "sync: $message")
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun startMyOwnForeground() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent
            .getActivity(
                this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT
            )

        val NOTIFICATION_CHANNEL_ID = "com.kofo.spamdetector"
        val channelName = if (SharedPreference(this)
                .getBoolPreference(this, "IsSpam")!!
        )
            "Spam message detected!"
        else
            "Message is not a spam!"
        val chan = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            channelName,
            NotificationManager.IMPORTANCE_HIGH
        )
        chan.lightColor = Color.GREEN
        val manager =
            (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?)!!
        manager.createNotificationChannel(chan)
        val notificationBuilder: NotificationCompat.Builder =
            NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
        val notification: Notification = notificationBuilder.setOngoing(true)
            .setContentTitle(resources.getString(R.string.app_name))
            .setTicker(resources.getString(R.string.app_name))
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setContentText(
                "Message From: " + SharedPreference(this)
                    .getStringPreference(this, "MessageFrom") +
                        "Text Message: " + SharedPreference(this)
                    .getStringPreference(this, "SmsText") +
                        "Result: " + SharedPreference(this)
                    .getStringPreference(this, "TextResult")
            )
            .setSmallIcon(R.drawable.ic_baseline_textsms_24)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .build()
        startForeground(NOTIFICATION_ID, notification)
    }
}