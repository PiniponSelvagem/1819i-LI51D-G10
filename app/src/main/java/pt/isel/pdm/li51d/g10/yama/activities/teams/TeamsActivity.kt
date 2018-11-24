package pt.isel.pdm.li51d.g10.yama.activities.teams

import android.app.ActionBar
import android.app.Activity
import android.app.Fragment
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_teams.*
import pt.isel.pdm.li51d.g10.yama.R
import pt.isel.pdm.li51d.g10.yama.data.dto.User
import pt.isel.pdm.li51d.g10.yama.utils.showHttpErrorToast



class TeamsActivity : AppCompatActivity() {

    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var layoutMgr: RecyclerView.LayoutManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teams)

        //TODO: this loggerUser should go to the activity that will display user info
        val loggedUser = intent.getSerializableExtra("loggedUser") as User


        val viewModel = ViewModelProviders.of(this).get(TeamsViewModel::class.java)
        //viewModel.teams.observe(this, Observer<MutableList<Team>> {})

        if (viewModel.isRefreshed.value == false) {
            viewModel.loadTeams(
                    success = {
                        viewAdapter.notifyDataSetChanged()
                    },
                    fail = { e -> showHttpErrorToast(this, e) }
            )
        }

        layoutMgr = LinearLayoutManager(this)
        viewAdapter = TeamsAdapter(viewModel.teams.value!!)

        teams_recycler_view.apply {
            setHasFixedSize(true)
            layoutManager = layoutMgr
            adapter = viewAdapter
        }
        teams_recycler_view.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
    }
}
