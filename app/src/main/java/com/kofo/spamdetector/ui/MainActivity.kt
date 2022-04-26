package com.kofo.spamdetector.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.kofo.spamdetector.data.model.SmsMlResult
import com.kofo.spamdetector.data.preference.SharedPreference
import com.kofo.spamdetector.data.service.ActivityStarter
import com.kofo.spamdetector.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var backPass: Long? = 0
    var smsViewModel: SmsViewModel? = null
    private lateinit var binding: ActivityMainBinding

    fun getMainActivityIntent(context: Context?): Intent {
        return Intent(context, MainActivity::class.java)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        smsViewModel = ViewModelProvider(this)[SmsViewModel::class.java]

        //permission
        permission()

        //get data
        data()

        checkSmsRedundancyAndInsert()

        binding.root.setOnRefreshListener {

            data()

            checkSmsRedundancyAndInsert()

            binding.root.isRefreshing = false
        }

        binding.allMessages.setOnClickListener {
            ActivityStarter.startActivity(
                this@MainActivity,
                AllSmsActivity().getAllSmsActivityIntent(this), false
            )
        }

    }

    private fun checkSmsRedundancyAndInsert() {
        smsViewModel!!.checkSms(
            SharedPreference(this).getStringPreference("SmsText"),
            SharedPreference(this).getStringPreference("MessageText")
        )

        smsViewModel!!.getCheckedSms().observe(this) {
            if (it > 0) {
                toast("SMS already exists in database")
            } else {
                addSms(this)
                toast("SMS saved successfully")
            }
        }
    }

    @SuppressLint("SetTextI18n")
    fun data() {
        val sms = intent.extras?.getString("SmsText")
        if (!sms.isNullOrEmpty()) {

            toast("From Bundle")
            when (intent.extras!!.getBoolean("Status")) {
                true -> {
                    binding.circleError.visibility = View.VISIBLE
                    binding.errorIcon.visibility = View.VISIBLE
                    binding.circlePass.visibility = View.GONE
                    binding.passIcon.visibility = View.GONE
                }
                false -> {
                    binding.circlePass.visibility = View.VISIBLE
                    binding.passIcon.visibility = View.VISIBLE
                    binding.circleError.visibility = View.GONE
                    binding.errorIcon.visibility = View.GONE
                }
            }

            binding.noData.visibility = View.GONE
            binding.title.visibility = View.VISIBLE
            binding.messageFrom.visibility = View.VISIBLE
            binding.messageScore.visibility = View.VISIBLE
            binding.textMessage.visibility = View.VISIBLE
            binding.totalResult.visibility = View.VISIBLE
            binding.allMessages.visibility = View.VISIBLE

            binding.messageFrom.text = intent.extras!!
                .getString("MessageFrom")

            binding.messageScore.text = intent.extras!!
                .getDouble("Score").toString()

            binding.textMessage.text = intent.extras!!
                .getString("SmsText")

            binding.totalResult.text = intent.extras!!
                .getString("TextResult")


            Handler(Looper.getMainLooper()).postDelayed({
                toast("last spam result!!!")
            }, 2000)

        } else {

            if (SharedPreference(this)
                    .getStringPreference("SmsText") != null
            ) {

                when (SharedPreference(this).getBoolPreference("IsSpam")!!) {
                    true -> {
                        binding.circleError.visibility = View.VISIBLE
                        binding.errorIcon.visibility = View.VISIBLE
                        binding.circlePass.visibility = View.GONE
                        binding.passIcon.visibility = View.GONE
                    }
                    false -> {
                        binding.circlePass.visibility = View.VISIBLE
                        binding.passIcon.visibility = View.VISIBLE
                        binding.circleError.visibility = View.GONE
                        binding.errorIcon.visibility = View.GONE
                    }
                }

                binding.noData.visibility = View.GONE
                binding.title.visibility = View.VISIBLE
                binding.messageFrom.visibility = View.VISIBLE
                binding.messageScore.visibility = View.VISIBLE
                binding.textMessage.visibility = View.VISIBLE
                binding.totalResult.visibility = View.VISIBLE
                binding.allMessages.visibility = View.VISIBLE
                binding.messageFromLabel.visibility = View.VISIBLE
                binding.messageScoreLabel.visibility = View.VISIBLE
                binding.textMessageLabel.visibility = View.VISIBLE
                binding.totalResultLabel.visibility = View.VISIBLE

                binding.messageFrom.text = SharedPreference(this)
                    .getStringPreference("MessageFrom")

                binding.messageScore.text = SharedPreference(this)
                    .getStringPreference("Score")

                binding.textMessage.text = SharedPreference(this)
                    .getStringPreference("SmsText")

                binding.totalResult.text = SharedPreference(this)
                    .getStringPreference("TextResult")

                Handler(Looper.getMainLooper()).postDelayed({
                    toast("last spam result!!!")
                }, 2000)


            } else {

                binding.circlePass.visibility = View.GONE
                binding.passIcon.visibility = View.GONE
                binding.circleError.visibility = View.GONE
                binding.errorIcon.visibility = View.GONE
                binding.noData.visibility = View.VISIBLE
                binding.title.visibility = View.GONE
                binding.messageFrom.visibility = View.GONE
                binding.messageScore.visibility = View.GONE
                binding.textMessage.visibility = View.GONE
                binding.totalResult.visibility = View.GONE
                binding.allMessages.visibility = View.GONE

                binding.messageFromLabel.visibility = View.GONE
                binding.messageScoreLabel.visibility = View.GONE
                binding.textMessageLabel.visibility = View.GONE
                binding.totalResultLabel.visibility = View.GONE


                Handler(Looper.getMainLooper()).postDelayed({
                    toast("No incoming messages to analyse!!!")
                }, 2000)
            }
        }


    }

    private fun addSms(context: Context?) {
        smsViewModel!!.insertSmsNow(
            SmsMlResult(
                null,
                SharedPreference(context!!).getBoolPreference("IsSpam")!!,
                SharedPreference(context).getStringPreference("TextResult")!!,
                SharedPreference(context).getStringPreference("Score")!!.toDouble(),
                SharedPreference(context).getStringPreference("SmsText")!!,
                SharedPreference(context).getStringPreference("MessageFrom")!!,
                SharedPreference(context).getStringPreference("MessageText")!!,
                ""
            )
        )
    }

    private fun permission() {
        Dexter.withContext(this)
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