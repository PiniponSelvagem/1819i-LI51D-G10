package pt.isel.pdm.li51d.g10.yama.data.database.message.teammessages

import androidx.annotation.NonNull
import androidx.room.*
import pt.isel.pdm.li51d.g10.yama.data.database.message.Message
import pt.isel.pdm.li51d.g10.yama.data.database.team.Team
import pt.isel.pdm.li51d.g10.yama.data.database.user.User

@Entity(tableName = "team_messages",
        primaryKeys = ["team_id", "user_id"],
        indices = [
            Index(value = ["team_id"]),
            Index(value = ["message"])
        ],
        foreignKeys = [
            ForeignKey(entity = Team::class,
                    parentColumns = ["id"],
                    childColumns = ["team_id"]),
            ForeignKey(entity = Message::class,
                    parentColumns = ["user_nickname"],
                    childColumns = ["message"])
        ])
data class TeamMessage(

        @NonNull
        @ColumnInfo(name = "team_id")
        val teamId: Int,

        @NonNull
        @ColumnInfo(name = "message")
        val userId: String
)