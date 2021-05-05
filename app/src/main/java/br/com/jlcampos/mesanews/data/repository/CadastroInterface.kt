package br.com.jlcampos.mesanews.data.repository

import br.com.jlcampos.mesanews.data.model.Signup
import br.com.jlcampos.mesanews.utils.Constants
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface CadastroInterface {

    @FormUrlEncoded
    @POST(Constants.signup_ws)
    suspend fun signup(
        @Field(Constants.signup_name) name: String,
        @Field(Constants.signup_email) email: String,
        @Field(Constants.signup_password) password: String
    ) : Response<Signup>
}