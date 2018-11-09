package pt.isel.pdm.i41n.g6.yama

import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import kotlinx.android.synthetic.main.activity_login.*
import pt.isel.pdm.i41n.g6.yama.data.HttpRequests


class LoginActivity : AppCompatActivity() {

    private lateinit var preferences : SharedPreferences
    private lateinit var pEditor     : Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        preferences = PreferenceManager.getDefaultSharedPreferences(this)
        pEditor     = preferences.edit()

        checkSharedPreferences()

        login_button.setOnClickListener {
            if (login_checkbox.isChecked) {
                saveCredentials()
            } else {
                forgetCredentials()
            }
            pEditor.apply()

            //TODO: example intent for now
            startActivity(Intent(this, UserActivity::class.java))
        }
    }


    private fun checkSharedPreferences() {
        login_orgID.setText(preferences.getString(R.string.spKey__login_orgID.toString(), ""))
        login_token.setText(preferences.getString(R.string.spKey__login_token.toString(), ""))
        login_checkbox.isChecked = preferences.getBoolean(R.string.spKey__login_checkbox.toString(), false)
    }

    private fun saveCredentials() {
        pEditor.putBoolean(R.string.spKey__login_checkbox.toString(), true)
        pEditor.putString(R.string.spKey__login_orgID.toString(), login_orgID.text.toString())
        pEditor.putString(R.string.spKey__login_token.toString(), login_token.text.toString())
    }

    private fun forgetCredentials() {
        pEditor.putBoolean(R.string.spKey__login_checkbox.toString(), false)
        pEditor.putString(R.string.spKey__login_orgID.toString(), "")
        pEditor.putString(R.string.spKey__login_token.toString(), "")
    }
}
