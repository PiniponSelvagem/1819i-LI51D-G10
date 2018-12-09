package pt.isel.pdm.li51d.g10.yama.data

import android.util.Log
import com.google.firebase.firestore.QuerySnapshot
import pt.isel.pdm.li51d.g10.yama.data.database.message.Message

object FirebaseAPI {

    private val TAG: String = FirebaseAPI::class.java.simpleName

    fun fetchMessages(teamID: Int, loggedUserNickname: String, query: QuerySnapshot) : MutableList<Message> {
        val messages = mutableListOf<Message>()
        Log.i(TAG, "Fetching messages")

        for (i in 0 until query.size()) {
            val data = query.documents[i].data
            if (data != null) {
                messages.add(
                        Message(data["user"].toString(),
                                query.documents[i].id.toLong(),
                                data["text"].toString(),
                                teamID,
                                loggedUserNickname==data["user"].toString()
                        )
                )
            }
        }
        Log.i(TAG, "Messages fetched")

        return messages
    }


    //
    // This code below would be better since it would only "get" the
    // document changes. But the problem with it, is that for some
    // reason the recycle view that is going to use the teamMessages from
    // YamaDatabase, bugs by placing the new changed item at position 0,
    // replacing allways the top most value of the view and never adding
    // a new one. Maybe is a problem related to ChatAdapter???
    // Or is it related to the teamMessages.postValue
    // at YamaDatabase.getMessagesForTeam???
    //
    /*
    for (i in 0 until query.documentChanges.size) {
            val data = query.documentChanges[i].document
            messages.add(
                    Message(data["user"].toString(),
                            query.documents[i].id.toLong(),
                            data["text"].toString(),
                            teamID,
                            loggedUserNickname==data["user"].toString()
                    )
            )
        }
     */
}