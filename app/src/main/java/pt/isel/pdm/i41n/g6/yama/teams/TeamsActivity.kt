package pt.isel.pdm.i41n.g6.yama.teams

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_teams.*
import pt.isel.pdm.i41n.g6.yama.R

class TeamsActivity : AppCompatActivity() {
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var layoutMgr: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teams)

        val value = intent.getSerializableExtra("loggedUser")

        Toast.makeText(this, value.toString(), Toast.LENGTH_LONG).show()

        val cities = arrayOf(
                "Groot-Bijgaarden",
                "Jhansi",
                "Hénin-Beaumont",
                "Huntly",
                "Talgarth"
        )
        layoutMgr = LinearLayoutManager(this)
        viewAdapter = StringAdapter(cities)

        cities_recycler_view.apply {
            setHasFixedSize(true)
            layoutManager = layoutMgr
            adapter = viewAdapter
        }
    }
}
