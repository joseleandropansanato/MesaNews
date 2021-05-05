package br.com.jlcampos.mesanews.data.repository

import br.com.jlcampos.mesanews.data.model.Feed
import br.com.jlcampos.mesanews.data.model.Highlight
import br.com.jlcampos.mesanews.utils.Constants
import retrofit2.Response
import retrofit2.http.*

interface FeedInterface {

    @GET(Constants.news_ws)
    suspend fun getNews(
        @Query(Constants.news_current_page) currentPage : String,
        @Query(Constants.news_per_page) perPage : String,
        @Query(Constants.news_published_at) publishedAt : String,
        @Header(Constants.AUTHORIZATION) token: String
    ) : Response<Feed?>

    @GET(Constants.hl_ws)
    suspend fun getHighlights(
        @Header(Constants.AUTHORIZATION) token: String
    ) : Response<Highlight?>

}