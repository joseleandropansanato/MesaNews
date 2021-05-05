package br.com.jlcampos.mesanews.data.model

import br.com.jlcampos.mesanews.utils.Constants
import com.google.gson.annotations.SerializedName

data class Signin (
    @SerializedName(Constants.signin_code)
    var code: String?,
    @SerializedName(Constants.signin_message)
    var message: String?,
    @SerializedName(Constants.signin_token)
    var token: String?
)