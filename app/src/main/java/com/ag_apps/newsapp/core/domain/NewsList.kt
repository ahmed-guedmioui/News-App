package com.ag_apps.newsapp.core.domain


data class NewsList(
    val nextPage: String?,
    val articles: List<Article>,
)