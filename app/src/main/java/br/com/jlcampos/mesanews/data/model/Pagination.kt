package br.com.jlcampos.mesanews.data.model

import br.com.jlcampos.mesanews.utils.Constants
import com.google.gson.annotations.SerializedName

data class Pagination (
        @SerializedName(Constants.news_pagination_current_page)
        var currentPage: Int,
        @SerializedName(Constants.news_pagination_per_page)
        var perPage: Int,
        @SerializedName(Constants.news_pagination_total_pages)
        var totalPages: Int,
        @SerializedName(Constants.news_pagination_total_items)
        var totalItems: Int
)