package br.com.jlcampos.mesanews.data.model

import br.com.jlcampos.mesanews.utils.Constants
import com.google.gson.annotations.SerializedName

data class Signup (
    @SerializedName(Constants.signup_code)
    var code: String?,
    @SerializedName(Constants.signup_message)
    var message: String?,
    @SerializedName(Constants.signup_token)
    var token: String?
)