package pt.isel.pdm.li51d.g10.yama.data

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import pt.isel.pdm.li51d.g10.yama.R
import pt.isel.pdm.li51d.g10.yama.data.database.YamaDatabase
import pt.isel.pdm.li51d.g10.yama.data.database.team.Team
import pt.isel.pdm.li51d.g10.yama.data.database.team.teamusers.TeamUser
import pt.isel.pdm.li51d.g10.yama.network.HttpRequests

class Repository(private val db: YamaDatabase) {

    // YamaDatabase LiveData
    val loggedUser   = db.loggedUser
    val allTeams     = db.allTeams
    val teamUsers    = db.teamUsers
    val userMessages = db.userMessages

    @SuppressLint("StaticFieldLeak")
    fun loginUser(success: (Unit) -> Unit, fail: (Exception) -> Unit) {
        object: AsyncTask<Unit, Void, Void>() {
            override fun doInBackground(vararg params: Unit?): Void? {
                val cachedUserID = getSharedPreference(R.string.spKey__logged_id.toString())
                if (cachedUserID == "" || !db.isLoggedUserPresent(cachedUserID.toInt())) {
                    createLoggedUserFromWeb(success, fail)
                }
                else {
                    success.invoke(Unit)
                }
                return null
            }
        }.execute()
    }
    private fun createLoggedUserFromWeb(success: (Unit) -> Unit, fail: (Exception) -> Unit) {
        GithubApi.getLoggedUser(
                resp = { str ->
                    run {
                        try {
                            val jObj = JSONObject(str)
                            setSharedPreference(R.string.spKey__logged_id.toString(), jObj.getString("id"))
                            db.insertLoggedUser(convertJsonToUser(jObj))
                            success.invoke(Unit)
                        } catch (e: JSONException) {
                            e.printStackTrace() //TODO: Logcat
                        }
                    }
                },
                err = { e -> fail.invoke(e) }
        )
    }

    @SuppressLint("StaticFieldLeak")
    fun loadTeams(success: (Unit) -> Unit, fail: (Exception) -> Unit) {
        object: AsyncTask<Unit, Void, Void>() {
            override fun doInBackground(vararg params: Unit?): Void? {
                if (!db.isAllTeamsFilled()) {
                    createTeamFromWeb(success, fail)
                }
                else {
                    success.invoke(Unit)
                }
                return null
            }
        }.execute()
    }
    private fun createTeamFromWeb(success: (Unit) -> Unit, fail: (Exception) -> Unit) {
        GithubApi.getTeams(
                getSharedPreference(R.string.spKey__login_orgID.toString()),
                resp = { str ->
                    run {
                        try {
                            insertTeam(str)
                            success.invoke(Unit)
                        } catch (e: JSONException) {
                            e.printStackTrace() //TODO: do logging
                        }
                    }
                },
                err = { e -> fail.invoke(e) }
        )
    }
    private fun insertTeam(str: String) {
        val jArray = JSONArray(str)
        var jObj: JSONObject

        for (i in 0 until jArray.length()) {
            jObj = jArray[i] as JSONObject
            db.insert(Team(jObj.getInt("id"), jObj.getString("name")))
        }
    }

    @SuppressLint("StaticFieldLeak")
    fun getTeamUsers(teamID: Int, success: (Unit) -> Unit, fail: (Exception) -> Unit) {
        object: AsyncTask<Unit, Void, Void>() {
            override fun doInBackground(vararg params: Unit?): Void? {
                if (!db.getTeamUsers(teamID)) {
                    createTeamUsersFromWeb(teamID, success, fail)
                }
                return null
            }
        }.execute()
    }
    private fun createTeamUsersFromWeb(teamID: Int, success: (Unit) -> Unit, fail: (Exception) -> Unit) {
        GithubApi.getTeamUsers(teamID,
                resp = { str ->
                    run {
                        try {
                            teamUsers(teamID, str, success, fail)
                            success.invoke(Unit)
                        } catch (e: JSONException) {
                            e.printStackTrace() //TODO: do logging
                        }
                    }
                },
                err = { e -> fail.invoke(e) }
        )
    }
    private fun teamUsers(teamID: Int, response: String, success: (Unit) -> Unit, fail: (Exception) -> Unit) {
        val jArray = JSONArray(response)
        var jObj: JSONObject
        for (i in 0 until jArray.length()) {
            jObj = jArray[i] as JSONObject
            GithubApi.getUser(jObj.getString("url"),
                    resp = { str ->
                        run {
                            try {
                                val jObjUser = JSONObject(str)
                                db.insertTeamUsers(convertJsonToUser(jObjUser), TeamUser(teamID, jObjUser.getInt("id")))
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



    //TODO: maybe place in AvatarAPI
    fun getUserImage(url: String, width: Int, height: Int, headers: Map<String, String> = mapOf(),
                     resp: (Bitmap) -> Unit, err: (Exception) -> Unit){
        HttpRequests.getBitmap(url,
                width, height,
                headers, resp, err)
    }

    //TODO: part of the WIP for the navigation drawer to show "My teams"
    fun getUserTeams(resp: (String) -> Unit, err: (Exception) -> Unit){
        GithubApi.getUserTeams(resp, err)
    }

    fun getSharedPreference(key: String) : String {
        return Preferences.get(key)
    }

    fun setSharedPreference(key: String, value: String) {
        Preferences.set(key, value)
    }

    fun removeSharedPreference(key: String) {
        Preferences.remove(key)
    }

    fun deleteAllData() {
        db.deleteAllData()
    }

    fun loadMessages(teamId: Int) {
        db.getMessagesFromTeam(teamId)
    }
}