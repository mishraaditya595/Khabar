package com.vob.api

import com.vob.models.NewsResponse
import com.vob.util.Constants.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsAPI {

    @GET("v2/top-headlines")
    suspend fun getBreakingNews(
        @Query("country")countryCode: String = "in",
        @Query("page")pageNumber: Int = 1,
        @Query("apiKey")apiKey: String = API_KEY
    ): Response<NewsResponse>

    @GET("v2/everything")
    suspend fun searchForNews(
        @Query("q")searchQuery: String,
        @Query("page")pageNumber: Int = 1,
        @Query("apiKey")apiKey: String = API_KEY
    ): Response<NewsResponse>

    @GET("v2/top-headlines")
    suspend fun getGeneralNews(
        @Query("country") countryCode: String = "in",
        @Query("category") category: String = "general",
        @Query("page") pageNumber: Int = 1,
        @Query("apiKey") apiKey: String = API_KEY
    ): Response<NewsResponse>
}