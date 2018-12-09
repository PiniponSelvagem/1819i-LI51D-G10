package pt.isel.pdm.li51d.g10.yama.activities.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import pt.isel.pdm.li51d.g10.yama.R
import pt.isel.pdm.li51d.g10.yama.data.database.message.Message


class ChatAdapter constructor(private val owner: LifecycleOwner, private val data: MutableLiveData<MutableList<Message>>) : RecyclerView.Adapter<ChatAdapter.ItemViewHolder>() {

    private val fromOtherUser  = 0
    private val fromLoggedUser = 1

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val chatText = itemView.findViewById<TextView>(R.id.chat_text)
        val chatUser = itemView.findViewById<TextView>(R.id.chat_usernickname)

        fun bindItems(msg: Message) {
            chatText.text = msg.text
            if (chatUser!=null) chatUser.text = msg.userNickname
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (data.value!![position].fromLoggedUser) {
            return fromLoggedUser
        } else {
            return fromOtherUser
        }
    }

    /**
     * Create new views (invoked by the layout manager)
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        // create a new view
        return if (viewType==fromLoggedUser)
            ItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.message, parent, false))
        else
            ItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.message_sender, parent, false))
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