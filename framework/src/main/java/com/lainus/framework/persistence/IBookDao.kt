package com.lainus.framework.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface IBookDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE) // para repes
    suspend fun saveBook(book: BookEntity)

    @Query("SELECT * FROM books")
    suspend fun getFavoriteBooks(): List<BookEntity>
}