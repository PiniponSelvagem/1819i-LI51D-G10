package pt.isel.pdm.li51d.g10.yama.data.database.team

import androidx.annotation.NonNull
import androidx.room.*
import java.io.Serializable

@Entity(tableName = "teams")
class Team(
        @PrimaryKey
        @NonNull
        @ColumnInfo(name = "id")
        val id: Int,

        @NonNull
        @ColumnInfo(name = "name")
        val name: String
) : Serializable