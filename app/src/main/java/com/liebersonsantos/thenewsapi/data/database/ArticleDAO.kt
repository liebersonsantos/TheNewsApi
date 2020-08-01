package com.liebersonsantos.thenewsapi.data.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.liebersonsantos.thenewsapi.data.model.Article

@Dao
interface ArticleDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(article: Article)

    @Query("SELECT * FROM articles")
    fun getAllArticles(): LiveData<List<Article>>

    @Delete
    suspend fun deleteArticle(article: Article)
}