package pt.isel.pdm.li51d.g10.yama.data.database

import android.os.AsyncTask
import androidx.room.Database
import androidx.room.RoomDatabase
import pt.isel.pdm.li51d.g10.yama.data.database.team.Team
import pt.isel.pdm.li51d.g10.yama.data.database.team.TeamDao
import pt.isel.pdm.li51d.g10.yama.data.database.team.teamusers.TeamUser
import pt.isel.pdm.li51d.g10.yama.data.database.team.teamusers.TeamUserDao
import pt.isel.pdm.li51d.g10.yama.data.database.user.User
import pt.isel.pdm.li51d.g10.yama.data.database.user.UserDao


@Database(version = 1, entities = [TeamUser::class, Team::class, User::class])
abstract class YamaRoomDatabase : RoomDatabase() {
    abstract fun teamUserDao():        TeamUserDao
    abstract fun teamDao():            TeamDao
    abstract fun userDao():            UserDao
}


class PopulateDbAsync internal constructor(db: YamaRoomDatabase) : AsyncTask<Void, Void, Void>() {

    private val teamUserDao: TeamUserDao = db.teamUserDao()
    private val teamDao:     TeamDao     = db.teamDao()
    private val userDao:     UserDao     = db.userDao()

    override fun doInBackground(vararg params: Void): Void? {
        teamUserDao.deleteAll()
        teamDao.deleteAll()
        userDao.deleteAll()
        /*
        var word = Team("Hello")
        mDao.insert(word)
        word = Team("World")
        mDao.insert(word)
        */
        return null
    }
}