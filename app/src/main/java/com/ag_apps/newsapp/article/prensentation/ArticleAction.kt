package com.ag_apps.newsapp.article.prensentation

/**
 * @author Ahmed Guedmioui
 */
sealed interface ArticleAction {
    data class LoadArticle(val articleId: String): ArticleAction
}