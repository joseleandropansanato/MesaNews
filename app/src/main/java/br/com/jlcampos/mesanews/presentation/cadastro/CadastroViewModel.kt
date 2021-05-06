package br.com.jlcampos.mesanews.presentation.cadastro

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import br.com.jlcampos.mesanews.data.model.Signup
import br.com.jlcampos.mesanews.utils.MyResult
import br.com.jlcampos.mesanews.utils.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CadastroViewModel(application: Application) : AndroidViewModel(application) {

    val signupLiveData: MutableLiveData<MyResult<Signup?>> = MutableLiveData()

    fun pushSignup(name: String, email: String, pwd: String) {
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.Default) {
                signupLiveData.postValue(MyResult.loading(data = null))
                try {

                    val resposta = RetrofitClient
                        .instance!!
                        .apiSignup
                        .signup(name, email, pwd)

                    val myResult: MyResult<Signup?> = MyResult.success(
                            Signup(
                                code = resposta.code().toString(),
                                message = resposta.message(),
                                token = resposta.body()?.token)
                        )

                    signupLiveData.postValue(myResult)

                } catch (exception: Exception) {
                    signupLiveData.postValue(MyResult.error(data = null, message = exception.message ?: "Ooops!"))
                }
            }
        }
    }
}