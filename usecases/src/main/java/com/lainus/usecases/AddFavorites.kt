package com.lainus.usecases

import com.lainus.data.BookRepository
import com.lainus.domain.Book


class AddFavorites (
    val bookRepository: BookRepository
){
    suspend fun invoke(book: Book) {
        bookRepository.saveBook(book)
    }
}