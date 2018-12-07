package pt.isel.pdm.li51d.g10.yama.data.database.message

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "messages")
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
        val fromLoggedUser: Boolean
)