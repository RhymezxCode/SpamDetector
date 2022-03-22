package com.kofo.spamdetector.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.text.Html
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.google.android.material.snackbar.Snackbar
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.kofo.spamdetector.data.preferences.SharedPreference
import com.kofo.spamdetector.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private var backPass: Long? = 0
    private lateinit var binding: ActivityMainBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //permission
        permission()

        //get data
        data()

        binding.root.setOnRefreshListener {


            data()

            binding.root.isRefreshing = false
        }


    }

    @SuppressLint("SetTextI18n")
    fun data() {
        if (SharedPreference(this)
                .getStringPreference(this, "SmsText") != null
        ) {
            binding.fullInfo.text = Html.fromHtml(
                "" +
                        "<br><b>Message From: </b>" + SharedPreference(this)
                    .getStringPreference(this, "MessageFrom") +
                        "<br><br><b>Message Score: </b>" + SharedPreference(this)
                    .getStringPreference(this, "Score") +
                        "<br><br><b>Text Message: </b>" + SharedPreference(this)
                    .getStringPreference(this, "SmsText") +
                        "<br><br><b>Total Result: </b>" + SharedPreference(this)
                    .getStringPreference(this, "TextResult")
            )

            binding.user.visibility = View.VISIBLE
            binding.noData.visibility = View.GONE
            binding.fullInfo.visibility = View.VISIBLE
            binding.title.visibility = View.VISIBLE

            toast("last spam result!!!")
        } else {
            binding.user.visibility = View.GONE
            binding.noData.visibility = View.VISIBLE
            binding.fullInfo.visibility = View.GONE
            binding.title.visibility = View.GONE

            toast("No incoming messages to analyse!!!")
        }
    }

    private fun permission() {
        Dexter.withActivity(this)
            .withPermission(
                Manifest.permission.RECEIVE_SMS
            )
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                    //Do nothing
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                    showSettingsDialog()
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                }
            }).check()
    }

    private fun showSettingsDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Need Permissions")
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.")
        builder.setPositiveButton(
            "GOTO SETTINGS"
        ) { dialog, _ ->
            dialog.cancel()
            openSettings()
        }
        builder.setNegativeButton(
            "CANCEL"
        ) { dialog, _ ->
            dialog.cancel()
        }
        builder.show()
    }

    private fun openSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivityForResult(intent, 101)
    }

    override fun onBackPressed() {
        if (backPass!! + 2000 > System.currentTimeMillis()) {
            val a = Intent(Intent.ACTION_MAIN)
            a.addCategory(Intent.CATEGORY_HOME)
            a.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(a)
            finishAffinity()
        } else {
            Snackbar.make(
                findViewById(android.R.id.content),
                "Touch again to exit",
                Snackbar.LENGTH_SHORT
            ).show()
            backPass = System.currentTimeMillis()
        }
    }

    private fun toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT)
            .show()
    }
}