package com.ag_apps.newsapp.core.prensentation

/**
 * @author Ahmed Guedmioui
 */
sealed interface Screen {

    @kotlinx.serialization.Serializable
    data object News : Screen

    @kotlinx.serialization.Serializable
    data class Article(val articleId: String) : Screen

}