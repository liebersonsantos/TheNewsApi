package com.liebersonsantos.thenewsapi.data.repository

import com.liebersonsantos.thenewsapi.BuildConfig
import com.liebersonsantos.thenewsapi.data.database.ArticleDB
import com.liebersonsantos.thenewsapi.data.model.Article
import com.liebersonsantos.thenewsapi.data.model.NewsResponse
import com.liebersonsantos.thenewsapi.data.network.ApiService

class NewsRepository(private val db: ArticleDB) {

    suspend fun getBreakingNews(countryCode: String, pageNumber: Int): NewsResponse =
        ApiService.service.getBreakingNews(countryCode, pageNumber, BuildConfig.API_KEY)

    suspend fun searchNews(searchQuery: String, pageNumber: Int): NewsResponse =
        ApiService.service.searchForNews(searchQuery, pageNumber, BuildConfig.API_KEY)

    suspend fun upsertArticle(article: Article) = db.articleDao().upsert(article)

    suspend fun deleteArticle(article: Article) = db.articleDao().deleteArticle(article)

    fun getSavedNews() = db.articleDao()
        .getAllArticles()  //nao precisa de um suspend fun tendo em vista que ele retorna um livedata

}