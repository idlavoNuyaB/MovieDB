package com.freisia.vueee.core.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.freisia.vueee.core.data.model.tv.TVResponse

@Dao
interface TVDao {
    @Query("Select * FROM TV ORDER BY Lower(name) ASC")
    fun findAllTV() : LiveData<List<TVResponse>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(tv: TVResponse) : Long

    @Delete
    suspend fun delete(tv: TVResponse)

    @Query("DELETE FROM TV")
    suspend fun deleteAll()
}