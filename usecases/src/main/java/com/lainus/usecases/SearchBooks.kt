package com.lainus.usecases


import com.lainus.data.BookRepository
import com.lainus.domain.Book

class SearchBooks (
    val bookRepository: BookRepository
) {
    suspend fun invoke(toSearch: String): List<Book> {
        return bookRepository.searchByQuery(toSearch)
    }
}