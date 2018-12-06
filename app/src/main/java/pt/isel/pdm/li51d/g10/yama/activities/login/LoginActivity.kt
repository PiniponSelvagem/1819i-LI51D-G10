package pt.isel.pdm.li51d.g10.yama.activities.login

import android.arch.lifecycle.*
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import kotlinx.android.synthetic.main.activity_login.*
import pt.isel.pdm.li51d.g10.yama.R
import pt.isel.pdm.li51d.g10.yama.activities.teams.TeamsActivity
import android.view.View.OnFocusChangeListener
import pt.isel.pdm.li51d.g10.yama.utils.hideKeyboard
import pt.isel.pdm.li51d.g10.yama.utils.showHttpErrorToast


class LoginActivity : AppCompatActivity() {

    private var isConnecting: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        //viewModel.userLogged.observe(this, Observer<User> {})

        viewModel.checkSharedPreferences(login_orgID as EditText, login_token as EditText)

        if(savedInstanceState != null)
            isConnecting = savedInstanceState.getBoolean("is_connecting")

        if (isConnecting)
            disableInteraction()

        login_button.setOnClickListener {
            viewModel.saveCredentials(login_orgID as EditText, login_token as EditText)

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

        login_root.onFocusChangeListener = OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                hideKeyboard(this, this.currentFocus)
            }
        }
    }

    @Override
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean("is_connecting", isConnecting)
        super.onSaveInstanceState(outState)
    }

    private fun disableInteraction() {
        isConnecting = true
        login_root.requestFocus()
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
}
