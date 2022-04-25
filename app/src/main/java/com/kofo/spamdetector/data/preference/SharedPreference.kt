package com.kofo.spamdetector.data.preference

import android.content.Context
import android.content.SharedPreferences

import android.security.keystore.KeyProperties

import android.security.keystore.KeyGenParameterSpec
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

import java.lang.Exception
import java.util.*


class SharedPreference(private var context: Context) {

    private var preferenceName = "SpamDetector"

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

    fun saveValue(key: String?, value: String?) {
        val sharedPreferences = getEncryptedSharedPreferences()
        val editor = sharedPreferences!!.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun saveValue(key: String?, value: Boolean?) {
        val sharedPreferences = getEncryptedSharedPreferences()
        val editor = sharedPreferences!!.edit()
        editor.putBoolean(key, value!!)
        editor.apply()
    }


    fun getStringPreference(key: String?): String? {
        return getEncryptedSharedPreferences()!!.getString(key, null)
    }

    fun getBoolPreference(key: String?): Boolean? {
        return getEncryptedSharedPreferences()!!.getBoolean(key, false)
    }

}