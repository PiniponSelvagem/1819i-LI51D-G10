package pt.isel.pdm.i41n.g6.yama

import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.Toast
import com.android.volley.*
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONException
import org.json.JSONObject
import pt.isel.pdm.i41n.g6.yama.data.LoggedUser
import pt.isel.pdm.i41n.g6.yama.data.httprequests.Volley
import pt.isel.pdm.i41n.g6.yama.organization.TeamsActivity

//TODO: save state
class LoginActivity : AppCompatActivity() {

    private lateinit var preferences : SharedPreferences
    private lateinit var pEditor     : Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        preferences = PreferenceManager.getDefaultSharedPreferences(this)
        pEditor     = preferences.edit()

        checkSharedPreferences()

        Volley.init(this)

        login_button.setOnClickListener {
            disableInteraction()

            if (login_checkbox.isChecked) {
                saveCredentials()
            } else {
                forgetCredentials()
            }
            pEditor.apply()

            val headers = mutableMapOf<String, String>()
            headers["Authorization"] = "token "+login_token.text.toString()


            val queue = Volley.getRequestQueue()
            val myReq = Volley.HeadersStringRequest(Request.Method.GET,
                    "https://api.github.com/user",
                    headers,
                    createReqSuccessListener(),
                    createReqErrorListener())
            queue.add(myReq)
        }
    }

    private fun createReqSuccessListener(): Response.Listener<String> = Response.Listener {
        response ->
        run {
            try {
                val i = Intent(this, TeamsActivity::class.java)
                i.putExtra("loggedUser", loggedUserData(response))
                startActivity(i)
                finish()
            } catch (e: JSONException) {
                e.printStackTrace() //TODO: do logging
            }
        }
    }

    private fun createReqErrorListener(): Response.ErrorListener = Response.ErrorListener {
        error ->
        run {
            if (error is TimeoutError || error is NoConnectionError) {
                Toast.makeText(this, "Timeout", Toast.LENGTH_LONG).show()
            } else if (error is AuthFailureError) {
                Toast.makeText(this, "Authentication Error", Toast.LENGTH_LONG).show()
            } else if (error is ServerError) {
                Toast.makeText(this, "Server Error", Toast.LENGTH_LONG).show()
            } else if (error is NetworkError) {
                Toast.makeText(this, "Network Error", Toast.LENGTH_LONG).show()
            } else if (error is ParseError) {
                Toast.makeText(this, "Parse Error", Toast.LENGTH_LONG).show()
            }

            enableInteraction()
        }
    }


    private fun disableInteraction() {
        login_token.isEnabled = false
        login_orgID.isEnabled = false
        login_checkbox.isEnabled = false
        login_button.isEnabled = false
    }

    private fun enableInteraction() {
        login_token.isEnabled = true
        login_orgID.isEnabled = true
        login_checkbox.isEnabled = true
        login_button.isEnabled = true
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

    private fun loggedUserData(response: String) : LoggedUser {
        val jObj = JSONObject(response)
        return LoggedUser(
                login_token.text.toString(),
                login_orgID.text.toString(),

                jObj.getString("login"),
                jObj.getString("name"),
                jObj.getInt("id"),
                jObj.getString("avatar_url"),
                jObj.getInt("followers"),
                jObj.getInt("following"),
                jObj.getString("email"),
                jObj.getString("location"),
                jObj.getString("blog"),
                jObj.getString("bio")
        )
    }
}
