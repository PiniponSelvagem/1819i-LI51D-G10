package pt.isel.pdm.i41n.g6.yama.organization

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
import pt.isel.pdm.i41n.g6.yama.data.LoggedUser
import pt.isel.pdm.i41n.g6.yama.data.Team
import pt.isel.pdm.i41n.g6.yama.data.httprequests.Volley

//TODO: save state
class TeamsActivity : AppCompatActivity() {
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var layoutMgr: RecyclerView.LayoutManager

    private var teams = mutableListOf<Team>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teams)

        val loggedUser = intent.getSerializableExtra("loggedUser") as LoggedUser

        val headers = mutableMapOf<String, String>()
        headers["Authorization"] = "token "+loggedUser.token


        val queue = Volley.getRequestQueue()
        val myReq = Volley.HeadersStringRequest(Request.Method.GET,
                "https://api.github.com/orgs/${loggedUser.orgID}/teams",
                headers,
                createReqSuccessListener(),
                createReqErrorListener())
        queue.add(myReq)

        layoutMgr = LinearLayoutManager(this)
        viewAdapter = TeamsAdapter(teams)

        teams_recycler_view.apply {
            setHasFixedSize(false)
            layoutManager = layoutMgr
            adapter = viewAdapter
        }
    }

    private fun createReqSuccessListener(): Response.Listener<String> = Response.Listener {
        response ->
        run {
            try {
                teams = teamsData(response)
                viewAdapter.notifyDataSetChanged()
                Toast.makeText(this, "TEAMS UPDATED", Toast.LENGTH_LONG).show()
            } catch (e: JSONException) {
                e.printStackTrace() //TODO: do logging
            }
        }
    }

    //TODO: dosent look pretty
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

    //TODO: ATM CAN RETURN DUPLICATED TEAMS (the team that the user is in)
    private fun teamsData(response: String) : MutableList<Team> {
        val jArray = JSONArray(response)
        var jObj : JSONObject

        for ( i in 0 until jArray.length()) {
            jObj = jArray[i] as JSONObject
            teams.add(Team(jObj.getString("name"), jObj.getInt("id")))
        }

        return teams
    }

}
