package pt.isel.pdm.li51d.g10.yama.data

import android.graphics.Bitmap
import pt.isel.pdm.li51d.g10.yama.network.HttpRequests

object GithubApi {

    private const val baseUrl = "https://api.github.com"

    fun getUser(headers: MutableMap<String, String>,
                resp: (String) -> Unit, err: (Exception) -> Unit) {
        HttpRequests.getString("$baseUrl/user",
                headers,
                resp,
                err
        )
    }

    fun getUserTeams(headers: MutableMap<String, String>,
                resp: (String) -> Unit, err: (Exception) -> Unit) {
        HttpRequests.getString("$baseUrl/user/teams",
                headers,
                resp,
                err
        )
    }

    fun getTeams(headers: MutableMap<String, String>, orgID: String,
                 resp: (String) -> Unit, err: (Exception) -> Unit) {
        HttpRequests.getString("$baseUrl/orgs/$orgID/teams",
                headers,
                resp,
                err
        )
    }

    fun getTeamMembers(headers: MutableMap<String, String>, teamID: Int,
                 resp: (String) -> Unit, err: (Exception) -> Unit) {
        HttpRequests.getString("$baseUrl/teams/$teamID/members",
                headers,
                resp,
                err
        )
    }

    fun getTeamData(url: String, headers: MutableMap<String, String>,
                 resp: (String) -> Unit, err: (Exception) -> Unit) {
        HttpRequests.getString(url,
                headers,
                resp,
                err
        )
    }

    fun getUserImage(url: String, width: Int, height: Int, headers: Map<String, String> = mapOf(),
                     resp: (Bitmap) -> Unit, err: (Exception) -> Unit) {
        HttpRequests.getBitmap(url,
                width,
                height,
                headers,
                resp,
                err
        )
    }

}