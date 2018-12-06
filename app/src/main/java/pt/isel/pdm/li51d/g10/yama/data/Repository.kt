package pt.isel.pdm.li51d.g10.yama.data

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.AsyncTask
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
    val loggedUser = db.loggedUser
    val allTeams   = db.allTeams
    val allUsers   = db.allUsers
    val teamUsers  = db.teamUsers

    fun loginUser(success: (Unit) -> Unit, fail: (Exception) -> Unit) {
        if (loggedUser.value==null) {
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
        else {
            success.invoke(Unit)
        }
    }

    fun loadTeams(success: (Unit) -> Unit, fail: (Exception) -> Unit) {
        if (allTeams.value==null) {
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
        else {
            success.invoke(Unit)
        }
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
    fun getTeamMembers(teamID: Int, success: (Unit) -> Unit, fail: (Exception) -> Unit) {
        object: AsyncTask<Unit, Void, Void>() {
            override fun doInBackground(vararg params: Unit?): Void? {
                if (!db.getTeamUsers(teamID)) {
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
                return null
            }
        }.execute()
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





    fun getTeams(orgID: String, resp: (String) -> Unit, err: (Exception) -> Unit) {
        GithubApi.getTeams(orgID, resp, err)
    }

    fun getUser(url: String, resp: (String) -> Unit, err: (Exception) -> Unit){
        GithubApi.getUser(url, resp, err)
    }

    fun getLoggedUser(resp: (String) -> Unit, err: (Exception) -> Unit){
        GithubApi.getLoggedUser(resp, err)
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

}