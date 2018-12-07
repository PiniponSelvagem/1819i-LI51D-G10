package pt.isel.pdm.li51d.g10.yama.data.database.message

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MessageDao {

    @Query("SELECT * from messages ORDER BY timestamp ASC")
    fun getAllMessages(): LiveData<MutableList<Message>>

    @Query("SELECT * from messages ORDER BY timestamp ASC")
    fun getAllMessagesList(): List<Message>

    @Insert
    fun insert(message: Message)

    @Query("DELETE FROM messages")
    fun deleteAll()
}