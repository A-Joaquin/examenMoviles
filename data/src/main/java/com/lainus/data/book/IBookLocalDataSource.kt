package com.lainus.data.book

import com.lainus.domain.Book

interface IBookLocalDataSource {
    suspend fun saveBook(book: Book): Boolean
    suspend fun getFavoriteBooks(): List<Book>
}