package com.yogendra.playapplication.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "allkeys")
data class Allkeys(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "key_id")
    val key_id: Int = 1,
    val key_value: String,
    val next_page: Long? = 0
)
