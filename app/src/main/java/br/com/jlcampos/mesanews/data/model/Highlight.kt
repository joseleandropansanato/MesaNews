package br.com.jlcampos.mesanews.data.model

import br.com.jlcampos.mesanews.utils.Constants
import com.google.gson.annotations.SerializedName

data class Highlight (
    @SerializedName(Constants.hl_data)
    var highlight: List<News>
)