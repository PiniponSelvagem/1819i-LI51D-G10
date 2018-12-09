package pt.isel.pdm.li51d.g10.yama.activities.login

import android.widget.EditText
import androidx.lifecycle.ViewModel
import pt.isel.pdm.li51d.g10.yama.R
import pt.isel.pdm.li51d.g10.yama.data.Repository

class LoginViewModel(private val repository: Repository) : ViewModel() {

    fun loginUser(success: (Unit) -> Unit, fail: (Exception) -> Unit) {
        repository.loginUser(success, fail)
    }

    /*
        Returns false if logged_id is in shared preferences, user had
        previously successful login.
     */
    fun shouldAcceptNewCredentials() : Boolean {
        return (repository.getSharedPreference(R.string.spKey__logged_id.toString()) == "")
    }

    fun checkCredentials(orgID: EditText, token: EditText) {
        orgID.setText(repository.getSharedPreference(R.string.spKey__login_orgID.toString()))
        token.setText(repository.getSharedPreference(R.string.spKey__login_token.toString()))
    }

    fun saveCredentials(orgID: String, token: String) {
        repository.setSharedPreference(R.string.spKey__login_orgID.toString(), orgID)
        repository.setSharedPreference(R.string.spKey__login_token.toString(), token)
    }
}