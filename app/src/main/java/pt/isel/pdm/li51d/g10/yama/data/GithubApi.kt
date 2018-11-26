package pt.isel.pdm.li51d.g10.yama.data

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
}