package pt.isel.pdm.li51d.g10.yama.data.database.user

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
class User(
        @PrimaryKey
        @NonNull
        @ColumnInfo(name = "id")
        val id: Int,

        @NonNull
        @ColumnInfo(name = "nickname")
        val nickname: String,

        @NonNull
        @ColumnInfo(name = "name")
        val name: String,

        @NonNull
        @ColumnInfo(name = "avatar_url")
        val avatarUrl: String,

        @NonNull
        @ColumnInfo(name = "followers")
        val followers: Int,

        @NonNull
        @ColumnInfo(name = "following")
        val following: Int,

        @NonNull
        @ColumnInfo(name = "email")
        val email: String,

        @NonNull
        @ColumnInfo(name = "location")
        val location: String,

        @NonNull
        @ColumnInfo(name = "blog")
        val blog: String,

        @NonNull
        @ColumnInfo(name = "bio")
        val bio: String
)