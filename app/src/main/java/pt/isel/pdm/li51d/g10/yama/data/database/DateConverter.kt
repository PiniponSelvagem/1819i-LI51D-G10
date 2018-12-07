package pt.isel.pdm.li51d.g10.yama.data.database

import android.annotation.SuppressLint
import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.*

class DateConverter {

    @SuppressLint("SimpleDateFormat")
    val dateFormat = SimpleDateFormat("YYYY-MM-DD HH:MM:SS")

    @TypeConverter
    fun fromTimestamp(value: String): Date { //TODO: maybe this needs a try catch?
        //NOTE: http://androidkt.com/datetime-datatype-sqlite-using-room/
        return dateFormat.parse(value)
    }
}