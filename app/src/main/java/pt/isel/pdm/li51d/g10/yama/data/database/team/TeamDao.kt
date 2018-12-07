package pt.isel.pdm.li51d.g10.yama.data.database.team

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TeamDao {

    @Query("SELECT * from teams ORDER BY name ASC")
    fun getAllTeams(): LiveData<MutableList<Team>>

    @Query("SELECT * from teams ORDER BY name ASC")
    fun getAllTeamsList(): List<Team>

    @Insert
    fun insert(team: Team)

    @Query("DELETE FROM teams")
    fun deleteAll()
}