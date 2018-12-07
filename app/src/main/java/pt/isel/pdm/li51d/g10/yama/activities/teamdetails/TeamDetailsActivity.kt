package pt.isel.pdm.li51d.g10.yama.activities.teamdetails

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_details.*
import pt.isel.pdm.li51d.g10.yama.R
import pt.isel.pdm.li51d.g10.yama.data.database.team.Team
import pt.isel.pdm.li51d.g10.yama.data.database.user.User
import pt.isel.pdm.li51d.g10.yama.utils.showHttpErrorToast
import pt.isel.pdm.li51d.g10.yama.utils.viewModel

class TeamDetailsActivity : AppCompatActivity() {

    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var layoutMgr: RecyclerView.LayoutManager

    private lateinit var viewModel: TeamDetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        val team = intent.getSerializableExtra("team") as Team
        title = team.name

        viewModel = this.viewModel()
        viewModel.teamUsers.observe(this, Observer<MutableList<User>> {
            viewAdapter.notifyDataSetChanged()
        })

        viewModel.loadTeamMembers(team.id,
                success = { },
                fail = { e -> showHttpErrorToast(this, e) }
        )

        layoutMgr = LinearLayoutManager(this)
        viewAdapter = TeamDetailsAdapter(this, viewModel.teamUsers)

        team_recycler_view.apply {
            setHasFixedSize(true)
            layoutManager = layoutMgr
            adapter = viewAdapter
        }
    }
}
