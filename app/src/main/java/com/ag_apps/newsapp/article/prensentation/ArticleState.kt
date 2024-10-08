package com.ag_apps.newsapp.article.prensentation

import com.ag_apps.newsapp.core.domain.Article

/**
 * @author Ahmed Guedmioui
 */
data class ArticleState(
    val article: Article? = null,
    val isLoading: Boolean = false,
    val isError: Boolean = false
)
