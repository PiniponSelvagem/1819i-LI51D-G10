package pt.isel.pdm.i41n.g6.yama

import android.app.Application
import pt.isel.pdm.i41n.g6.yama.data.httprequests.Volley

class Application: Application() {
    override fun onCreate() {
        super.onCreate()
        Volley.init(this)
    }
}