package br.com.jlcampos.mesanews.data.repository

import br.com.jlcampos.mesanews.data.model.Signin
import br.com.jlcampos.mesanews.utils.Constants
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface LoginInterface {

    @FormUrlEncoded
    @POST(Constants.signin_ws)
    suspend fun signin(
        @Field(Constants.signin_email) email: String,
        @Field(Constants.signin_password) password: String
    ) : Response<Signin?>

}