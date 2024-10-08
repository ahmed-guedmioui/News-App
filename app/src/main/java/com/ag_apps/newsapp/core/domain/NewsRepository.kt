package com.ag_apps.newsapp.core.domain

import kotlinx.coroutines.flow.Flow


/**
 * @author Ahmed Guedmioui
 */
interface NewsRepository {

    suspend fun getNews(): Flow<NewsResult<NewsList>>
    suspend fun paginate(nextPage: String?): Flow<NewsResult<NewsList>>
//    suspend fun getArticle(articleId: String): Flow<NewsResult<Article>>

}