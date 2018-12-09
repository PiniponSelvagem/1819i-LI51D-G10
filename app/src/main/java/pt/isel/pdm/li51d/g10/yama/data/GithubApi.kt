package pt.isel.pdm.li51d.g10.yama.data

import android.graphics.Bitmap
import android.util.Log
import pt.isel.pdm.li51d.g10.yama.R
import pt.isel.pdm.li51d.g10.yama.network.HttpRequests
import pt.isel.pdm.li51d.g10.yama.utils.createTokenHeader

object GithubApi {

    private val token = Preferences.get(R.string.spKey__login_token.toString())
    val headers = mapOf("Authorization" to createTokenHeader(token))

    private val TAG: String = GithubApi::class.java.simpleName

    private const val baseUrl = "https://api.github.com"

    fun getLoggedUser(resp: (String) -> Unit, err: (Exception) -> Unit) {
        Log.i(TAG, "Preparing request for Logged User")
        HttpRequests.getString("$baseUrl/user",
                headers,
                resp,
                err
        )
    }

    fun getUserTeams(resp: (String) -> Unit, err: (Exception) -> Unit) {
        Log.i(TAG, "Preparing request to get the teams from a User")
        HttpRequests.getString("$baseUrl/user/teams",
                headers,
                resp,
                err
        )
    }

    fun getTeams(orgID: String, resp: (String) -> Unit, err: (Exception) -> Unit) {
        Log.i(TAG, "Preparing request to get the teams from an organization")
        HttpRequests.getString("$baseUrl/orgs/$orgID/teams",
                headers,
                resp,
                err
        )
    }

    fun getTeamUsers(teamID: Int, resp: (String) -> Unit, err: (Exception) -> Unit) {
        Log.i(TAG, "Preparing request to get the members of a team")
        HttpRequests.getString("$baseUrl/teams/$teamID/members",
                headers,
                resp,
                err
        )
    }

    fun getUser(url: String, resp: (String) -> Unit, err: (Exception) -> Unit) {
        Log.i(TAG, "Preparing request to get a User information")
        HttpRequests.getString(
                url,
                headers,
                resp,
                err
        )
    }

    fun getAvatar(url: String, resp: (Bitmap) -> Unit, err: (Exception) -> Unit) {
        Log.i(TAG, "Preparing request to get the avatar from a User")
        HttpRequests.getBitmap(
                url,
                500, 500,
                headers,
                resp,
                err
        )
    }
}