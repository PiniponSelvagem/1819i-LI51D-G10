package pt.isel.pdm.li51d.g10.yama.activities.chat

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.view.*
import android.os.Bundle
import android.view.View.*
import kotlinx.android.synthetic.main.activity_chat.*
import pt.isel.pdm.li51d.g10.yama.R
import pt.isel.pdm.li51d.g10.yama.data.dto.Team
import pt.isel.pdm.li51d.g10.yama.activities.teamdetails.TeamDetailsActivity
import pt.isel.pdm.li51d.g10.yama.utils.hideKeyboard

class ChatActivity : AppCompatActivity() {

    private lateinit var team: Team

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        team = intent.getSerializableExtra("team") as Team
        title = team.name

        chat_root.onFocusChangeListener = OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                hideKeyboard(this, this.currentFocus)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the main_menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_chat, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.details -> {
                showTeamDetails()
                return true
            }
        }
        return false
    }

    private fun showTeamDetails() {
        val i = Intent(this, TeamDetailsActivity::class.java)
        i.putExtra("team", team)
        startActivity(i)
    }
}
