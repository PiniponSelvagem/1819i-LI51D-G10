package pt.isel.pdm.li51d.g10.yama.data.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import pt.isel.pdm.li51d.g10.yama.data.database.team.Team
import pt.isel.pdm.li51d.g10.yama.data.database.team.TeamDao
import pt.isel.pdm.li51d.g10.yama.data.database.team.teamusers.TeamUser
import pt.isel.pdm.li51d.g10.yama.data.database.team.teamusers.TeamUserDao
import pt.isel.pdm.li51d.g10.yama.data.database.user.User
import pt.isel.pdm.li51d.g10.yama.data.database.user.UserDao

class YamaDatabase(private val teamUserDao: TeamUserDao,
                   private val teamDao: TeamDao,
                   private val userDao: UserDao) {
    var loggedUser: MutableLiveData<User>       = MutableLiveData()
    val allTeams:   LiveData<MutableList<Team>> = teamDao.getAllTeams()
    val allUsers:   LiveData<MutableList<User>> = userDao.getAllUsers()
    val teamUsers:  MutableLiveData<MutableList<User>> = MutableLiveData()

    fun insert(team: Team) {
        object : DaoAsyncProcessor<Unit>(null) {
            override fun doAsync() {
                teamDao.insert(team)
            }
        }.start()
    }

    fun insert(user: User) {
        object : DaoAsyncProcessor<Unit>(null) {
            override fun doAsync() {
                userDao.insert(user)
            }
        }.start()
    }

    fun insertLoggedUser(user: User) {
        object : DaoAsyncProcessor<Unit>(null) {
            override fun doAsync() {
                userDao.insert(user)
                loggedUser.postValue(userDao.getUser(user.id))
            }
        }.start()
    }

    fun insertTeamUsers(user: User, teamUser: TeamUser) {
        object : DaoAsyncProcessor<Unit>(null) {
            override fun doAsync() {
                userDao.insert(user)
                teamUserDao.insert(teamUser)
                teamUsers.postValue(teamUserDao.getUsersForTeam(teamUser.teamId))
            }
        }.start()
    }

    fun getTeamUsers(teamId: Int) : Boolean {
        val teamUsersList = teamUserDao.getUsersForTeam(teamId)
        if (teamUsersList.isEmpty())
            return false
        teamUsers.postValue(teamUsersList)
        return true
    }
}