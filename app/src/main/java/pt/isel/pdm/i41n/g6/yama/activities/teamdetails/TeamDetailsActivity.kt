package pt.isel.pdm.i41n.g6.yama.activities.teamdetails

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_details.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import pt.isel.pdm.i41n.g6.yama.R
import pt.isel.pdm.i41n.g6.yama.activities.teamdetails.adapters.TeamAdapter
import pt.isel.pdm.i41n.g6.yama.data.Team
import pt.isel.pdm.i41n.g6.yama.data.User
import pt.isel.pdm.i41n.g6.yama.network.HttpRequests
import pt.isel.pdm.i41n.g6.yama.utils.showHttpErrorToast


class TeamDetailsActivity : AppCompatActivity() {
    private lateinit var preferences : SharedPreferences

    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var layoutMgr: RecyclerView.LayoutManager

    private lateinit var token: String
    private lateinit var team: Team
    private var teamUsers = mutableListOf<User>()
    private val headers = mutableMapOf<String, String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        team = intent.getSerializableExtra("team") as Team
        title = team.name

        preferences = this.getSharedPreferences("login", Context.MODE_PRIVATE)
        token = "token ${preferences.getString(R.string.spKey__login_token.toString(), "")}"

        val team = intent.getSerializableExtra("team") as Team

        headers["Authorization"] = token

        HttpRequests.getString("https://api.github.com/teams/${team.id}/members", headers,
                resp = { str ->
                    run {
                        try {
                            teamData(str)
                            viewAdapter.notifyDataSetChanged()
                        } catch (e: JSONException) {
                            e.printStackTrace() //TODO: do logging
                        }
                    }
                },
                err = { e -> showHttpErrorToast(this, e) }
        )

        layoutMgr = LinearLayoutManager(this)
        viewAdapter = TeamAdapter(teamUsers)

        team_recycler_view.apply {
            setHasFixedSize(true)
            layoutManager = layoutMgr
            adapter = viewAdapter
        }
    }

    private fun teamData(response: String) {
        val jArray = JSONArray(response)
        var jObj: JSONObject
        for ( i in 0 until jArray.length()) {
            jObj = jArray[i] as JSONObject

            HttpRequests.getString(jObj.getString("url"), headers,
                    resp = { str ->
                        run {
                            try {
                                teamUsers.add(getUserData(str))
                                viewAdapter.notifyDataSetChanged()
                            } catch (e: JSONException) {
                                e.printStackTrace() //TODO: do logging
                            }
                        }
                    },
                    err = { e -> showHttpErrorToast(this, e) }
            )
        }
    }

    private fun getUserData(response: String) : User {
        val jObj = JSONObject(response)
        val user = User(
                "",
                "",

                jObj.getString("login"),
                jObj.getString("name"),
                jObj.getInt("id"),
                jObj.getString("avatar_url"),
                jObj.getInt("followers"),
                jObj.getInt("following"),
                jObj.getString("email"),
                jObj.getString("location"),
                jObj.getString("blog"),
                jObj.getString("bio")
        )

        //TODO: getBitmap values maxWidth=500, maxHeight=500 should be the imageView sizes
        HttpRequests.getBitmap(jObj.getString("avatar_url"), 500, 500,
                resp = { bitmap ->
                    run {
                        user.avatar = bitmap
                        viewAdapter.notifyDataSetChanged()
                    }
                },
                err = { e -> showHttpErrorToast(this, e) }
        )

        return user
    }
}
