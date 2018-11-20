package pt.isel.pdm.li51d.g10.yama.activities.teamdetails

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import pt.isel.pdm.li51d.g10.yama.R
import pt.isel.pdm.li51d.g10.yama.data.Preferences
import pt.isel.pdm.li51d.g10.yama.data.Repository
import pt.isel.pdm.li51d.g10.yama.data.dto.User

class TeamDetailsViewModel : ViewModel() {

    private val teamUsersLiveData = MutableLiveData<MutableList<User>>()
    val teamUsers: LiveData<MutableList<User>> = teamUsersLiveData

    private val isRefreshedLiveData = MutableLiveData<Boolean>()
    val isRefreshed = isRefreshedLiveData

    init {
        teamUsersLiveData.value = mutableListOf()
        isRefreshedLiveData.value = false
    }

    //TODO: WIP refresh by user request
    fun refresh(teamID: Int, width: Int, height: Int,
                success: (Unit) -> Unit, fail: (Exception) -> Unit) {
        isRefreshedLiveData.value = false
        loadTeamMembers(teamID, width, height, success, fail)
    }

    fun loadTeamMembers(teamID: Int, width: Int, height: Int,
                        success: (Unit) -> Unit, fail: (Exception) -> Unit) {
        val token = Preferences.get(R.string.spKey__login_token.toString())
        val headers = mutableMapOf<String, String>()
        headers["Authorization"] = "token $token"

        Repository.getTeamMembers(headers, teamID,
                resp = { str ->
                    run {
                        try {
                            teamData(str, width, height, success, fail)
                            success.invoke(Unit)
                            isRefreshedLiveData.value = true
                        } catch (e: JSONException) {
                            e.printStackTrace() //TODO: do logging
                        }
                    }
                },
                err = { e -> fail.invoke(e) }
        )
    }

    private fun teamData(response: String, width: Int, height: Int,
                         success: (Unit) -> Unit, fail: (Exception) -> Unit) {
        val token = Preferences.get(R.string.spKey__login_token.toString())
        val headers = mutableMapOf<String, String>()
        headers["Authorization"] = "token $token"

        val jArray = JSONArray(response)
        var jObj: JSONObject
        for (i in 0 until jArray.length()) {
            jObj = jArray[i] as JSONObject

            Repository.getTeamData(jObj.getString("url"), headers,
                    resp = { str ->
                        run {
                            try {
                                teamUsersLiveData.value!!.add(getUserData(str, width, height, success, fail))
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


    private fun getUserData(response: String, width: Int, height: Int,
                            success: (Unit) -> Unit, fail: (Exception) -> Unit): User {
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

        Repository.getUserImage(jObj.getString("avatar_url"), width, height,
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