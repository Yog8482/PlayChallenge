package com.yogendra.playapplication.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import retrofit2.http.Field

//First- response allkeys object
data class Keys(
    @Field("")
    val keys: List<String> = emptyList()
)

@Entity(tableName = "allkeys")
data class Allkeys(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "key_id")
    val key_id: Int = -1,
    val key_value: String
)