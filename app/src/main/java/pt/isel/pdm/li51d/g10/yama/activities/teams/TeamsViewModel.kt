package pt.isel.pdm.li51d.g10.yama.activities.teams

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import pt.isel.pdm.li51d.g10.yama.R
import pt.isel.pdm.li51d.g10.yama.data.Preferences
import pt.isel.pdm.li51d.g10.yama.data.Repository
import pt.isel.pdm.li51d.g10.yama.data.dto.Team

class TeamsViewModel : ViewModel() {

    private val teamsLiveData = MutableLiveData<MutableList<Team>>()
    val teams: LiveData<MutableList<Team>> = teamsLiveData

    private val isRefreshedLiveData = MutableLiveData<Boolean>()
    val isRefreshed = isRefreshedLiveData

    init {
        teamsLiveData.value = mutableListOf()
        isRefreshedLiveData.value = false
    }

    //TODO: WIP refresh by user request
    fun refresh(success: (Unit) -> Unit, fail: (Exception) -> Unit) {
        isRefreshedLiveData.value = false
        loadTeams(success, fail)
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
                            isRefreshedLiveData.value = true
                        } catch (e: JSONException) {
                            e.printStackTrace() //TODO: do logging
                        }
                    }
                },
                err = { e -> fail.invoke(e) }
        )
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