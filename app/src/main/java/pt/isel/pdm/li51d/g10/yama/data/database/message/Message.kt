package pt.isel.pdm.li51d.g10.yama.data.database.message

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import pt.isel.pdm.li51d.g10.yama.data.database.team.Team

@Entity(tableName = "messages",
        foreignKeys = [
            ForeignKey(entity = Team::class,
                    parentColumns = ["id"],
                    childColumns = ["teamId"])
        ])
data class Message(
        @PrimaryKey
        @NonNull
        @ColumnInfo(name = "user_nickname")
        val userNickname:   String,

        @NonNull
        @ColumnInfo(name = "timestamp")
        val timestamp:      Long,

        @NonNull
        @ColumnInfo(name = "text")
        val text:           String,

        @NonNull
        @ColumnInfo(name = "from_logged_user")
        val fromLoggedUser: Boolean,

        @NonNull
        @ColumnInfo(name = "teamId")
        val teamId:         Int
)