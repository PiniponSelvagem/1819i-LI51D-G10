package pt.isel.pdm.li51d.g10.yama.activities.teams

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import pt.isel.pdm.li51d.g10.yama.R
import pt.isel.pdm.li51d.g10.yama.activities.chat.ChatActivity
import pt.isel.pdm.li51d.g10.yama.data.database.team.Team

class TeamsAdapter constructor(owner: LifecycleOwner, val data: LiveData<MutableList<Team>>) : RecyclerView.Adapter<TeamsAdapter.ItemViewHolder>() {

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val teamName = itemView.findViewById<TextView>(R.id.team_name)
        val teamId   = itemView.findViewById<TextView>(R.id.team_id)

        fun bindItems(team: Team) {
            teamName.text = team.name
            teamId.text = team.id.toString()

            itemView.setOnClickListener {
                val i = Intent(itemView.context, ChatActivity::class.java)
                i.putExtra("team", team)
                itemView.context.startActivity(i)
            }
        }
    }

    init {
        data.observe(owner, Observer {
            this.notifyDataSetChanged()
        })
    }

    /**
     * Create new views (invoked by the layout manager)
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
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
        val current = data.value!![position]
        holder.bindItems(current)
    }

    /**
     * Return the size of your dataset (invoked by the layout manager)
     */
    // getItemCount() is called many times, and when it is first called,
    // words has not been updated (means initially, it's null, and we can't return null).
    override fun getItemCount(): Int {
        return data.value?.size ?: 0
    }
}