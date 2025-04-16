package com.lainus.usecases

import com.lainus.data.BookRepository
import com.lainus.domain.Book


class GetMyFavoriteBooks (
    val bookRepository: BookRepository
) {
    suspend fun invoke() : List<Book> {
        return bookRepository.getFavoriteBooks()
    }
}