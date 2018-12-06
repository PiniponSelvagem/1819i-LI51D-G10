package pt.isel.pdm.li51d.g10.yama.activities.login

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.widget.EditText
import org.json.JSONException
import org.json.JSONObject
import pt.isel.pdm.li51d.g10.yama.R
import pt.isel.pdm.li51d.g10.yama.data.Repository
import pt.isel.pdm.li51d.g10.yama.data.dto.User

class LoginViewModel : ViewModel() {

    private val userLiveData = MutableLiveData<User>()
    val userLogged: LiveData<User> = userLiveData

    fun loginUser(success: (Unit) -> Unit, fail: (Exception) -> Unit) {
        val token = Repository.getSharedPreference(R.string.spKey__login_token.toString())
        val orgID = Repository.getSharedPreference(R.string.spKey__login_orgID.toString())
        val headers = mutableMapOf<String, String>()
        headers["Authorization"] = "token $token"

        Repository.getUser(headers,
                resp = { str ->
                    run {
                        try {
                            userLiveData.value = loggedUserData(token, orgID, str)
                            success.invoke(Unit)
                        } catch (e: JSONException) {
                            e.printStackTrace() //TODO: Logcat
                        }
                    }
                },
                err = { e -> fail.invoke(e) }
        )
    }

    private fun loggedUserData(token: String, orgID: String, response: String): User {
        val jObj = JSONObject(response)
        return User(
                "token $token",
                orgID,

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

    fun checkSharedPreferences(orgID: EditText, token: EditText) {
        orgID.setText(Repository.getSharedPreference(R.string.spKey__login_orgID.toString()))

        token.setText(Repository.getSharedPreference(R.string.spKey__login_token.toString()))
    }

    fun saveCredentials(orgID: EditText, token: EditText) {
        Repository.setSharedPreference(R.string.spKey__login_orgID.toString(), orgID.text.toString())

        Repository.setSharedPreference(R.string.spKey__login_token.toString(), token.text.toString())
    }
}