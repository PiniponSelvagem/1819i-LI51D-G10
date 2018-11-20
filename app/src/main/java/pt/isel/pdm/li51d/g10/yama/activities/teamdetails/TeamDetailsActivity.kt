package pt.isel.pdm.li51d.g10.yama.activities.teamdetails

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_details.*
import kotlinx.android.synthetic.main.recycler_view_user.*
import pt.isel.pdm.li51d.g10.yama.R
import pt.isel.pdm.li51d.g10.yama.activities.teamdetails.adapters.TeamAdapter
import pt.isel.pdm.li51d.g10.yama.data.dto.Team
import pt.isel.pdm.li51d.g10.yama.data.dto.User
import pt.isel.pdm.li51d.g10.yama.utils.showHttpErrorToast


class TeamDetailsActivity : AppCompatActivity() {

    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var layoutMgr: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        val team = intent.getSerializableExtra("team") as Team
        title = team.name


        val viewModel = ViewModelProviders.of(this).get(TeamDetailsViewModel::class.java)
        viewModel.teamUsers.observe(this, Observer<MutableList<User>> {})

        if (viewModel.isRefreshed.value == false) {
            //TODO: user_avatar.maxWidth, user_avatar.maxHeight,
            //hardcoded for now, since user_avatar is coming null
            viewModel.loadTeamMembers(team.id, 500, 500,
                    success = {
                        viewAdapter.notifyDataSetChanged()
                    },
                    fail = { e -> showHttpErrorToast(this, e) }
            )
        }

        layoutMgr = LinearLayoutManager(this)
        viewAdapter = TeamAdapter(viewModel.teamUsers.value!!)

        team_recycler_view.apply {
            setHasFixedSize(true)
            layoutManager = layoutMgr
            adapter = viewAdapter
        }
    }
}
