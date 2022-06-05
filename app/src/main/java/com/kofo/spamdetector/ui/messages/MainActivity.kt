package com.kofo.spamdetector.ui.messages

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
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
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
import com.kofo.spamdetector.ui.messages.option.OptionActivity
import com.kofo.spamdetector.ui.messages.search.SearchActivity

class MainActivity : AppCompatActivity() {
    private var backPass: Long? = 0
    private var smsViewModel: SmsViewModel? = null
    private lateinit var binding: ActivityMainBinding
    private var adapter: MessagesTabAdapter? = null

    fun getMainActivityIntent(context: Context?): Intent {
        return Intent(context, MainActivity::class.java)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        smsViewModel = ViewModelProvider(this)[SmsViewModel::class.java]

        with(binding) {
            //permission
            permission()

            //get data
            data()

            checkSmsRedundancyAndInsert()

            root.setOnRefreshListener {

                data()

                checkSmsRedundancyAndInsert()

                root.isRefreshing = false
            }

            binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Ham"))
            binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Spam"))

            val fragmentManager: FragmentManager = this@MainActivity.supportFragmentManager
            adapter = MessagesTabAdapter(fragmentManager, lifecycle)
            binding.viewPager2.adapter = adapter


            binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab) {
                    binding.viewPager2.currentItem = tab.position
                }

                override fun onTabUnselected(tab: TabLayout.Tab) {}
                override fun onTabReselected(tab: TabLayout.Tab) {}
            })

            binding.viewPager2.registerOnPageChangeCallback(object :
                ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    binding.tabLayout.selectTab(binding.tabLayout.getTabAt(position))
                }
            })


            allMessages.setOnClickListener {
                ActivityStarter.startActivity(
                    this@MainActivity,
                    AllSmsActivity().getAllSmsActivityIntent(this@MainActivity),
                    false
                )
            }

            menu.setOnClickListener {
                ActivityStarter.startActivity(
                    this@MainActivity,
                    OptionActivity().getOptionActivityIntent(this@MainActivity),
                    false
                )
            }

            search.setOnClickListener {
                ActivityStarter.startActivity(
                    this@MainActivity,
                    SearchActivity().getSearchActivityIntent(this@MainActivity),
                    false
                )
            }

        }
    }

    private fun checkSmsRedundancyAndInsert() {
        smsViewModel?.checkSms(
            SharedPreference(this).getStringPreference("SmsText"),
            SharedPreference(this).getStringPreference("MessageText")
        )

        smsViewModel?.getCheckedSms()?.observe(this) {
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
        with(binding) {
            val sms = intent.extras?.getString("SmsText")
            if (!sms.isNullOrEmpty()) {


                toast("From Bundle")
                when (intent.extras!!.getBoolean("Status")) {
                    true -> {
//                    circleError.visibility = View.VISIBLE
//                    errorIcon.visibility = View.VISIBLE
//                    circlePass.visibility = View.GONE
//                    passIcon.visibility = View.GONE
                    }
                    false -> {
//                    circlePass.visibility = View.VISIBLE
//                    passIcon.visibility = View.VISIBLE
//                    circleError.visibility = View.GONE
//                    errorIcon.visibility = View.GONE
                    }
                }

                noData.visibility = View.GONE
//            title.visibility = View.VISIBLE
//            messageFrom.visibility = View.VISIBLE
//            messageScore.visibility = View.VISIBLE
//            textMessage.visibility = View.VISIBLE
//            totalResult.visibility = View.VISIBLE
//            allMessages.visibility = View.VISIBLE

//            messageFrom.text = intent.extras!!
//                .getString("MessageFrom")
//
//            messageScore.text = intent.extras!!
//                .getDouble("Score").toString()
//
//            textMessage.text = intent.extras!!
//                .getString("SmsText")
//
//            totalResult.text = intent.extras!!
//                .getString("TextResult")


                Handler(Looper.getMainLooper()).postDelayed({
                    toast("last spam result!!!")
                }, 2000)

            } else {

                if (SharedPreference(this@MainActivity)
                        .getStringPreference("SmsText") != null
                ) {

                    when (SharedPreference(this@MainActivity)
                        .getBoolPreference("IsSpam")!!) {
                        true -> {
//                        circleError.visibility = View.VISIBLE
//                       errorIcon.visibility = View.VISIBLE
//                        circlePass.visibility = View.GONE
//                        passIcon.visibility = View.GONE
                        }
                        false -> {
//                        circlePass.visibility = View.VISIBLE
//                        passIcon.visibility = View.VISIBLE
//                        circleError.visibility = View.GONE
//                        errorIcon.visibility = View.GONE
                        }
                    }

                    noData.visibility = View.GONE
//                title.visibility = View.VISIBLE
//                messageFrom.visibility = View.VISIBLE
//                messageScore.visibility = View.VISIBLE
//                textMessage.visibility = View.VISIBLE
//                totalResult.visibility = View.VISIBLE
                    allMessages.visibility = View.VISIBLE
//                messageFromLabel.visibility = View.VISIBLE
//                messageScoreLabel.visibility = View.VISIBLE
//                textMessageLabel.visibility = View.VISIBLE
//                totalResultLabel.visibility = View.VISIBLE

//               messageFrom.text = SharedPreference(this@MainActivity)
//                    .getStringPreference("MessageFrom")
//                messageScore.text = SharedPreference(this@MainActivity)
//                    .getStringPreference("Score")
//
//                textMessage.text = SharedPreference(this@MainActivity)
//                    .getStringPreference("SmsText")
//
//                totalResult.text = SharedPreference(this@MainActivity)
//                    .getStringPreference("TextResult")

                    Handler(Looper.getMainLooper()).postDelayed({
                        toast("last spam result!!!")
                    }, 2000)


                } else {

//                circlePass.visibility = View.GONE
//                passIcon.visibility = View.GONE
//                circleError.visibility = View.GONE
//                errorIcon.visibility = View.GONE
                    noData.visibility = View.VISIBLE
//                title.visibility = View.GONE
//                messageFrom.visibility = View.GONE
//                messageScore.visibility = View.GONE
//                textMessage.visibility = View.GONE
//                totalResult.visibility = View.GONE
                    allMessages.visibility = View.GONE

//               messageFromLabel.visibility = View.GONE
//                messageScoreLabel.visibility = View.GONE
//                textMessageLabel.visibility = View.GONE
//                totalResultLabel.visibility = View.GONE


                    Handler(Looper.getMainLooper()).postDelayed({
                        toast("No incoming messages to analyse!!!")
                    }, 2000)
                }
            }

        }
    }

    private fun addSms(context: Context?) {
        smsViewModel!!.insertSmsNow(
            SharedPreference(context!!)
                .getBoolPreference("IsSpam")?.let { isSpam ->
                    SharedPreference(context)
                        .getStringPreference("TextResult")?.let { textResult ->
                            SharedPreference(context)
                                .getStringPreference("Score")?.let { score ->
                                    SharedPreference(context)
                                        .getStringPreference("SmsText")?.let { smsText ->
                                            SharedPreference(context)
                                                .getStringPreference("MessageFrom")
                                                ?.let { messageFrom ->
                                                    SharedPreference(context)
                                                        .getStringPreference("MessageText")
                                                        ?.let { messageText ->
                                                            SmsMlResult(
                                                                null,
                                                                isSpam,
                                                                textResult,
                                                                score.toDouble(),
                                                                smsText,
                                                                messageFrom,
                                                                messageText,
                                                                ""
                                                            )
                                                        }
                                                }
                                        }
                                }
                        }
                }
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
        builder.setMessage(
            "This app needs permission to use this feature. " +
                    "You can grant them in app settings."
        )
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