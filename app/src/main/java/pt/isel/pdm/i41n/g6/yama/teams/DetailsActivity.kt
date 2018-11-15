package pt.isel.pdm.i41n.g6.yama.teams

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.ImageView
import android.widget.Toast
import com.android.volley.*
import com.android.volley.toolbox.ImageRequest
import kotlinx.android.synthetic.main.activity_details.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import pt.isel.pdm.i41n.g6.yama.R
import pt.isel.pdm.i41n.g6.yama.data.Team
import pt.isel.pdm.i41n.g6.yama.data.User
import pt.isel.pdm.i41n.g6.yama.data.httprequests.Volley


class DetailsActivity : AppCompatActivity() {
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

        val queue = Volley.getRequestQueue()
        val myReq = Volley.HeadersStringRequest(Request.Method.GET,
                "https://api.github.com/teams/${team.id}/members",
                headers,
                createReqSuccessListener(),
                createReqErrorListener())
        queue.add(myReq)

        layoutMgr = LinearLayoutManager(this)
        viewAdapter = TeamAdapter(teamUsers)

        team_recycler_view.apply {
            setHasFixedSize(true)
            layoutManager = layoutMgr
            adapter = viewAdapter
        }
    }

    private fun createReqSuccessListener(): Response.Listener<String> = Response.Listener {
        response ->
        run {
            try {
                teamData(response)
                viewAdapter.notifyDataSetChanged()
                //Toast.makeText(this, teamUsers.toString(), Toast.LENGTH_LONG).show()
            } catch (e: JSONException) {
                e.printStackTrace() //TODO: do logging
            }
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

    private fun teamData(response: String) {
        val jArray = JSONArray(response)
        var jObj: JSONObject
        for ( i in 0 until jArray.length()) {
            jObj = jArray[i] as JSONObject
            //TODO: improve
            val headers = mutableMapOf<String, String>()
            headers["Authorization"] = token
            val queue = Volley.getRequestQueue()
            val myReq = Volley.HeadersStringRequest(Request.Method.GET,
                    jObj.getString("url"),
                    headers,
                    createReqSuccessListenerUser(),
                    createReqErrorListener())
            queue.add(myReq)
            //TODO: end
        }
    }

    private fun createReqSuccessListenerUser(): Response.Listener<String> = Response.Listener {
        response ->
        run {
            try {
                teamUsers.add(getUserData(response))
                viewAdapter.notifyDataSetChanged()
            } catch (e: JSONException) {
                e.printStackTrace() //TODO: do logging
            }
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

        loadImage(user, jObj.getString("avatar_url"))

        return user
    }

    //TODO: make it more pretty and maybe move this to other place, but GOD DAMN IT FINALLY WORKS!!!
    private fun loadImage(user: User, url: String) {
        val imageRequest = ImageRequest(
                url,
                Response.Listener {
                    user.avatar = it
                    viewAdapter.notifyDataSetChanged()
                },
                500, 500,
                ImageView.ScaleType.CENTER,
                Bitmap.Config.ARGB_8888,
                Response.ErrorListener {
                    TODO("NOT IMPLEMENTED")
                }
        )

        headers["Authorization"] = token

        val queue = Volley.getRequestQueue()
        queue.add(imageRequest)
    }
}
