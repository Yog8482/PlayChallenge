package com.yogendra.playapplication.datasource.local

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yogendra.playapplication.data.Allkeys

@Dao
interface AllKeysDao {

    @Query("SELECT * FROM allkeys")
    fun getPagedKeys(): DataSource.Factory<Int, Allkeys>

    @Query("SELECT * FROM allkeys LIMIT :pagesize OFFSET :startrowid")
    fun getNextBatchOfKeys(pagesize: Int, startrowid: String): List<Allkeys>

    @Query("SELECT next_page FROM allkeys")
    fun getNextPageStartkey(): Long?

    @Query("SELECT rowid FROM allkeys WHERE key_value=:keyvalue")
    fun getRowidBykeyvalue(keyvalue: String): Int


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<Allkeys>)

    @Query("UPDATE allkeys SET next_page = :nextpage_startkey")//WHERE key_id = :rid
    fun updateNextPageAllkeys(nextpage_startkey: String?): Int
}