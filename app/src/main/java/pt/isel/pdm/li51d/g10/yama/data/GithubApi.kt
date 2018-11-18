package pt.isel.pdm.li51d.g10.yama.data

import pt.isel.pdm.li51d.g10.yama.network.HttpRequests

object GithubApi {

    fun getUser(headers: MutableMap<String, String>,
                resp: (String) -> Unit, err: (Exception) -> Unit) {
        HttpRequests.getString("https://api.github.com/user",
                headers,
                resp,
                err
        )
    }

    fun getTeams(headers: MutableMap<String, String>, orgID: String,
                 resp: (String) -> Unit, err: (Exception) -> Unit) {
        HttpRequests.getString("https://api.github.com/orgs/$orgID/teams",
                headers,
                resp,
                err
        )
    }

    fun getTeamMembers(headers: MutableMap<String, String>, teamID: Int,
                 resp: (String) -> Unit, err: (Exception) -> Unit) {
        HttpRequests.getString("https://api.github.com/teams/$teamID/members",
                headers,
                resp,
                err
        )
    }
}