package pt.isel.pdm.i41n.g6.yama.activities.login

import android.arch.lifecycle.*
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_login.*
import pt.isel.pdm.i41n.g6.yama.R
import pt.isel.pdm.i41n.g6.yama.activities.teams.TeamsActivity
import pt.isel.pdm.i41n.g6.yama.data.Preferences
import pt.isel.pdm.i41n.g6.yama.data.dto.User
import pt.isel.pdm.i41n.g6.yama.utils.showHttpErrorToast

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        checkSharedPreferences()

        val viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        viewModel.userLogged.observe(this, Observer<User> {})

        login_button.setOnClickListener { _ ->
            saveCredentials()

            disableInteraction()
            viewModel.loginUser(
                    success = {
                        val i = Intent(this, TeamsActivity::class.java)
                        i.putExtra("loggedUser", viewModel.userLogged.value)
                        startActivity(i)
                        finish()
                    },
                    fail = { e ->
                        run {
                            enableInteraction()
                            showHttpErrorToast(this, e)
                        }
                    }
            )
        }
    }

    private fun disableInteraction() {
        login_token.isEnabled = false
        login_orgID.isEnabled = false
        login_button.isEnabled = false
    }

    private fun enableInteraction() {
        login_token.isEnabled = true
        login_orgID.isEnabled = true
        login_button.isEnabled = true
    }



    private fun checkSharedPreferences() {
        login_orgID.setText(Preferences.get(R.string.spKey__login_orgID.toString()))
        login_token.setText(Preferences.get(R.string.spKey__login_token.toString()))
    }

    private fun saveCredentials() {
        Preferences.set(R.string.spKey__login_orgID.toString(), login_orgID.text.toString())
        Preferences.set(R.string.spKey__login_token.toString(), login_token.text.toString())
    }
}
