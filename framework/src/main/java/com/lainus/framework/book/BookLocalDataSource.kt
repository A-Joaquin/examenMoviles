package com.lainus.framework.book

import android.content.Context
import com.lainus.data.book.IBookLocalDataSource
import com.lainus.domain.Book
import com.lainus.framework.mappers.toEntity
import com.lainus.framework.persistence.AppRoomDatabase
import com.lainus.framework.mappers.toDomain


class BookLocalDataSource (val context: Context) : IBookLocalDataSource {
    val bookDao = AppRoomDatabase.getDatabase(context).bookDao()
    override suspend fun saveBook(book: Book): Boolean {
        bookDao.saveBook(book.toEntity())
        return true
    }

    override suspend fun getFavoriteBooks(): List<Book> {
        return bookDao.getFavoriteBooks().map { it.toDomain()}
    }
}