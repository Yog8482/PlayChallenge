package com.yogendra.playapplication.data

import androidx.room.Entity
import androidx.room.Ignore

@Entity(tableName = "details", primaryKeys = ["id"])
//Second-Response Object for each key
data class Itemdetail(
    val id: String,
    val descendants: String?,
    val by: String?,
//    val kids: List<String>? = emptyList(),
    val score: String?,
    val time: String?,
    val type: String?,
    val title: String?,
    val url: String?
)
