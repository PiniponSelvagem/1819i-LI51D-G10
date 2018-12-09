package pt.isel.pdm.li51d.g10.yama.activities.chat

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.QuerySnapshot
import pt.isel.pdm.li51d.g10.yama.data.Repository
import pt.isel.pdm.li51d.g10.yama.data.database.message.Message

class ChatViewModel(private val repository: Repository) : ViewModel() {

    val teamMessages: MutableLiveData<MutableList<Message>> = repository.teamMessages.also {
        if (repository.teamMessages.value != null) repository.teamMessages.value!!.clear()
    }

    fun addMessage(timestamp: Long, text: String, teamID: Int) : Message {
        return Message(
                repository.loggedUser.value!!.nickname,
                timestamp,
                text,
                teamID,
                true
        )
    }

    fun fetchMessages(teamID: Int, querySnapshot: QuerySnapshot) {
        repository.fetchMessages(teamID, querySnapshot)
    }
}