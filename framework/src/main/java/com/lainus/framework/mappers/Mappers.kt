package com.lainus.framework.mappers

import com.lainus.domain.Book
import com.lainus.framework.dto.BookDto
import com.lainus.framework.dto.BookSearchResponseDto
import com.lainus.framework.persistence.BookEntity


fun BookDto.toDomain(): Book {
    return Book(
        authors = authorName ?: listOf("Autor desconocido"),
        title = title ?: "Título desconocido",
        publicationYear = firstPublishYear?.toString() ?: "Año desconocido"
    )
}

fun BookSearchResponseDto.toModel(): List<Book> {
    return this.docs.map { it.toDomain() }
}

//Para la bd
fun Book.toEntity(): BookEntity {
    return BookEntity(
        title = this.title,
        authors = this.authors.joinToString(),
        publicationYear = this.publicationYear
    )
}

fun BookEntity.toDomain(): Book {
    return Book(
        title = this.title,
        authors = this.authors.split(",").map { it.trim() },
        publicationYear = this.publicationYear
    )
}