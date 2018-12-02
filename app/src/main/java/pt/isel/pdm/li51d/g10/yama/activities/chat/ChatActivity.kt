package pt.isel.pdm.li51d.g10.yama.activities.chat

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.view.*
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View.*
import kotlinx.android.synthetic.main.activity_chat.*
import pt.isel.pdm.li51d.g10.yama.R
import pt.isel.pdm.li51d.g10.yama.data.dto.Team
import pt.isel.pdm.li51d.g10.yama.activities.teamdetails.TeamDetailsActivity
import pt.isel.pdm.li51d.g10.yama.data.dto.Message
import pt.isel.pdm.li51d.g10.yama.utils.hideKeyboard

class ChatActivity : AppCompatActivity() {

    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var layoutMgr: RecyclerView.LayoutManager
    private lateinit var team: Team

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        team = intent.getSerializableExtra("team") as Team
        title = team.name

        chat_root.onFocusChangeListener = OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                hideKeyboard(this, this.currentFocus)
            }
        }

        val viewModel = ViewModelProviders.of(this).get(ChatViewModel::class.java)

        layoutMgr = LinearLayoutManager(this)
        viewAdapter = ChatAdapter(viewModel.message.value!!)

        btn_send.setOnClickListener {
            if (set_message.text.toString() != "") {    //check if message to send is empty
                viewModel.addMessage(Message(set_message.text.toString(), true))
                set_message.text.clear()
                viewAdapter.notifyDataSetChanged()
                scrollToBottom()
            }
        }

        chat_recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = layoutMgr
            adapter = viewAdapter
        }
    }

    private fun scrollToBottom() {
        chat_recyclerView.smoothScrollToPosition(viewAdapter.itemCount - 1)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the main_menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_chat, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.details -> {
                showTeamDetails()
                return true
            }
        }
        return false
    }

    private fun showTeamDetails() {
        val i = Intent(this, TeamDetailsActivity::class.java)
        i.putExtra("team", team)
        startActivity(i)
    }
}
