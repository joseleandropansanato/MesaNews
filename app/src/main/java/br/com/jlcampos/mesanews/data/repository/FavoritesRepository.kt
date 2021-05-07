package br.com.jlcampos.mesanews.data.repository

import br.com.jlcampos.mesanews.data.model.News
import br.com.jlcampos.mesanews.utils.Constants
import org.json.JSONObject

class FavoritesRepository {

    fun getFav(mFav: String): List<News> {

        val jsonFav = JSONObject(mFav)

        val list = mutableListOf<News>()

        val keyItr: Iterator<String> = jsonFav.keys()

        keyItr.forEach {
            val news: String = jsonFav[it] as String

            val jsonNews = JSONObject(news)
            val n = News(
                title = jsonNews.optString(Constants.news_data_title),
                description = jsonNews.optString(Constants.news_data_description),
                content = jsonNews.optString(Constants.news_data_content),
                author = jsonNews.optString(Constants.news_data_author),
                publishedAt = jsonNews.optString(Constants.news_data_published_at),
                highlight = jsonNews.optBoolean(Constants.news_data_highlight),
                url = jsonNews.optString(Constants.news_data_url),
                imageUrl = jsonNews.optString(Constants.news_data_image_url)
            )

            list.add(n)
        }

        return list
    }
}