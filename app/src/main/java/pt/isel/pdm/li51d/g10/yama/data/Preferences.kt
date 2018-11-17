package pt.isel.pdm.li51d.g10.yama.data

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences

object Preferences {

    private lateinit var preferences : SharedPreferences

    fun init(context: Context) {
        preferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
    }

    fun set(key: String, value: String) {
        with(preferences.edit()) {
            putString(key, value)
            apply()
        }
    }
    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    fun get(key: String): String = preferences.getString(key, "")

    @SuppressLint("ApplySharedPref")
    fun remove(key: String, value: String) {
        with(preferences.edit()) {
            remove(key, value)
            commit()
        }
    }
}