package pt.isel.pdm.li51d.g10.yama.activities.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.OnFocusChangeListener
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_login.*
import pt.isel.pdm.li51d.g10.yama.R
import pt.isel.pdm.li51d.g10.yama.activities.teams.TeamsActivity
import pt.isel.pdm.li51d.g10.yama.utils.hideKeyboard
import pt.isel.pdm.li51d.g10.yama.utils.showHttpErrorToast
import pt.isel.pdm.li51d.g10.yama.utils.viewModel

class LoginActivity : AppCompatActivity() {

    private lateinit var viewModel: LoginViewModel

    private var isConnecting: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        viewModel = this.viewModel()
        viewModel.checkCredentials(login_orgID, login_token)

        if(savedInstanceState != null)
            isConnecting = savedInstanceState.getBoolean("is_connecting")

        if (isConnecting)
            disableInteraction()

        login_button.setOnClickListener {
            viewModel.saveCredentials(login_orgID.text.toString(), login_token.text.toString())

            disableInteraction()
            viewModel.loginUser(
                    success = {
                        startActivity(Intent(this, TeamsActivity::class.java))
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
