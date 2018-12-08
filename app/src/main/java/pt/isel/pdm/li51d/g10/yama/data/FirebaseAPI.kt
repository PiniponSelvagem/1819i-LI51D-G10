package pt.isel.pdm.li51d.g10.yama.data

import android.content.Context
import android.util.Log
import android.view.View
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import pt.isel.pdm.li51d.g10.yama.data.database.message.Message

object FirebaseAPI {

    private lateinit var messagesCollection: DocumentReference

    fun init(context: Context) {
        messagesCollection = FirebaseFirestore.getInstance()
                .collection("billboard")
                .document("messages")
    }

    fun saveBillboardMessage(message: Message) {
        val msg = mapOf(
                "user" to message.userNickname,
                "text" to message.text
        )

        val t = messagesCollection.collection(message.teamId.toString())
                .document(message.timestamp.toString())

        t.set(msg).addOnSuccessListener {
            Log.i("THIS", "New billboard document has been saved")
        }.addOnFailureListener {
            Log.w("THIS", "New billboard document NOT saved", it)
        }
    }

    fun fetchBillboardMessage(teamId: Int) {
        messagesCollection.collection(teamId.toString()).get().addOnSuccessListener {
            updateBillboardMessage(it)
        }.addOnFailureListener {
            Log.w("THIS", "New billboard document NOT saved", it)
        }
    }

    private fun updateBillboardMessage(querySnapshot: QuerySnapshot?): List<MutableMap<String, Any>?> {
        if (querySnapshot != null && !querySnapshot.isEmpty) {
            Log.i("THIS", "Collection updated:"+querySnapshot.documents.size)
            return querySnapshot.documents.map { it.data }
        }
        return emptyList()
    }

}