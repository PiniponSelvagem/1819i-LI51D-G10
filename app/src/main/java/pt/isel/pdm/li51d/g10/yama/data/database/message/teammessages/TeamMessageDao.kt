package pt.isel.pdm.li51d.g10.yama.data.database.message.teammessages

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import pt.isel.pdm.li51d.g10.yama.data.database.message.Message
import pt.isel.pdm.li51d.g10.yama.data.database.team.Team
import pt.isel.pdm.li51d.g10.yama.data.database.team.teamusers.TeamUser
import pt.isel.pdm.li51d.g10.yama.data.database.user.User

@Dao
interface TeamMessageDao {
    @Insert
    fun insert(join: TeamMessage)

    @Query("""
        SELECT * FROM team_messages
        INNER JOIN messages
        ON team_messages.message = message
        WHERE team_messages.team_id = teamId
        ORDER BY timestamp ASC
        """)
    fun getMessagesForTeam(teamId: Int): MutableList<Message>

//    @Query("""
//        SELECT * FROM teams INNER JOIN teamUser ON
//        teams.id = teamUser.team_id WHERE
//        teamUser.user_id = :userId
//        """)
//    fun getTeamsForUser(userId: Int): List<Team>

    @Query("DELETE FROM team_messages")
    fun deleteAll()
}