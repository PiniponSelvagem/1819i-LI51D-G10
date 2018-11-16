package pt.isel.pdm.i41n.g6.yama.activities.teams.adapters

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import pt.isel.pdm.i41n.g6.yama.R
import pt.isel.pdm.i41n.g6.yama.data.Team
import pt.isel.pdm.i41n.g6.yama.activities.chat.ChatActivity

class TeamsAdapter(private val data: MutableList<Team>) : RecyclerView.Adapter<TeamsAdapter.ItemViewHolder>() {

    private val TAG = TeamsAdapter::class.java.simpleName

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val teamName = itemView.findViewById<TextView>(R.id.team_name)
        val teamId = itemView.findViewById<TextView>(R.id.team_id)

        fun bindItems(team: Team) {
            teamName.text = team.name
            teamId.text   = team.id.toString()

            itemView.setOnClickListener {
                val i = Intent(itemView.context, ChatActivity::class.java)
                i.putExtra("team", team)
                itemView.context.startActivity(i)
            }
        }
    }

    /**
     * Create new views (invoked by the layout manager)
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : ItemViewHolder {
        // create a new view
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_team, parent, false)
        return ItemViewHolder(view)
    }


    /**
     * Replace the contents of a view (invoked by the layout manager)
     */
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        holder.bindItems(data[position])
    }

    /**
     * Return the size of your dataset (invoked by the layout manager)
     */
    override fun getItemCount(): Int = data.size
}