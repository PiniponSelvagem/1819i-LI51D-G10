package pt.isel.pdm.li51d.g10.yama.data.database

import android.os.AsyncTask
import android.util.Log
import androidx.room.Database
import androidx.room.RoomDatabase
import pt.isel.pdm.li51d.g10.yama.R
import pt.isel.pdm.li51d.g10.yama.data.Preferences
import pt.isel.pdm.li51d.g10.yama.data.database.message.Message
import pt.isel.pdm.li51d.g10.yama.data.database.message.MessageDao
import pt.isel.pdm.li51d.g10.yama.data.database.team.Team
import pt.isel.pdm.li51d.g10.yama.data.database.team.TeamDao
import pt.isel.pdm.li51d.g10.yama.data.database.team.teamusers.TeamUser
import pt.isel.pdm.li51d.g10.yama.data.database.team.teamusers.TeamUserDao
import pt.isel.pdm.li51d.g10.yama.data.database.user.User
import pt.isel.pdm.li51d.g10.yama.data.database.user.UserDao


@Database(version = 1, entities = [TeamUser::class, Team::class, User::class, Message::class])
abstract class YamaRoomDatabase : RoomDatabase() {
    abstract fun teamUserDao():        TeamUserDao
    abstract fun teamDao():            TeamDao
    abstract fun userDao():            UserDao
    abstract fun messageDao():         MessageDao
}


class PopulateDbAsync internal constructor(db: YamaRoomDatabase) : AsyncTask<Void, Void, Void>() {

    private val TAG: String = PopulateDbAsync::class.java.simpleName

    private val teamUserDao: TeamUserDao = db.teamUserDao()
    private val teamDao:     TeamDao     = db.teamDao()
    private val userDao:     UserDao     = db.userDao()
    private val messageDao:  MessageDao  = db.messageDao()

    override fun doInBackground(vararg params: Void): Void? {
        if (Preferences.get(R.string.spKey__logged_id.toString()) == "") {
            Log.i(TAG, "Deleting all data")
            teamUserDao.deleteAll()
            teamDao.deleteAll()
            userDao.deleteAll()
            messageDao.deleteAll()
            Log.i(TAG, "All data has been deleted")
        }

        /*
        var word = Team("Hello")
        mDao.insert(word)
        word = Team("World")
        mDao.insert(word)
        */
        return null
    }
}