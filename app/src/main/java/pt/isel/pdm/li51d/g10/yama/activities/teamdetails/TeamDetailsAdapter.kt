package pt.isel.pdm.li51d.g10.yama.activities.teamdetails

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TableRow
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import pt.isel.pdm.li51d.g10.yama.R
import pt.isel.pdm.li51d.g10.yama.data.database.user.User
import pt.isel.pdm.li51d.g10.yama.utils.convertBytesToBitmap

class TeamDetailsAdapter constructor(private val owner: LifecycleOwner, val data: MutableLiveData<MutableList<User>>) : RecyclerView.Adapter<TeamDetailsAdapter.ItemViewHolder>() {

    private val TAG = TeamDetailsAdapter::class.java.simpleName

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val avatar      = itemView.findViewById<ImageView>(R.id.user_avatar)
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
            avatar.setImageBitmap(convertBytesToBitmap(user.avatar))
            nickname.text = user.nickname
            name.text = user.name
            followers.text = user.followers.toString()
            following.text = user.following.toString()
            email.text = user.email
            location.text = user.location
            user.blog
            bio.text = user.bio
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
        val current = data.value!![position]
        showHideIndividualData(holder, current)
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


    private fun showHideIndividualData(holder: ItemViewHolder, current: User) {
        if (current.name != "null") holder.name.visibility = View.VISIBLE
        else holder.name.visibility = View.GONE

        if (current.email != "null") holder.emailRow.visibility = View.VISIBLE
        else holder.emailRow.visibility = View.GONE

        if (current.location != "null") holder.locationRow.visibility = View.VISIBLE
        else holder.locationRow.visibility = View.GONE

        if (current.blog != "") holder.blogRow.visibility = View.VISIBLE
        else holder.blogRow.visibility = View.GONE

        if (current.bio != "null") holder.bioRow.visibility = View.VISIBLE
        else holder.bioRow.visibility = View.GONE
    }
}