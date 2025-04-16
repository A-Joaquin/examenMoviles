package com.lainus.data.book

import com.lainus.domain.Book

interface IBookRemoteDataSource {
    suspend fun searchByQuery(query: String) : List<Book>
}