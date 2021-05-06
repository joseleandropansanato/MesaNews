package br.com.jlcampos.mesanews.presentation.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import br.com.jlcampos.mesanews.data.model.Signin
import br.com.jlcampos.mesanews.utils.AppPrefs
import br.com.jlcampos.mesanews.utils.MyResult
import br.com.jlcampos.mesanews.utils.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    val signinLiveData: MutableLiveData<MyResult<Signin?>> = MutableLiveData()

    val loggedLiveData: MutableLiveData<MyResult<Boolean>> = MutableLiveData()

    fun getSignin(user: String, pwd: String) {

        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.Default) {

                signinLiveData.postValue(MyResult.loading(data = null))

                try {

                    val resposta = RetrofitClient
                        .instance!!
                        .apiSignin
                        .signin(user, pwd)

                    val myResult: MyResult<Signin?> = MyResult.success(Signin(
                            code = resposta.code().toString(),
                            message = resposta.message(),
                            token = resposta.body()?.token))

                    signinLiveData.postValue(myResult)

                } catch (exception: Exception) {
                    signinLiveData.postValue(MyResult.error(data = null, message = exception.message ?: "Ooops!"))
                }
            }
        }
    }

    fun tratarLogin(data: String?) {

        loggedLiveData.postValue(MyResult.loading(data = null))
        try {

            val session = AppPrefs(getApplication())

            session.setToken(data)
            session.setLogged(session.getToken() != null)

            if (session.isLogged()) {

                loggedLiveData.postValue(MyResult.success(session.isLogged()))

            } else {

                loggedLiveData.postValue(
                    MyResult.error(
                        data = session.isLogged(),
                        message = "Algum problema ao logar"
                    )
                )
            }

        } catch (exception: Exception) {
            loggedLiveData.postValue(MyResult.error(data = false, message = "Algum problema ao logar"))
        }
    }
}