package com.lainus.framework.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BookSearchResponseDto(
    @Json(name = "docs")
    val docs: List<BookDto>
)
