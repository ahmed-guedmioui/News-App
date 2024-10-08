package com.ag_apps.newsapp.news.prensentation

/**
 * @author Ahmed Guedmioui
 */
sealed interface NewsAction {
    data object Paginate: NewsAction
}