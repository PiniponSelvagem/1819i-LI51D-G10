package pt.isel.pdm.li51d.g10.yama.data

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.util.Log

object Preferences {

    private val TAG: String = Preferences::class.java.simpleName

    private lateinit var preferences: SharedPreferences

    fun init(context: Context) {
        Log.i(TAG, "Init called")
        preferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
    }

    fun set(key: String, value: String) {
        with(preferences.edit()) {
            putString(key, value)
            apply()
        }
        Log.i(TAG, "New Shared Preference added")
    }

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    fun get(key: String): String {
        Log.i(TAG, "Getting Shared Preference")
        return preferences.getString(key, "")
    }

    @SuppressLint("ApplySharedPref")
    fun remove(key: String) {
        with(preferences.edit()) {
            remove(key)
            commit()
        }
        Log.i(TAG, "Shared Preference removed")
    }
}