package pt.isel.pdm.li51d.g10.yama.data.database.team.teamusers

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import pt.isel.pdm.li51d.g10.yama.data.database.team.Team
import pt.isel.pdm.li51d.g10.yama.data.database.user.User

@Dao
interface TeamUserDao {

    @Insert
    fun insert(join: TeamUser)

    @Query("""
        SELECT * FROM users INNER JOIN teamUser ON
        users.id = teamUser.user_id WHERE
        teamUser.team_id = :teamId
        ORDER BY name ASC
        """)
    fun getUsersForTeam(teamId: Int): MutableList<User>

    @Query("""
        SELECT * FROM teams INNER JOIN teamUser ON
        teams.id = teamUser.team_id WHERE
        teamUser.user_id = :userId
        """)
    fun getTeamsForUser(userId: Int): List<Team>

    @Query("DELETE FROM teamUser")
    fun deleteAll()
}