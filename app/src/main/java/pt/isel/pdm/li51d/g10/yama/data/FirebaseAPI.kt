package pt.isel.pdm.li51d.g10.yama.data

import com.google.firebase.firestore.FirebaseFirestore
import pt.isel.pdm.li51d.g10.yama.network.HttpRequests

object FirebaseAPI {

    private val messagesReference = FirebaseFirestore.getInstance().collection("billboard").document("message")

    fun getLoggedUser(resp: (String) -> Unit, err: (Exception) -> Unit) {

    }
}