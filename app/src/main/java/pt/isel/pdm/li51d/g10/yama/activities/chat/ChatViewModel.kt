package pt.isel.pdm.li51d.g10.yama.activities.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import pt.isel.pdm.li51d.g10.yama.data.Repository
import pt.isel.pdm.li51d.g10.yama.data.database.message.Message

class ChatViewModel(private val repository: Repository) : ViewModel() {

    private val messageLiveData = MutableLiveData<MutableList<Message>>()
    val message: LiveData<MutableList<Message>> = messageLiveData

    init {
        messageLiveData.value = mutableListOf()
    }

    fun addMessage(time: Long, msg: String, teamId: Int, isInternetAvailable: Boolean) : Message {
        //sample message
        //val message = Message("user.login", Date(2018, 12, 31, 10, 1), msg, true)
        val message = Message("user.nickname", time, msg, true, teamId)
        messageLiveData.value!!.add(message)
        repository.saveMessage(message, isInternetAvailable)
        return message
    }

    fun updateBillboardMessage(teamId: Int, isInternetAvailable: Boolean) {
        repository.loadMessages(teamId, isInternetAvailable)
    }
}