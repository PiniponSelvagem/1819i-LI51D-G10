package pt.isel.pdm.li51d.g10.yama.activities.teams

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_teams.*
import kotlinx.android.synthetic.main.drawer_header_teams.view.*
import pt.isel.pdm.li51d.g10.yama.R
import pt.isel.pdm.li51d.g10.yama.activities.login.LoginActivity
import pt.isel.pdm.li51d.g10.yama.data.Preferences
import pt.isel.pdm.li51d.g10.yama.data.database.team.Team
import pt.isel.pdm.li51d.g10.yama.data.database.user.User
import pt.isel.pdm.li51d.g10.yama.utils.showDialogYesNo
import pt.isel.pdm.li51d.g10.yama.utils.showHttpErrorToast
import pt.isel.pdm.li51d.g10.yama.utils.viewModel

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

        viewModel = this.viewModel()

        viewModel.loggedUser.observe(this, Observer<User> {
            user -> drawerLoggedUser(headerView, user)
        })

        //TODO: feature WIP (navigation drawer show teams the logged user is in)
        //NOTE: atm is being given an emptyList() so it knows that it needs to hide "My teams" section
        drawerMyTeams(navigationView, emptyList())

        /*
        //TODO: feature WIP (navigation drawer show teams the logged user is in)
        viewModel.loggedUserTeams.observe(this, Observer<List<Team>> {
            teams -> drawerMyTeams(navigationView, teams)
        })
        */

        /*TODO
        viewModel.loadLoggedUserAvatar(viewModel.loggedUser.value!!.avatarUrl,
                success = { bitmap ->
                    //TODO: viewModel.loggedUser.value?.avatar = bitmap
                    run {
                        //TODO: headerView.user_avatar_drawer.setImageBitmap(viewModel.loggedUser.value?.avatar)
                    }
                },
                fail = { e -> showHttpErrorToast(this, e) }
        )
        */

        /*
        //TODO: feature WIP (navigation drawer show teams the logged user is in)
        viewModel.loadLoggedUserTeams(
                success = { },   //TODO: not being used
                fail    = { e -> showHttpErrorToast(this, e) }
        )
        */

        viewModel.loadTeams(
                success = { },   //TODO: not being used
                fail    = { e -> showHttpErrorToast(this, e) }
        )

        layoutMgr   = LinearLayoutManager(this)
        viewAdapter = TeamsAdapter(this, viewModel.teams)

        teams_recycler_view.apply {
            setHasFixedSize(true)
            layoutManager = layoutMgr
            adapter = viewAdapter
        }
        teams_recycler_view.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
    }


    private fun drawerLoggedUser(headerView: View, loggedUser: User) {
        //TODO: headerView.user_avatar_drawer.setImageBitmap(loggedUser.avatar)
        headerView.user_followers_drawer.text = loggedUser.followers.toString()
        headerView.user_following_drawer.text = loggedUser.following.toString()
        headerView.user_nickname_drawer.text  = loggedUser.nickname
        headerView.user_name_drawer.text      = loggedUser.name
        if (loggedUser.email != "null") headerView.user_email_drawer.text = loggedUser.email
        else headerView.user_email_drawer.visibility = View.GONE
    }


    //TODO: feature WIP (navigation drawer show teams the logged user is in)
    //NOTE: atm is being given an emptyList() so it knows that it needs to hide "My teams" section
    private fun drawerMyTeams(navigationView: NavigationView, teams: List<Team>) {
        val myTeamsGroup = navigationView.menu.findItem(R.id.drawer_group_my_teams)

        if (!teams.isEmpty()) {
            val subMenu = myTeamsGroup.subMenu
            for (i in 0 until teams.size) {
                subMenu.add(0, i, 0, teams[i].name).setIcon(R.drawable.ic_group_white_24dp)
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
                            Preferences.remove(R.string.spKey__login_token.toString())
                            Preferences.remove(R.string.spKey__login_orgID.toString())
                            startActivity(Intent(this, LoginActivity::class.java))
                            finish()
                        },
                        no = {}
                )
            }
            else -> {
                //TODO: feature WIP (navigation drawer show teams the logged user is in)
                /*
                val i = Intent(this, ChatActivity::class.java)
                i.putExtra("team", viewModel.loggedUserTeams.value!![item.itemId])
                startActivity(i)
                */
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
