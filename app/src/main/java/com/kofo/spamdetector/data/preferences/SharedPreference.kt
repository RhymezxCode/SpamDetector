package com.kofo.spamdetector.data.preferences

import android.content.Context
import android.content.SharedPreferences
import android.os.Build

import android.security.keystore.KeyProperties

import android.security.keystore.KeyGenParameterSpec
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

import java.lang.Exception
import java.util.*


class SharedPreference(context: Context) {

    private var preferenceName = "SpamDetector"
    private var context = context

    @RequiresApi(Build.VERSION_CODES.M)
    private fun getMasterKey(): MasterKey? {
        try {
            val spec = KeyGenParameterSpec.Builder(
                "_androidx_security_master_key_",
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            )
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .setKeySize(256)
                .build()
            return MasterKey.Builder(context)
                .setKeyGenParameterSpec(spec)
                .build()
        } catch (e: Exception) {
            Log.e(javaClass.simpleName, "Error on getting master key", e)
        }
        return null
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun getEncryptedSharedPreferences(): SharedPreferences? {
        try {
            return getMasterKey()?.let {
                EncryptedSharedPreferences.create(
                    Objects.requireNonNull(context),
                    preferenceName,
                    it,  // calling the method above for creating MasterKey
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
                )
            }
        } catch (e: Exception) {
            Log.e(javaClass.simpleName, "Error on getting encrypted shared preferences", e)
        }
        return null
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun saveValue(key: String?, value: String?) {
        val sharedPreferences = getEncryptedSharedPreferences()
        val editor = sharedPreferences!!.edit()
        editor.putString(key, value)
        editor.apply()
    }
    @RequiresApi(Build.VERSION_CODES.M)
    fun saveValue(key: String?, value: Boolean?) {
        val sharedPreferences = getEncryptedSharedPreferences()
        val editor = sharedPreferences!!.edit()
        editor.putBoolean(key, value!!)
        editor.apply()
    }


    @RequiresApi(Build.VERSION_CODES.M)
    fun getStringPreference(context: Context, key: String?): String? {
        return getEncryptedSharedPreferences()!!.getString(key, null)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun getBoolPreference(context: Context, key: String?): Boolean? {
        return getEncryptedSharedPreferences()!!.getBoolean(key, false)
    }


}