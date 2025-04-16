package com.lainus.framework.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BookDto(
    @Json(name = "title")
    val title: String?,
    @Json(name = "author_name")
    val authorName: List<String>?,
    @Json(name = "first_publish_year")
    val firstPublishYear: Int?
)