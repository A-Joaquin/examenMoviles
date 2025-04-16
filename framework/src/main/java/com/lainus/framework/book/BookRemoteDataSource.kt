package com.lainus.framework.book

import com.lainus.data.book.IBookRemoteDataSource
import com.lainus.domain.Book


class BookRemoteDataSource (
    val retrofitServise: RetrofitClient
) : IBookRemoteDataSource {
    override suspend fun searchByQuery(query: String) : List<Book> {
        return retrofitServise.apiService.searchBooks(query).toModel()
    }
}