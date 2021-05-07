package br.com.jlcampos.mesanews.presentation.feed

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import br.com.jlcampos.mesanews.data.model.Feed
import br.com.jlcampos.mesanews.data.model.Highlight
import br.com.jlcampos.mesanews.data.model.News
import br.com.jlcampos.mesanews.utils.AppPrefs
import br.com.jlcampos.mesanews.utils.Constants
import br.com.jlcampos.mesanews.utils.MyResult
import br.com.jlcampos.mesanews.utils.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.imaginativeworld.whynotimagecarousel.CarouselItem
import org.json.JSONObject
import org.json.JSONStringer

class FeedViewModel(application: Application) : AndroidViewModel(application) {

    val feedListLiveData: MutableLiveData<MyResult<Feed?>> = MutableLiveData()
    var newPage = 1
    private var feedResponse: MyResult<Feed?>? = null

    val hihlightLiveData: MutableLiveData<MyResult<Highlight?>> = MutableLiveData()
    val carouselLiveData: MutableLiveData<List<CarouselItem>> = MutableLiveData()

    fun getFeed(perPage: String, publishedAt: String) {

        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.Default) {

                feedListLiveData.postValue(MyResult.loading(data = null))

                try {

                    val resposta = RetrofitClient
                        .instance!!
                        .apiFeed
                        .getNews(newPage.toString(), perPage, publishedAt,Constants.BEARER + AppPrefs(getApplication()).getToken()!!).body()

                    val myResult = MyResult.success(resposta)

                    handleFeed(myResult)

                } catch (exception: Exception) {
                    feedListLiveData.postValue(MyResult.error(data = null, message = exception.message ?: "Ooops!"))
                }
            }
        }
    }

    private fun handleFeed(myResult: MyResult<Feed?>) {
        newPage++

        val session = AppPrefs(getApplication())
        val myFavorites = JSONObject(session.getFavorite()!!)

        if (feedResponse == null) {

            val newList = myResult.data?.news?.toMutableList()

            if (newList != null) {
                for(i in 0 until newList.size)
                    newList[i].favorite = myFavorites.has(newList[i].title)
            }

            myResult.data?.news = newList!!

            feedResponse = myResult
        } else {
            val oldList = feedResponse?.data?.news?.toMutableList()
            val newList = myResult.data?.news?.toMutableList()

            if (newList != null) {
                for(i in 0 until newList.size)
                    newList[i].favorite = myFavorites.has(newList[i].title)
            }

            oldList?.addAll(newList!!)
            feedResponse?.data?.news = oldList!!
        }

        feedListLiveData.postValue(feedResponse ?: myResult)
    }

    fun getHighlight(){
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.Default) {

                hihlightLiveData.postValue(MyResult.loading(data = null))

                try {

                    val resposta = RetrofitClient
                        .instance!!
                        .apiFeed
                        .getHighlights(Constants.BEARER + AppPrefs(getApplication()).getToken()!!).body()

                    val myResult = MyResult.success(resposta)

                    hihlightLiveData.postValue(myResult)

                } catch (exceptio: Exception) {
                    hihlightLiveData.postValue(MyResult.error(data = null, message = exceptio.message ?: "Ooops!"))
                }
            }
        }
    }

    fun getCasousel(list: List<News>) {
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.Default) {
                val carouselList = mutableListOf<CarouselItem>()

                list.forEach {
                    carouselList.add(
                        CarouselItem(
                            imageUrl = it.imageUrl,
                            caption = it.title
                        )
                    )
                }
                carouselLiveData.postValue(carouselList)
            }
        }
    }

    fun addFavorite(news: News) {
        val session = AppPrefs(getApplication())
        val myFavorites = JSONObject(session.getFavorite()!!)

        val jsonNews = JSONStringer()
            .`object`()
            .key(Constants.news_data_title)
            .value(news.title)
            .key(Constants.news_data_description)
            .value(news.description)
            .key(Constants.news_data_content)
            .value(news.content)
            .key(Constants.news_data_author)
            .value(news.author)
            .key(Constants.news_data_published_at)
            .value(news.publishedAt)
            .key(Constants.news_data_highlight)
            .value(news.highlight)
            .key(Constants.news_data_url)
            .value(news.url)
            .key(Constants.news_data_image_url)
            .value(news.imageUrl)
            .endObject()

        myFavorites.put(
            /*key*/news.title,
            /*value*/jsonNews
        )

        session.setFavorite(myFavorites.toString())

    }

    fun removeFavorite(news: News) {
        val session = AppPrefs(getApplication())
        val myFavorites = JSONObject(session.getFavorite()!!)

        myFavorites.remove(news.title)

        session.setFavorite(myFavorites.toString())
    }

}