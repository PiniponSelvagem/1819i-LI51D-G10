package pt.isel.pdm.i41n.g6.yama.activities.teams

import android.content.Context
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Toast
import com.android.volley.*
import kotlinx.android.synthetic.main.activity_teams.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import pt.isel.pdm.i41n.g6.yama.R
import pt.isel.pdm.i41n.g6.yama.data.Team
import pt.isel.pdm.i41n.g6.yama.data.User
import pt.isel.pdm.i41n.g6.yama.network.HttpRequests
import pt.isel.pdm.i41n.g6.yama.activities.teams.adapters.TeamsAdapter

//TODO: save state
class TeamsActivity : AppCompatActivity() {
    private lateinit var preferences : SharedPreferences

    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var layoutMgr: RecyclerView.LayoutManager

    private var teams = mutableListOf<Team>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teams)

        preferences = this.getSharedPreferences("login", Context.MODE_PRIVATE)
        val token = "token ${preferences.getString(R.string.spKey__login_token.toString(), "")}"
        val orgID = preferences.getString(R.string.spKey__login_orgID.toString(), "")

        //TODO: this loggerUser should go to the activity that will display user info
        val loggedUser = intent.getSerializableExtra("loggedUser") as User

        val headers = mutableMapOf<String, String>()
        headers["Authorization"] = token

        HttpRequests.getString("https://api.github.com/orgs/$orgID/teams", headers) {
            str ->
            run {
                try {
                    teamsData(str)
                    viewAdapter.notifyDataSetChanged()
                    Toast.makeText(this, "TEAMS UPDATED", Toast.LENGTH_LONG).show()
                } catch (e: JSONException) {
                    e.printStackTrace() //TODO: do logging
                }
            }
        }

        layoutMgr = LinearLayoutManager(this)
        viewAdapter = TeamsAdapter(teams)

        teams_recycler_view.apply {
            setHasFixedSize(true)
            layoutManager = layoutMgr
            adapter = viewAdapter
        }
    }

    //TODO: this looks like shit
    private fun createReqErrorListener(): Response.ErrorListener = Response.ErrorListener {
        error ->
        run {
            if (error is TimeoutError || error is NoConnectionError) {
                Toast.makeText(this, "Timeout", Toast.LENGTH_LONG).show()
            } else if (error is AuthFailureError) {
                Toast.makeText(this, "Authentication Error", Toast.LENGTH_LONG).show()
            } else if (error is ServerError) {
                Toast.makeText(this, "Server Error", Toast.LENGTH_LONG).show()
            } else if (error is NetworkError) {
                Toast.makeText(this, "Network Error", Toast.LENGTH_LONG).show()
            } else if (error is ParseError) {
                Toast.makeText(this, "Parse Error", Toast.LENGTH_LONG).show()
            }
        }
    }

    //TODO: make teams.add more clear, maybe return the mutableList instead
    private fun teamsData(response: String) {
        val jArray = JSONArray(response)
        var jObj : JSONObject

        for ( i in 0 until jArray.length()) {
            jObj = jArray[i] as JSONObject
            teams.add(Team(jObj.getString("name"), jObj.getInt("id")))
        }
    }

}
