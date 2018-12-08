package pt.isel.pdm.li51d.g10.yama.data.database.message

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MessageDao {

    @Query("""
        SELECT * from messages
        WHERE messages.teamId = :teamId
        ORDER BY timestamp ASC""")
    fun getTeamMessages(teamId: Int): MutableList<Message>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(message: Message)

    @Query("DELETE FROM messages")
    fun deleteAll()
}