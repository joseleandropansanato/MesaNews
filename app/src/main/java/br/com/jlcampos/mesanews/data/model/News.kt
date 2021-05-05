package br.com.jlcampos.mesanews.data.model

import br.com.jlcampos.mesanews.utils.Constants
import com.google.gson.annotations.SerializedName

data class News (
    @SerializedName(Constants.news_data_title)
    var title: String,
    @SerializedName(Constants.news_data_description)
    var description: String,
    @SerializedName(Constants.news_data_content)
    var content: String,
    @SerializedName(Constants.news_data_author)
    var author: String,
    @SerializedName(Constants.news_data_published_at)
    var publishedAt: String,
    @SerializedName(Constants.news_data_highlight)
    var highlight: Boolean,
    @SerializedName(Constants.news_data_url)
    var url: String,
    @SerializedName(Constants.news_data_image_url)
    var imageUrl: String
)