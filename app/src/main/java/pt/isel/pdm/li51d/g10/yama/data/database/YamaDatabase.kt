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
                Log.i(TAG, "Adding new team")
                teamDao.insert(team)
            }
        }.start()
    }

    fun insert(user: User) {
        object : DaoAsyncProcessor<Unit>(null) {
            override fun doAsync() {
                Log.i(TAG, "Adding new user")
                userDao.insert(user)
            }
        }.start()
    }

    fun insertLoggedUser(user: User) {
        object : DaoAsyncProcessor<Unit>(null) {
            override fun doAsync() {
                Log.i(TAG, "Adding user as the logged-in user")
                userDao.insert(user)
                loggedUser.postValue(user)
            }
        }.start()
    }

    fun insertTeamUsers(user: User, teamUser: TeamUser) {
        object : DaoAsyncProcessor<Unit>(null) {
            override fun doAsync() {
                Log.i(TAG, "Adding team users")
                userDao.insert(user)
                teamUserDao.insert(teamUser)
                teamUsers.postValue(teamUserDao.getUsersForTeam(teamUser.teamId))
            }
        }.start()
    }

    fun getTeamUsers(teamId: Int) : Boolean {
        Log.i(TAG, "Getting team users")
        val teamUsersList = teamUserDao.getUsersForTeam(teamId)
        if (teamUsersList.isEmpty())
            return false
        teamUsers.postValue(teamUsersList)
        return true
    }


    fun isLoggedUserPresent(userID: Int) : Boolean {
        val user = userDao.getUser(userID)
        if (user.id != userID)
            return false
        loggedUser.postValue(user)
        return true
    }

    fun isAllTeamsFilled() : Boolean {
        val teamsList = teamDao.getAllTeamsList()
        if (teamsList.isEmpty())
            return false
        return true
    }

    fun deleteAllData() {
        object : DaoAsyncProcessor<Unit>(null) {
            override fun doAsync() {
                Log.i(TAG, "Deleting all data")
                teamUserDao.deleteAll()
                teamDao.deleteAll()
                userDao.deleteAll()
                Log.i(TAG, "All the data has been deleted")
            }
        }.start()
    }


    fun insert(message: Message) {
        object : DaoAsyncProcessor<Unit>(null) {
            override fun doAsync() {
                Log.i(TAG, "Adding new message")
                messageDao.insert(message)
            }
        }.start()
    }

    fun getMessagesForTeam(teamId: Int) {
        object : DaoAsyncProcessor<Unit>(null) {
            override fun doAsync() {
                Log.i(TAG, "Getting the messages from a specific team")
                teamMessages.postValue(messageDao.getTeamMessages(teamId))
            }
        }.start()
    }
}