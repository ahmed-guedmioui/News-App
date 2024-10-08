package com.ag_apps.newsapp.core.data

import com.ag_apps.newsapp.core.data.local.ArticlesDao
import com.ag_apps.newsapp.core.data.remote.NewsListDto
import com.ag_apps.newsapp.core.domain.Article
import com.ag_apps.newsapp.core.domain.NewsList
import com.ag_apps.newsapp.core.domain.NewsRepository
import com.ag_apps.newsapp.core.domain.NewsResult
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.coroutines.cancellation.CancellationException

/**
 * @author Ahmed Guedmioui
 */
class NewsRepositoryImpl(
    private val httpClient: HttpClient,
    private val dao: ArticlesDao
) : NewsRepository {

    private val tag = "NewsRepository: "

    private suspend fun getLocalNews(nextPage: String?): NewsList {
        val localNews = dao.getArticleList()
        println(tag + "getLocalNews: " + localNews.size + " nextPage: " + nextPage)

        val newsList = NewsList(
            nextPage = nextPage,
            articles = localNews.map { it.toArticle() }
        )

        return newsList
    }

    private suspend fun getRemoteNews(nextPage: String?): NewsList {
        val newsListDto: NewsListDto = httpClient.get(baseUrl) {
            parameter("apikey", apiKey)
            parameter("language", "en")
            if (nextPage != null) parameter("page", nextPage)
        }.body()

        println(tag + "getRemoteNews: " + newsListDto.results?.size + " nextPage: " + nextPage)

        return newsListDto.toNewsList()
    }

    override suspend fun getNews(): Flow<NewsResult<NewsList>> {
        return flow {
            val remoteNewsList = try {
                getRemoteNews(null)
            } catch (e: Exception) {
                e.printStackTrace()
                if (e is CancellationException) throw e
                println(tag + "getNews remote exception: " + e.message)
                null
            }

            remoteNewsList?.let {
                dao.clearDatabase()
                dao.upsertArticleList(remoteNewsList.articles.map { it.toArticleEntity() })
                emit(NewsResult.Success(getLocalNews(remoteNewsList.nextPage)))
                return@flow
            }

            val localNewsList = getLocalNews(null)
            if (localNewsList.articles.isNotEmpty()) {
                emit(NewsResult.Success(localNewsList))
                return@flow
            }

            emit(NewsResult.Error("No Data"))

        }
    }

    override suspend fun paginate(nextPage: String?): Flow<NewsResult<NewsList>> {
        return flow {
            val remoteNewsList = try {
                getRemoteNews(nextPage)
            } catch (e: Exception) {
                e.printStackTrace()
                if (e is CancellationException) throw e
                println(tag + "paginate remote exception: " + e.message)
                null
            }

            remoteNewsList?.let {
                dao.upsertArticleList(remoteNewsList.articles.map { it.toArticleEntity() })

                // not getting them from the database like getNews()
                // because we will also get old items that we already have before paginating
                emit(NewsResult.Success(remoteNewsList))
                return@flow
            }
        }
    }

    override suspend fun getArticle(articleId: String): Flow<NewsResult<Article>> {
        return flow {
            dao.getArticle(articleId)?.let { article ->
                println(tag + "get local article " + article.articleId)
                emit(NewsResult.Success(article.toArticle()))
                return@flow
            }

            try {
                val remoteArticle: NewsListDto = httpClient.get(baseUrl) {
                    parameter("apikey", apiKey)
                    parameter("id", articleId)
                }.body()

                println(tag + "get article remote " + remoteArticle.results?.size)

                if (remoteArticle.results?.isNotEmpty() == true) {
                    emit(NewsResult.Success(remoteArticle.results[0].toArticle()))
                } else {
                    emit(NewsResult.Error("Can't Load Article"))
                }

            } catch (e: Exception) {
                e.printStackTrace()
                if (e is CancellationException) throw e
                println(tag + "get article remote exception: " + e.message)
                emit(NewsResult.Error("Can't Load Article"))
            }
        }

    }



























    private val baseUrl = "https://newsdata.io/api/1/latest"
    private val apiKey = "pub_552786af29afa282f2a74a8b9d86df0fa8561"
}