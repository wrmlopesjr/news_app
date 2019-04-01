package dev.wilsonjr.newsapp.api.repository

import dev.wilsonjr.newsapp.api.model.ArticlesResponse
import dev.wilsonjr.newsapp.api.model.SourceResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsEndpoint {

    @GET("/v2/sources")
    fun getSources(@Query("country") country: String?, @Query("category") category: String?): Single<SourceResponse>

    @GET("/v2/everything")
    fun getTopHeadlines(@Query("sources") sources: String?, @Query("page") page: Int, @Query("pageSize") pageSize: Int ): Single<ArticlesResponse>

}