package pt.isel.pdm.li51d.g10.yama.activities.chat

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import pt.isel.pdm.li51d.g10.yama.R
import pt.isel.pdm.li51d.g10.yama.data.dto.Message

class ChatAdapter(private val data: MutableList<Message>) : RecyclerView.Adapter<ChatAdapter.ItemViewHolder>() {

    private val TAG = ChatAdapter::class.java.simpleName

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val chatText   = itemView.findViewById<TextView>(R.id.chat_text)

        fun bindItems(msg: Message) {
            chatText.text   = msg.text
        }
    }

    /**
     * Create new views (invoked by the layout manager)
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        // create a new view
        val view = LayoutInflater.from(parent.context).inflate(R.layout.message, parent, false)
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