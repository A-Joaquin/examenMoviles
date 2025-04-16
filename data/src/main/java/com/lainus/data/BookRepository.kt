package com.lainus.data

import com.lainus.data.book.IBookLocalDataSource
import com.lainus.data.book.IBookRemoteDataSource
import com.lainus.domain.Book

class BookRepository (
    val bookRemoteDataSource: IBookRemoteDataSource,
    val bookLocalDataSource: IBookLocalDataSource
) {
    suspend fun searchByQuery(query: String): List<Book> {
        return bookRemoteDataSource.searchByQuery(query)
    }

    suspend fun saveBook(book: Book): Boolean {
        bookLocalDataSource.saveBook(book)
        return true
    }

    suspend fun getFavoriteBooks(): List<Book> {
        return bookLocalDataSource.getFavoriteBooks()
    }
}