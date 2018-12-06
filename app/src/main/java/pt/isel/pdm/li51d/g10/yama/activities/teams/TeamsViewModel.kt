package pt.isel.pdm.li51d.g10.yama.activities.teams

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import pt.isel.pdm.li51d.g10.yama.R
import pt.isel.pdm.li51d.g10.yama.data.Repository
import pt.isel.pdm.li51d.g10.yama.data.database.team.Team
import pt.isel.pdm.li51d.g10.yama.data.database.user.User

class TeamsViewModel(private val repository: Repository) : ViewModel() {

    val teams: LiveData<MutableList<Team>> = repository.allTeams

    val loggedUser: MutableLiveData<User> = repository.loggedUser

    fun loadTeams(success: (Unit) -> Unit, fail: (Exception) -> Unit) {
        repository.loadTeams(success, fail)
    }

    fun removeCredentials() {
        repository.removeSharedPreference(R.string.spKey__login_token.toString())
        repository.removeSharedPreference(R.string.spKey__login_orgID.toString())
    }






    fun loadLoggedUserAvatar(avatarUrl: String, success: (Bitmap) -> Unit, fail: (Exception) -> Unit) {
        repository.getUserImage(avatarUrl, 250, 250, emptyMap(),
                resp = success,
                err  = fail
        )
    }

    /*
    //TODO: feature WIP (navigation drawer show teams the logged user is in)
    fun loadLoggedUserTeams(success: (Unit) -> Unit, fail: (Exception) -> Unit) {
        val token = Preferences.get(R.string.spKey__login_token.toString())
        val orgID = Preferences.get(R.string.spKey__login_orgID.toString())
        val headers = mutableMapOf<String, String>()
        headers["Authorization"] = createTokenHeader(token)

        Repository.getUserTeams(headers,
                resp = { str ->
                    run {
                        try {
                            loggedUserTeamsData(str, orgID)
                            success.invoke(Unit)
                        } catch (e: JSONException) {
                            e.printStackTrace() //TODO: do logging
                        }
                    }
                },
                err = { e -> fail.invoke(e) }
        )
    }
    */

    /*
    //TODO: feature WIP (navigation drawer show teams the logged user is in)
    private fun loggedUserTeamsData(response: String, orgID: String) {
        val jArray = JSONArray(response)
        var jObj: JSONObject

        for (i in 0 until jArray.length()) {
            jObj = jArray[i] as JSONObject
            val jObjInner = jObj.getJSONObject("organization")
            if (jObjInner.getString("login") == orgID) {
                repository.insert(
                        Team(jObj.getInt("id"), jObj.getString("name"), jObjInner.getString("login"), true)
                )
            }
        }
    }
    */
}