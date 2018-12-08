package pt.isel.pdm.li51d.g10.yama.data.database.team

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "teams")
data class Team(
        @PrimaryKey
        @NonNull
        @ColumnInfo(name = "id")
        val id: Int,

        @NonNull
        @ColumnInfo(name = "name")
        val name: String
) : Serializable