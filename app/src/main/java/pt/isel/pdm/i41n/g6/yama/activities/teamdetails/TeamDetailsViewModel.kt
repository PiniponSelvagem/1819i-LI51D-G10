package pt.isel.pdm.i41n.g6.yama.activities.teamdetails

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import pt.isel.pdm.i41n.g6.yama.R
import pt.isel.pdm.i41n.g6.yama.data.Preferences
import pt.isel.pdm.i41n.g6.yama.data.dto.User
import pt.isel.pdm.i41n.g6.yama.network.HttpRequests
import java.lang.Thread.sleep

class TeamDetailsViewModel : ViewModel() {

    private val teamUsersLiveData = MutableLiveData<MutableList<User>>()
    val teamUsers: LiveData<MutableList<User>> = teamUsersLiveData

    init {
        teamUsersLiveData.value = mutableListOf()
    }

    fun loadTeam(teamID: Int, success: (Unit) -> Unit, fail: (Exception) -> Unit) {
        val token = Preferences.get(R.string.spKey__login_token.toString())
        val headers = mutableMapOf<String, String>()
        headers["Authorization"] = "token $token"

        HttpRequests.getString("https://api.github.com/teams/$teamID/members", headers,
                resp = { str ->
                    run {
                        try {
                            teamData(str, success, fail)
                            success.invoke(Unit)
                        } catch (e: JSONException) {
                            e.printStackTrace() //TODO: do logging
                        }
                    }
                },
                err = { e -> fail.invoke(e) }
        )
    }

    private fun teamData(response: String, success: (Unit) -> Unit, fail: (Exception) -> Unit) {
        val token = Preferences.get(R.string.spKey__login_token.toString())
        val headers = mutableMapOf<String, String>()
        headers["Authorization"] = "token $token"

        val jArray = JSONArray(response)
        var jObj: JSONObject
        for ( i in 0 until jArray.length()) {
            jObj = jArray[i] as JSONObject

            HttpRequests.getString(jObj.getString("url"), headers,
                    resp = { str ->
                        run {
                            try {
                                teamUsersLiveData.value!!.add(getUserData(str, success, fail))
                                success.invoke(Unit)
                            } catch (e: JSONException) {
                                e.printStackTrace() //TODO: do logging
                            }
                        }
                    },
                    err = { e -> fail.invoke(e) }
            )
        }
    }


    private fun getUserData(response: String, success: (Unit) -> Unit, fail: (Exception) -> Unit) : User {
        val jObj = JSONObject(response)
        val user = User(
                "",
                "",

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

        //TODO: getBitmap values maxWidth=500, maxHeight=500 should be the imageView sizes
        HttpRequests.getBitmap(jObj.getString("avatar_url"), 500, 500,
                resp = { bitmap ->
                    run {
                        user.avatar = bitmap
                        success.invoke(Unit)
                    }
                },
                err = { e -> fail.invoke(e) }
        )

        return user
    }
}