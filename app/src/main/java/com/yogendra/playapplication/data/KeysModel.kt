package com.yogendra.playapplication.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONObject
import retrofit2.http.Field

//First- response allkeys object
//data class Keys(
//
//    val keys: Array<Int> = arrayOf()
////    val keys: Array<Int> = arrayOf()
//) {
//
//    fun getList(): List<Int> {
//        val jo = JSONObject();
//        val ja = keys
//        jo.put("key_list", ja);
//
//        val listType = object : TypeToken<List<Int?>>() {}.type
//        val list = Gson().fromJson<List<Int>>(keys.toString(), listType)
//        return list
//    }
//}
//
//data class DataStories(
//    val key_list: List<Int>
//)


@Entity(tableName = "allkeys")
data class Allkeys(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "key_id")
    val key_id: Int = 0,
    val key_value: String,
    val next_page: Long? = 0
)
