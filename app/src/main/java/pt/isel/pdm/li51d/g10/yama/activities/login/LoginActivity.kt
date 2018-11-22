package pt.isel.pdm.li51d.g10.yama.activities.login

import android.arch.lifecycle.*
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_login.*
import pt.isel.pdm.li51d.g10.yama.R
import pt.isel.pdm.li51d.g10.yama.activities.teams.TeamsActivity
import pt.isel.pdm.li51d.g10.yama.data.Preferences
import pt.isel.pdm.li51d.g10.yama.data.dto.User
import pt.isel.pdm.li51d.g10.yama.utils.showHttpErrorToast

class LoginActivity : AppCompatActivity() {

    private var isConnecting: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        checkSharedPreferences()

        val viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        viewModel.userLogged.observe(this, Observer<User> {})

        if(savedInstanceState != null)
            isConnecting = savedInstanceState.getBoolean("is_connecting")

        if (isConnecting)
            disableInteraction()

        login_button.setOnClickListener {
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

    @Override
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean("is_connecting", isConnecting)
        super.onSaveInstanceState(outState)
    }

    private fun disableInteraction() {
        isConnecting = true
        login_token.isEnabled = false
        login_orgID.isEnabled = false
        login_button.visibility = View.INVISIBLE
        login_progressBar.visibility = View.VISIBLE
    }

    private fun enableInteraction() {
        isConnecting = false
        login_token.isEnabled = true
        login_orgID.isEnabled = true
        login_button.visibility = View.VISIBLE
        login_progressBar.visibility = View.INVISIBLE
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
