package pt.isel.pdm.li51d.g10.yama.data

import android.graphics.Bitmap
import pt.isel.pdm.li51d.g10.yama.network.HttpRequests

object Repository {

    fun getTeams(headers: MutableMap<String, String>, orgID: String,
                 resp: (String) -> Unit, err: (Exception) -> Unit) {
        GithubApi.getTeams(headers, orgID, resp, err)
    }

    fun getTeamMembers(headers: MutableMap<String, String>, teamID: Int,
                 resp: (String) -> Unit, err: (Exception) -> Unit) {
        GithubApi.getTeamMembers(headers, teamID, resp, err)
    }

    fun getTeamData(url: String, headers: MutableMap<String, String>,
                     resp: (String) -> Unit, err: (Exception) -> Unit){
        HttpRequests.getString(url, headers, resp, err)
    }

    fun getUser(headers: MutableMap<String, String>,
                resp: (String) -> Unit, err: (Exception) -> Unit){
        GithubApi.getUser(headers, resp, err)
    }

    fun getUserImage(url: String, width: Int, height: Int, headers: Map<String, String> = mapOf(),
                     resp: (Bitmap) -> Unit, err: (Exception) -> Unit){
        HttpRequests.getBitmap(url,
                width, height,
                headers, resp, err)
    }

    fun getUserTeams(headers: MutableMap<String, String>,
                resp: (String) -> Unit, err: (Exception) -> Unit){
        GithubApi.getUserTeams(headers, resp, err)
    }

}