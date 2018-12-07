package pt.isel.pdm.li51d.g10.yama.data.database.team.teamusers

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import pt.isel.pdm.li51d.g10.yama.data.database.team.Team
import pt.isel.pdm.li51d.g10.yama.data.database.user.User

@Entity(tableName = "teamUser",
        primaryKeys = ["team_id", "user_id"],
        indices = [
            Index(value = ["team_id"]),
            Index(value = ["user_id"])
        ],
        foreignKeys = [
            ForeignKey(entity = Team::class,
                    parentColumns = ["id"],
                    childColumns = ["team_id"]),
            ForeignKey(entity = User::class,
                    parentColumns = ["id"],
                    childColumns = ["user_id"])
        ])
data class TeamUser(
        @ColumnInfo(name = "team_id") val teamId: Int,
        @ColumnInfo(name = "user_id") val userId: Int
)

//https://stackoverflow.com/questions/50799324/many-to-many-relations-with-room-livedata

