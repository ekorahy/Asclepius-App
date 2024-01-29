package com.dicoding.asclepius.data.remote.response

import com.google.gson.annotations.SerializedName

data class NewsResponse(

    @field:SerializedName("articles")
    val articles: List<ArticlesItem>,
)

data class Source(

    @field:SerializedName("name")
    val name: String,
)

data class ArticlesItem(

    @field:SerializedName("publishedAt")
    val publishedAt: String,

    @field:SerializedName("author")
    val author: String,

    @field:SerializedName("urlToImage")
    val urlToImage: Any,

    @field:SerializedName("description")
    val description: String,

    @field:SerializedName("source")
    val source: Source,

    @field:SerializedName("title")
    val title: String,
)