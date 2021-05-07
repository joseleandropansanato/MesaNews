package br.com.jlcampos.mesanews.presentation.favorites

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import br.com.jlcampos.mesanews.data.model.News
import br.com.jlcampos.mesanews.data.repository.FavoritesRepository
import br.com.jlcampos.mesanews.utils.AppPrefs
import br.com.jlcampos.mesanews.utils.MyResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoritesViewModel(application: Application) : AndroidViewModel(application) {

    val favLiveData: MutableLiveData<MyResult<List<News>?>> = MutableLiveData()

    fun getFavorites(){
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.Default) {

                favLiveData.postValue(MyResult.loading(data = null))

                try {
                    val favRepo = FavoritesRepository()
                    val listFav = favRepo.getFav(AppPrefs(getApplication()).getFavorite()!!)

                    favLiveData.postValue(MyResult.success(listFav))

                } catch (exception: Exception) {
                    favLiveData.postValue(MyResult.error(data = null, message = exception.message ?: "Ooops!"))
                }
            }
        }
    }
}