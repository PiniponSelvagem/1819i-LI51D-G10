package pt.isel.pdm.i41n.g6.yama.activities.chat

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.view.*
import android.os.Bundle
import pt.isel.pdm.i41n.g6.yama.R
import pt.isel.pdm.i41n.g6.yama.data.dto.Team
import pt.isel.pdm.i41n.g6.yama.activities.teamdetails.TeamDetailsActivity

class ChatActivity : AppCompatActivity() {

    private lateinit var team: Team

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        team = intent.getSerializableExtra("team") as Team
        title = team.name
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
