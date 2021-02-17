package com.freisia.vueee.core.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.freisia.vueee.core.data.model.movie.MovieResponse

@Dao
interface MovieDao {
    @Query("Select * FROM Movie ORDER BY Lower(title) ASC")
    fun findAllMovie() : LiveData<List<MovieResponse>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movie: MovieResponse) : Long

    @Delete
    suspend fun delete(movie: MovieResponse)

    @Query("DELETE FROM Movie")
    suspend fun deleteAll()
}