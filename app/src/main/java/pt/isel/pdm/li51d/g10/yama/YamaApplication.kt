package pt.isel.pdm.li51d.g10.yama

import android.app.Application
import android.util.Log
import androidx.room.Room
import pt.isel.pdm.li51d.g10.yama.data.Preferences
import pt.isel.pdm.li51d.g10.yama.data.Repository
import pt.isel.pdm.li51d.g10.yama.data.database.PopulateDbAsync
import pt.isel.pdm.li51d.g10.yama.data.database.YamaDatabase
import pt.isel.pdm.li51d.g10.yama.data.database.YamaRoomDatabase
import pt.isel.pdm.li51d.g10.yama.network.HttpRequests

class YamaApplication: Application() {

    private val TAG: String = YamaApplication::class.java.simpleName

    lateinit var repository: Repository
        private set

    override fun onCreate() {
        super.onCreate()
        Log.i(TAG, "Starting YAMA application")

        HttpRequests.init(this)
        Preferences.init(this)

        val yamaRoomDatabase =
                Room.databaseBuilder(this, YamaRoomDatabase::class.java, "Yama_database")
                        .build()

        PopulateDbAsync(yamaRoomDatabase).execute()

        repository = Repository(
                YamaDatabase(
                        yamaRoomDatabase.teamUserDao(),
                        yamaRoomDatabase.teamDao(),
                        yamaRoomDatabase.userDao(),
                        yamaRoomDatabase.messageDao()
                )
        )

        Log.i(TAG, "YAMA application is not fully loaded, have fun :D")
    }
}