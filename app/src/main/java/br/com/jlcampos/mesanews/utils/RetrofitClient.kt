package br.com.jlcampos.mesanews.utils

import br.com.jlcampos.mesanews.data.repository.FeedInterface
import br.com.jlcampos.mesanews.data.repository.LoginInterface
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitClient {
    private val retrofit: Retrofit

    init {

        val httpClient: OkHttpClient.Builder = OkHttpClient.Builder()
            .callTimeout(99999, TimeUnit.SECONDS)
            .connectTimeout(99999, TimeUnit.SECONDS)
            .readTimeout(99999, TimeUnit.SECONDS)
            .writeTimeout(99999, TimeUnit.SECONDS)
        retrofit = Retrofit.Builder()
            .baseUrl(Constants.URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient.build())
            .build()
    }

    val apiFeed: FeedInterface
        get() = retrofit.create(FeedInterface::class.java)

    val apiSignin: LoginInterface
        get() = retrofit.create(LoginInterface::class.java)

    companion object {
        private var mInstance: RetrofitClient? = null

        @get:Synchronized
        val instance: RetrofitClient?
            get() {
                if (mInstance == null) mInstance = RetrofitClient()
                return mInstance
            }
    }
}
