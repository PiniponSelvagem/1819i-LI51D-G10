package pt.isel.pdm.i41n.g6.yama.activities.login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONException
import org.json.JSONObject
import pt.isel.pdm.i41n.g6.yama.R
import pt.isel.pdm.i41n.g6.yama.data.User
import pt.isel.pdm.i41n.g6.yama.network.HttpRequests
import pt.isel.pdm.i41n.g6.yama.activities.teams.TeamsActivity
import pt.isel.pdm.i41n.g6.yama.utils.showHttpErrorToast

//TODO: save state
class LoginActivity : AppCompatActivity() {

    private lateinit var preferences : SharedPreferences
    private lateinit var pEditor     : Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        preferences = this.getSharedPreferences("login", Context.MODE_PRIVATE)
        pEditor     = preferences.edit()

        checkSharedPreferences()

        login_button.setOnClickListener {
            saveCredentials()
            pEditor.apply()

            disableInteraction()

            val headers = mutableMapOf<String, String>()
            headers["Authorization"] = "token ${login_token.text}"

            HttpRequests.getString("https://api.github.com/user", headers,
                    resp = { str ->
                        run {
                            try {
                                val i = Intent(this, TeamsActivity::class.java)
                                i.putExtra("loggedUser", loggedUserData(str))
                                startActivity(i)
                                finish()
                            } catch (e: JSONException) {
                                e.printStackTrace() //TODO: do logging
                            }
                        }
                    },
                    err = { e ->
                        run {
                            enableInteraction()
                            showHttpErrorToast(this, e)
                        }
                    }
            )
        }
    }


    private fun disableInteraction() {
        login_token.isEnabled = false
        login_orgID.isEnabled = false
        login_button.isEnabled = false
    }

    private fun enableInteraction() {
        login_token.isEnabled = true
        login_orgID.isEnabled = true
        login_button.isEnabled = true
    }



    private fun checkSharedPreferences() {
        login_orgID.setText(preferences.getString(R.string.spKey__login_orgID.toString(), ""))
        login_token.setText(preferences.getString(R.string.spKey__login_token.toString(), ""))
    }

    private fun saveCredentials() {
        pEditor.putString(R.string.spKey__login_orgID.toString(), login_orgID.text.toString())
        pEditor.putString(R.string.spKey__login_token.toString(), login_token.text.toString())
    }

    //TODO: maybe use DTO
    private fun loggedUserData(response: String) : User {
        val jObj = JSONObject(response)
        return User(
                "token ${login_token.text}",
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
