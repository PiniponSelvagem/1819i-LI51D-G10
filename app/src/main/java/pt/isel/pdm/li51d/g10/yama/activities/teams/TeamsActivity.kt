package pt.isel.pdm.li51d.g10.yama.activities.teams

import android.arch.lifecycle.ViewModelProviders
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.support.design.widget.NavigationView
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_teams.*
import pt.isel.pdm.li51d.g10.yama.R
import pt.isel.pdm.li51d.g10.yama.data.dto.User
import pt.isel.pdm.li51d.g10.yama.utils.showHttpErrorToast
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.Toolbar
import android.widget.Toast
import kotlinx.android.synthetic.main.drawer_header_teams.view.*


class TeamsActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var layoutMgr: RecyclerView.LayoutManager

    private lateinit var drawer: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle

    private lateinit var loggedUser: User


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teams)

        val toolbar: Toolbar = findViewById(R.id.toolbar_main)
        setSupportActionBar(toolbar)
        drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        toggle = ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.setDrawerListener(toggle)
        toggle.syncState()

        val navigationView = findViewById<View>(R.id.nav_view) as NavigationView
        navigationView.setNavigationItemSelectedListener(this)


        //TODO: this loggerUser should go to the activity that will display user info
        loggedUser = intent.getSerializableExtra("loggedUser") as User

        val headerView = navigationView.getHeaderView(0)
        headerView.user_avatar_drawer.setImageBitmap(loggedUser.avatar)
        headerView.user_nickname_drawer.text = loggedUser.nickname
        headerView.user_name_drawer.text = loggedUser.name


        val viewModel = ViewModelProviders.of(this).get(TeamsViewModel::class.java)
        //viewModel.teams.observe(this, Observer<MutableList<Team>> {})

        viewModel.loadLoggedUserAvatar(loggedUser.avatarUrl,
                success = {
                    bitmap -> loggedUser.avatar = bitmap
                        run {
                            headerView.user_avatar_drawer.setImageBitmap(loggedUser.avatar)
                        }
                },
                fail = { e -> showHttpErrorToast(this, e) }
        )

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

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        toggle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        toggle.onConfigurationChanged(newConfig)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item1 -> Toast.makeText(this, "Clicked item camera", Toast.LENGTH_SHORT).show()
            R.id.item2 -> Toast.makeText(this,   "Clicked item share", Toast.LENGTH_SHORT).show()
            R.id.item3 -> Toast.makeText(this, "Clicked item send", Toast.LENGTH_SHORT).show()
        }
        drawer.closeDrawer(GravityCompat.START)
        return false
    }

    override fun onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}
