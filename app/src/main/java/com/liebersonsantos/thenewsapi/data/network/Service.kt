package com.liebersonsantos.thenewsapi.data.network

import com.liebersonsantos.thenewsapi.data.model.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface Service {

    @GET("v2/top-headlines")
    suspend fun getBreakingNews(
        @Query("country") country: String,
        @Query("page") pageNumber: Int,
        @Query("apiKey") apiKey: String
    ): NewsResponse

    @GET("v2/everything")
    suspend fun searchForNews(
        @Query("q") searchQuery: String,
        @Query("page") pageNumber: Int,
        @Query("apiKey") apiKey: String
    ): NewsResponse
}