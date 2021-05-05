package br.com.jlcampos.mesanews.data.model

import br.com.jlcampos.mesanews.utils.Constants
import com.google.gson.annotations.SerializedName

data class Feed (
    @SerializedName(Constants.news_pagination)
    var pagination: Pagination,
    @SerializedName(Constants.news_data)
    var news: List<News>
)