package pt.isel.pdm.i41n.g6.yama.activities.login

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import org.json.JSONException
import org.json.JSONObject
import pt.isel.pdm.i41n.g6.yama.R
import pt.isel.pdm.i41n.g6.yama.data.Preferences
import pt.isel.pdm.i41n.g6.yama.data.dto.User
import pt.isel.pdm.i41n.g6.yama.network.HttpRequests
import java.lang.Exception

class LoginViewModel : ViewModel() {

    private val userLiveData = MutableLiveData<User>()
    val userLogged: LiveData<User> = userLiveData

    fun loginUser(success: (Unit) -> Unit, fail: (Exception) -> Unit) {
        val token = Preferences.get(R.string.spKey__login_token.toString())
        val orgID = Preferences.get(R.string.spKey__login_orgID.toString())
        val headers = mutableMapOf<String, String>()
        headers["Authorization"] = "token $token"

        HttpRequests.getString("https://api.github.com/user", headers,
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

    private fun loggedUserData(token: String, orgID: String, response: String) : User {
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
}