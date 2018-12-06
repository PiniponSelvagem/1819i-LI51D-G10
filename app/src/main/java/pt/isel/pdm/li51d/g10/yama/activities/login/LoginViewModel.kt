package pt.isel.pdm.li51d.g10.yama.activities.login

import androidx.lifecycle.ViewModel
import pt.isel.pdm.li51d.g10.yama.data.Repository

class LoginViewModel(private val repository: Repository) : ViewModel() {

    fun loginUser(success: (Unit) -> Unit, fail: (Exception) -> Unit) {
        repository.loginUser(success, fail)
    }
}