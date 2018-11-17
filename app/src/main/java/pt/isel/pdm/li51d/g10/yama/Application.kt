package pt.isel.pdm.li51d.g10.yama

import android.app.Application
import pt.isel.pdm.li51d.g10.yama.data.Preferences
import pt.isel.pdm.li51d.g10.yama.network.HttpRequests

class Application: Application() {
    override fun onCreate() {
        super.onCreate()
        HttpRequests.init(this)
        Preferences.init(this)
    }
}