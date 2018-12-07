package pt.isel.pdm.li51d.g10.yama.activities.teamdetails

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.json.JSONObject
import pt.isel.pdm.li51d.g10.yama.data.Repository
import pt.isel.pdm.li51d.g10.yama.data.convertJsonToUser
import pt.isel.pdm.li51d.g10.yama.data.database.user.User

class TeamDetailsViewModel(private val repository: Repository) : ViewModel() {

    val teamUsers: MutableLiveData<MutableList<User>> = repository.teamUsers.also {
        if (repository.teamUsers.value != null) repository.teamUsers.value!!.clear()
    }

    fun loadTeamMembers(teamID: Int, success: (Unit) -> Unit, fail: (Exception) -> Unit) {
        repository.getTeamUsers(teamID, success, fail)
    }


    private fun _getUserData(response: String, width: Int, height: Int,
                            success: (Unit) -> Unit, fail: (Exception) -> Unit): User {
        val jObj = JSONObject(response)
        val user = convertJsonToUser(jObj)

        /*
        Repository.getUserImage(jObj.getString("avatar_url"), width, height,
                resp = { bitmap ->
                    run {
                        user.avatar = bitmap
                        success.invoke(Unit)
                    }
                },
                err = { e -> fail.invoke(e) }
        )
        */

        return user
    }
}