package pt.isel.pdm.li51d.g10.yama.data.database.message

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "messages")
data class Message(
        @NonNull
        @ColumnInfo(name = "user_nickname")
        val userNickname:   String,

        @PrimaryKey
        @NonNull
        @ColumnInfo(name = "timestamp")
        val timestamp:      Long,

        @NonNull
        @ColumnInfo(name = "text")
        val text:           String,

        @NonNull
        @ColumnInfo(name = "teamId")
        val teamId:         Int,

        @ColumnInfo(name = "from_logged_user")
        val fromLoggedUser: Boolean
)