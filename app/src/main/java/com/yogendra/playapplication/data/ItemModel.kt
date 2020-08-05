package com.yogendra.playapplication.data

import androidx.room.Entity
import com.yogendra.socialmediamvvm.utils.DATE_FORMAT_20
import com.yogendra.socialmediamvvm.utils.formatDateFromDateString
import retrofit2.http.Field
import java.text.SimpleDateFormat
import java.util.*

@Entity(tableName = "Details", primaryKeys = ["id"])
data class Itemdetail(
    val id: String,
    val descendants: String,
    val by: String,
    val kids: List<Long>? = emptyList(),
    val score: String,
    val time: String,
    val type: String,
    val title: String,
    val url: String

) {

    fun getDateFormatted(): String {
        return formatDateFromDateString(DATE_FORMAT_20, DATE_FORMAT_20, getDateTime(time))
    }

    private fun getDateTime(s: String): String {
        try {
            val sdf = SimpleDateFormat(DATE_FORMAT_20)
            val netDate = Date(s.toLong() * 1000) //Seconds to milliseconds
            return sdf.format(netDate)
        } catch (e: Exception) {
            return e.toString()
        }
    }
}

data class Allkeys(
    @Field("")
    val keys: List<Long>? = emptyList(),
    var next_key: Long? = 0

)