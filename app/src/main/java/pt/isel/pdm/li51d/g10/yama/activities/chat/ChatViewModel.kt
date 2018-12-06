package pt.isel.pdm.li51d.g10.yama.activities.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import pt.isel.pdm.li51d.g10.yama.data.dto.Message

class ChatViewModel : ViewModel() {

    private val messageLiveData = MutableLiveData<MutableList<Message>>()
    val message: LiveData<MutableList<Message>> = messageLiveData

    init {
        messageLiveData.value = mutableListOf()
    }

    fun addMessage(message: Message) {
        messageLiveData.value!!.add(message)
    }
}