package pt.isel.pdm.i41n.g6.yama.teams

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableRow
import android.widget.TextView
import pt.isel.pdm.i41n.g6.yama.R
import pt.isel.pdm.i41n.g6.yama.data.User

class TeamAdapter(private val data: MutableList<User>) : RecyclerView.Adapter<TeamAdapter.ItemViewHolder>() {

    private val TAG = TeamAdapter::class.java.simpleName

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //TODO: avatar
        val nickname    = itemView.findViewById<TextView>(R.id.user_nickname)
        val name        = itemView.findViewById<TextView>(R.id.user_name)
        val followers   = itemView.findViewById<TextView>(R.id.user_followers)
        val following   = itemView.findViewById<TextView>(R.id.user_following)

        val emailRow    = itemView.findViewById<TableRow>(R.id.user_email_row)
        val email       = itemView.findViewById<TextView>(R.id.user_email)

        val locationRow = itemView.findViewById<TableRow>(R.id.user_location_row)
        val location    = itemView.findViewById<TextView>(R.id.user_location)

        val blogRow     = itemView.findViewById<TableRow>(R.id.user_blog_row)
        val blog        = itemView.findViewById<TextView>(R.id.user_blog)

        val bioRow      = itemView.findViewById<TableRow>(R.id.user_bio_row)
        val bio         = itemView.findViewById<TextView>(R.id.user_bio)

        fun bindItems(user: User) {
            nickname.text = user.nickname
            name.text = user.name
            followers.text = user.followers.toString()
            following.text = user.following.toString()
            
            if (user.email != "null") email.text = user.email
            else emailRow.visibility = View.GONE

            if (user.location != "null") location.text = user.location
            else locationRow.visibility = View.GONE

            if (user.blog != "") blog.text = user.blog
            else blogRow.visibility = View.GONE

            if (user.bio != "null") bio.text = user.bio
            else bioRow.visibility = View.GONE
        }
    }

    /**
     * Create new views (invoked by the layout manager)
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : ItemViewHolder {
        // create a new view
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_user, parent, false)
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