package pt.isel.pdm.li51d.g10.yama.activities.teamdetails

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import pt.isel.pdm.li51d.g10.yama.data.Repository
import pt.isel.pdm.li51d.g10.yama.data.database.user.User

class TeamDetailsViewModel(private val repository: Repository) : ViewModel() {

    val teamUsers: MutableLiveData<MutableList<User>> = repository.teamUsers.also {
        if (repository.teamUsers.value != null) repository.teamUsers.value!!.clear()
    }

    fun loadTeamUsers(teamID: Int, success: (Unit) -> Unit, fail: (Exception) -> Unit) {
        repository.getTeamUsers(teamID, success, fail)
    }
}