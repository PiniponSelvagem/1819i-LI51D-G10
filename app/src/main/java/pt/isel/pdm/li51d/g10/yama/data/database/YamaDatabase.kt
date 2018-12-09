package pt.isel.pdm.li51d.g10.yama.data.database

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import pt.isel.pdm.li51d.g10.yama.data.database.message.Message
import pt.isel.pdm.li51d.g10.yama.data.database.message.MessageDao
import pt.isel.pdm.li51d.g10.yama.data.database.team.Team
import pt.isel.pdm.li51d.g10.yama.data.database.team.TeamDao
import pt.isel.pdm.li51d.g10.yama.data.database.team.teamusers.TeamUser
import pt.isel.pdm.li51d.g10.yama.data.database.team.teamusers.TeamUserDao
import pt.isel.pdm.li51d.g10.yama.data.database.user.User
import pt.isel.pdm.li51d.g10.yama.data.database.user.UserDao

class YamaDatabase(private val teamUserDao: TeamUserDao,
                   private val teamDao: TeamDao,
                   private val userDao: UserDao,
                   private val messageDao: MessageDao) {

    private val TAG: String = YamaDatabase::class.java.simpleName

    var loggedUser:   MutableLiveData<User>                 = MutableLiveData()
    val allTeams:     LiveData<MutableList<Team>>           = teamDao.getAllTeams()
    val teamUsers:    MutableLiveData<MutableList<User>>    = MutableLiveData()
    val teamMessages: MutableLiveData<MutableList<Message>> = MutableLiveData()

    fun insert(team: Team) {
        object : DaoAsyncProcessor<Unit>(null) {
            override fun doAsync() {
                Log.d(TAG, "Adding team with id ${team.id}")
                teamDao.insert(team)
            }
        }.start()
    }

    fun insert(user: User) {
        object : DaoAsyncProcessor<Unit>(null) {
            override fun doAsync() {
                Log.d(TAG, "Adding user with id ${user.id}")
                userDao.insert(user)
            }
        }.start()
    }

    fun insertLoggedUser(user: User) {
        object : DaoAsyncProcessor<Unit>(null) {
            override fun doAsync() {
                Log.d(TAG, "Adding logged user with id ${user.id}")
                userDao.insert(user)
                loggedUser.postValue(user)
            }
        }.start()
    }

    fun insertTeamUsers(user: User, teamUser: TeamUser) {
        object : DaoAsyncProcessor<Unit>(null) {
            override fun doAsync() {
                Log.d(TAG, "Adding to team with id ${teamUser.teamId} user with id ${user.id}")
                userDao.insert(user)
                teamUserDao.insert(teamUser)
                teamUsers.postValue(teamUserDao.getUsersForTeam(teamUser.teamId))
            }
        }.start()
    }

    fun getTeamUsers(teamId: Int) : Boolean {
        Log.d(TAG, "Getting users for team with id $teamId")
        val teamUsersList = teamUserDao.getUsersForTeam(teamId)
        if (teamUsersList.isEmpty())
            return false
        teamUsers.postValue(teamUsersList)
        return true
    }

    fun isLoggedUserPresent(userID: Int) : Boolean {
        Log.d(TAG, "Checking if logged user with id $userID is present")
        val user = userDao.getUser(userID)
        if (user.id != userID)
            return false
        loggedUser.postValue(user)
        return true
    }

    fun isAllTeamsFilled() : Boolean {
        Log.d(TAG, "Checking if teams table has data")
        val teamsList = teamDao.getAllTeamsList()
        if (teamsList.isEmpty())
            return false
        return true
    }

    fun deleteAllData() {
        object : DaoAsyncProcessor<Unit>(null) {
            override fun doAsync() {
                Log.d(TAG, "Deleting all data")
                teamUserDao.deleteAll()
                teamDao.deleteAll()
                userDao.deleteAll()
                messageDao.deleteAll()
                Log.d(TAG, "All data has been deleted")
            }
        }.start()
    }

    fun insert(message: Message) {
        object : DaoAsyncProcessor<Unit>(null) {
            override fun doAsync() {
                Log.d(TAG, "Adding new message with timestamp ${message.timestamp}")
                messageDao.insert(message)
            }
        }.start()
    }

    fun getMessagesForTeam(teamID: Int) {
        object : DaoAsyncProcessor<Unit>(null) {
            override fun doAsync() {
                Log.d(TAG, "Getting messages from team with id $teamID")
                teamMessages.postValue(messageDao.getTeamMessages(teamID))
            }
        }.start()
    }
}