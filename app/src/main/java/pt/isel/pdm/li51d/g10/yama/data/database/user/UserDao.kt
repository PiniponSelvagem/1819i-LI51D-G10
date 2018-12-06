package pt.isel.pdm.li51d.g10.yama.data.database.user

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {

    @Query("SELECT * from users ORDER BY nickname ASC")
    fun getAllUsers(): LiveData<MutableList<User>>

    @Query("SELECT * from users WHERE id == :id")
    fun getUser(id: Int): User

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(user: User)

    @Query("DELETE FROM users")
    fun deleteAll()
}