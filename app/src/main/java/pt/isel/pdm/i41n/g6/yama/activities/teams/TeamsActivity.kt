package pt.isel.pdm.i41n.g6.yama.activities.teams

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_teams.*
import pt.isel.pdm.i41n.g6.yama.R
import pt.isel.pdm.i41n.g6.yama.data.dto.Team
import pt.isel.pdm.i41n.g6.yama.data.dto.User
import pt.isel.pdm.i41n.g6.yama.activities.teams.adapters.TeamsAdapter
import pt.isel.pdm.i41n.g6.yama.utils.showHttpErrorToast

class TeamsActivity : AppCompatActivity() {

    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var layoutMgr: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teams)


        //TODO: this loggerUser should go to the activity that will display user info
        val loggedUser = intent.getSerializableExtra("loggedUser") as User


        val viewModel = ViewModelProviders.of(this).get(TeamsViewModel::class.java)
        viewModel.teams.observe(this, Observer<MutableList<Team>> {})

        viewModel.loadTeams(
                success = {
                    viewAdapter.notifyDataSetChanged()
                    Toast.makeText(this, "TEAMS UPDATED", Toast.LENGTH_LONG).show() //TODO: just temporary
                },
                fail = { e -> showHttpErrorToast(this, e) }
        )

        layoutMgr = LinearLayoutManager(this)
        viewAdapter = TeamsAdapter(viewModel.teams.value!!)

        teams_recycler_view.apply {
            setHasFixedSize(true)
            layoutManager = layoutMgr
            adapter = viewAdapter
        }
    }
}
