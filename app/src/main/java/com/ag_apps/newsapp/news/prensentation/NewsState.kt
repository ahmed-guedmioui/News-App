package com.ag_apps.newsapp.news.prensentation

import com.ag_apps.newsapp.core.domain.Article

/**
 * @author Ahmed Guedmioui
 */
data class NewsState(
    val articleList: List<Article> = emptyList(),
    val nextPage: String? = null,
    val isLoading: Boolean = false,
    val isError: Boolean = false,
)
