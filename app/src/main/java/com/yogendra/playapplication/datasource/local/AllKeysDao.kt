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

    @Query("SELECT * FROM allkeys LIMIT :pagesize OFFSET :startkeyid")
    fun getNextBatchOfKeys(pagesize: Int, startkeyid: String): LiveData<List<Allkeys>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<Allkeys>)
}