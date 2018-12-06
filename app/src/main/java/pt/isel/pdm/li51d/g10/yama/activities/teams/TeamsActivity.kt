package pt.isel.pdm.li51d.g10.yama.activities.teams

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
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
import kotlinx.android.synthetic.main.drawer_header_teams.view.*
import pt.isel.pdm.li51d.g10.yama.activities.chat.ChatActivity
import pt.isel.pdm.li51d.g10.yama.activities.login.LoginActivity
import pt.isel.pdm.li51d.g10.yama.data.dto.Team
import pt.isel.pdm.li51d.g10.yama.utils.showDialogYesNo


class TeamsActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var layoutMgr: RecyclerView.LayoutManager

    private lateinit var drawer: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle

    private lateinit var viewModel: TeamsViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teams)

        val toolbar: Toolbar = findViewById(R.id.toolbar_main)
        setSupportActionBar(toolbar)
        drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        toggle = ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)

        val navigationView = findViewById<View>(R.id.nav_view) as NavigationView
        navigationView.setNavigationItemSelectedListener(this)
        val headerView = navigationView.getHeaderView(0)

        viewModel = ViewModelProviders.of(this).get(TeamsViewModel::class.java)
        viewModel.setLoggedUser(intent.getSerializableExtra("loggedUser") as User)
        //viewModel.teams.observe(this, Observer<MutableList<Team>> {})

        drawerLoggedUser(headerView, viewModel.loggedUser.value!!)

        if (viewModel.isLoggedUserRefreshed.value == false) {
            viewModel.loadLoggedUserAvatar(viewModel.loggedUser.value!!.avatarUrl,
                    success = { bitmap ->
                        viewModel.loggedUser.value?.avatar = bitmap
                        run {
                            headerView.user_avatar_drawer.setImageBitmap(viewModel.loggedUser.value?.avatar)
                        }
                    },
                    fail = { e -> showHttpErrorToast(this, e) }
            )

            viewModel.loadLoggedUserTeams(
                    success = { drawerMyTeams(navigationView) },
                    fail    = { e -> showHttpErrorToast(this, e) }
            )
        }
        else {
            drawerMyTeams(navigationView) //populate drawer with previously loaded data
        }

        if (viewModel.isTeamsRefreshed.value == false) {
            viewModel.loadTeams(
                    success = { viewAdapter.notifyDataSetChanged() },
                    fail    = { e -> showHttpErrorToast(this, e) }
            )
        }

        layoutMgr   = LinearLayoutManager(this)
        viewAdapter = TeamsAdapter(viewModel.teams.value!!)

        teams_recycler_view.apply {
            setHasFixedSize(true)
            layoutManager = layoutMgr
            adapter = viewAdapter
        }
        teams_recycler_view.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
    }


    private fun drawerLoggedUser(headerView: View, loggedUser: User) {
        headerView.user_avatar_drawer.setImageBitmap(loggedUser.avatar)
        headerView.user_followers_drawer.text = loggedUser.followers.toString()
        headerView.user_following_drawer.text = loggedUser.following.toString()
        headerView.user_nickname_drawer.text  = loggedUser.nickname
        headerView.user_name_drawer.text      = loggedUser.name
        if (loggedUser.email != "null") headerView.user_email_drawer.text = loggedUser.email
        else headerView.user_email_drawer.visibility = View.GONE
    }

    private fun drawerMyTeams(navigationView: NavigationView) {
        val myTeamsList = viewModel.loggedUserTeams.value as MutableList<Team>
        val myTeamsGroup = navigationView.menu.findItem(R.id.drawer_group_my_teams)

        if (!myTeamsList.isEmpty()) {
            val subMenu = myTeamsGroup.subMenu
            for (i in 0 until myTeamsList.size) {
                subMenu.add(0, i, 0, myTeamsList[i].name).setIcon(R.drawable.ic_group_white_24dp)
            }
        } else {
            myTeamsGroup.isVisible = false
        }
    }


    // --- Navigation Drawer related START --- //
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
            R.id.drawer_item_logout -> {
                drawer.closeDrawer(GravityCompat.START)
                showDialogYesNo(this, R.string.log_out, R.string.log_out_dialog_msg,
                        yes = {
                            viewModel.removeUserCredential(R.string.spKey__login_token.toString())
                            viewModel.removeUserCredential(R.string.spKey__login_orgID.toString())
                            startActivity(Intent(this, LoginActivity::class.java))
                            finish()
                        },
                        no = {}
                )
            }
            else -> {
                val i = Intent(this, ChatActivity::class.java)
                i.putExtra("team", viewModel.loggedUserTeams.value!![item.itemId])
                startActivity(i)
            }
        }
        return true
    }
    // --- Navigation Drawer related END --- //

    override fun onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}
