package pt.isel.pdm.li51d.g10.yama.activities.teams

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.graphics.Bitmap
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import pt.isel.pdm.li51d.g10.yama.R
import pt.isel.pdm.li51d.g10.yama.data.Preferences
import pt.isel.pdm.li51d.g10.yama.data.Repository
import pt.isel.pdm.li51d.g10.yama.data.dto.Team
import pt.isel.pdm.li51d.g10.yama.data.dto.User

class TeamsViewModel : ViewModel() {

    private val loggedUserLiveData = MutableLiveData<User>()
    val loggedUser: LiveData<User> = loggedUserLiveData

    private val teamsLiveData = MutableLiveData<MutableList<Team>>()
    val teams: LiveData<MutableList<Team>> = teamsLiveData

    private val loggedUserTeamsLiveData = MutableLiveData<MutableList<Team>>()
    val loggedUserTeams: LiveData<MutableList<Team>> = loggedUserTeamsLiveData

    private val isTeamsRefreshedLiveData = MutableLiveData<Boolean>()
    val isTeamsRefreshed = isTeamsRefreshedLiveData

    private val isLoggedUserRefreshedLiveData = MutableLiveData<Boolean>()
    val isLoggedUserRefreshed = isTeamsRefreshedLiveData


    init {
        teamsLiveData.value = mutableListOf()
        loggedUserTeamsLiveData.value = mutableListOf()
        isTeamsRefreshedLiveData.value = false
        isLoggedUserRefreshedLiveData.value = false
    }

    fun setLoggedUser(user: User) {
        loggedUserLiveData.value = user
    }


    //TODO: WIP refresh by user request
    fun refresh(success: (Unit) -> Unit, fail: (Exception) -> Unit) {
        isTeamsRefreshedLiveData.value = false
        isLoggedUserRefreshedLiveData.value = false
        loadTeams(success, fail)
    }

    fun loadLoggedUserAvatar(avatarUrl: String, success: (Bitmap) -> Unit, fail: (Exception) -> Unit) {
        Repository.getUserImage(avatarUrl, 250, 250, emptyMap(),
                resp = success.also { isLoggedUserRefreshedLiveData.value = true },
                err  = fail
        )
    }

    fun loadLoggedUserTeams(success: (Unit) -> Unit, fail: (Exception) -> Unit) {
        val token = Preferences.get(R.string.spKey__login_token.toString())
        val orgID = Preferences.get(R.string.spKey__login_orgID.toString())
        val headers = mutableMapOf<String, String>()
        headers["Authorization"] = "token $token"

        Repository.getUserTeams(headers,
                resp = { str ->
                    run {
                        try {
                            loggedUserTeamsData(str, orgID)
                            success.invoke(Unit)
                            isTeamsRefreshedLiveData.value = true
                        } catch (e: JSONException) {
                            e.printStackTrace() //TODO: do logging
                        }
                    }
                },
                err = { e -> fail.invoke(e) }
        )
    }

    fun loadTeams(success: (Unit) -> Unit, fail: (Exception) -> Unit) {
        val token = Preferences.get(R.string.spKey__login_token.toString())
        val orgID = Preferences.get(R.string.spKey__login_orgID.toString())
        val headers = mutableMapOf<String, String>()
        headers["Authorization"] = "token $token"

        Repository.getTeams(headers, orgID,
                resp = { str ->
                    run {
                        try {
                            teamsData(str)
                            success.invoke(Unit)
                            isTeamsRefreshedLiveData.value = true
                        } catch (e: JSONException) {
                            e.printStackTrace() //TODO: do logging
                        }
                    }
                },
                err = { e -> fail.invoke(e) }
        )
    }



    private fun loggedUserTeamsData(response: String, orgID: String) {
        val jArray = JSONArray(response)
        var jObj: JSONObject

        for (i in 0 until jArray.length()) {
            jObj = jArray[i] as JSONObject
            val jObjInner = jObj.getJSONObject("organization")
            if (jObjInner.getString("login") == orgID) {
                loggedUserTeamsLiveData.value!!.add(Team(jObj.getString("name"), jObj.getInt("id")))
            }
        }
    }

    private fun teamsData(response: String) {
        val jArray = JSONArray(response)
        var jObj: JSONObject

        for (i in 0 until jArray.length()) {
            jObj = jArray[i] as JSONObject
            teamsLiveData.value!!.add(Team(jObj.getString("name"), jObj.getInt("id")))
        }
    }
}