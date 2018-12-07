package pt.isel.pdm.li51d.g10.yama.activities.chat

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.OnFocusChangeListener
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.android.synthetic.main.activity_chat.*
import pt.isel.pdm.li51d.g10.yama.R
import pt.isel.pdm.li51d.g10.yama.activities.teamdetails.TeamDetailsActivity
import pt.isel.pdm.li51d.g10.yama.data.database.team.Team
import pt.isel.pdm.li51d.g10.yama.utils.hideKeyboard

class ChatActivity : AppCompatActivity() {

    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var layoutMgr: RecyclerView.LayoutManager
    private lateinit var team: Team

    private lateinit var messagesCollention: CollectionReference
    private val firebaseInstance = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        team = intent.getSerializableExtra("team") as Team
        title = team.name
        messagesCollention = firebaseInstance.collection("yama").document("messages").collection(team.id.toString())

        messagesCollention.addSnapshotListener(this) { documentSnapshot, firebaseFirestoreException ->
            updateBillboardMessage(documentSnapshot)
        }

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

                saveBillboardMessage(set_message.text.toString())

                viewModel.addMessage(set_message.text.toString())
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

    var count = 0
    fun saveBillboardMessage(msg: String) {
        //TODO: commented atm since this is not the final result and this
        //TODO:     is something that should be done by FirebaseAPI???
        /*
        val message = mapOf(
                "MSG" to msg
        )


        val t = messagesCollention.document((++count).toString())

        t.set(message).addOnSuccessListener {
            Log.i("THIS", "New billboard document has been saved")
        }.addOnFailureListener {
            Log.w("THIS", "New billboard document NOT saved", it)
        }
        */
    }

    fun fetchBillboardMessage(view: View) {
        messagesCollention.get().addOnSuccessListener {
            updateBillboardMessage(it)
        }.addOnFailureListener {
            Log.w("THIS", "New billboard document NOT saved", it)
        }
    }

    private fun updateBillboardMessage(querySnapshot: QuerySnapshot?) {
        if (querySnapshot != null)
            Log.i("THIS", "Collection updated:"+querySnapshot.documents.size)
    }
}
