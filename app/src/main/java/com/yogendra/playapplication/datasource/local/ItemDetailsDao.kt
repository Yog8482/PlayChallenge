package com.yogendra.playapplication.datasource.local

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yogendra.playapplication.data.Itemdetail


@Dao
interface ItemDetailsDao{

    @Query("SELECT * FROM details")
    fun getPagedDetails(): DataSource.Factory<Int, Itemdetail>

    @Query("SELECT * FROM details")
    fun getDetails(): LiveData<List<Itemdetail>>

    @Query("SELECT * FROM details WHERE id= :details_id")
    fun getArticleUser(details_id: String): LiveData<Itemdetail>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<Itemdetail>)

}