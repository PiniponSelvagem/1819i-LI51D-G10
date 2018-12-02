package pt.isel.pdm.li51d.g10.yama.activities.chat

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import pt.isel.pdm.li51d.g10.yama.R
import pt.isel.pdm.li51d.g10.yama.data.Preferences
import pt.isel.pdm.li51d.g10.yama.data.dto.Message
import pt.isel.pdm.li51d.g10.yama.data.dto.User

class ChatViewModel  : ViewModel() {

    private val messageLiveData = MutableLiveData<MutableList<Message>>()
    val message: LiveData<MutableList<Message>> = messageLiveData

    private val isRefreshedLiveData = MutableLiveData<Boolean>()
    val isRefreshed = isRefreshedLiveData

    init {
        messageLiveData.value = mutableListOf()
        //isRefreshedLiveData.value = false
    }

    fun addMessage(message: Message) {
        messageLiveData.value!!.add(message)
    }
}