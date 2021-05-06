package br.com.jlcampos.mesanews.presentation.feed

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AbsListView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.jlcampos.mesanews.databinding.ActivityFeedBinding
import br.com.jlcampos.mesanews.presentation.adapter.NewsAdapter
import br.com.jlcampos.mesanews.utils.SecurityUtils
import br.com.jlcampos.mesanews.utils.Constants
import br.com.jlcampos.mesanews.utils.Status
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.imaginativeworld.whynotimagecarousel.CarouselItem
import org.imaginativeworld.whynotimagecarousel.CarouselOnScrollListener
import org.imaginativeworld.whynotimagecarousel.OnItemClickListener

class FeedActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityFeedBinding

    private lateinit var viewModel: FeedViewModel

    private var myAdapter = NewsAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(FeedViewModel::class.java)

        myUI()
        myObservers()

        getFeedNews()
        getHighlight()
    }

    override fun onClick(view: View) {
        when(view) {
            binding.feedBtLogout -> {
                SecurityUtils().logout(this@FeedActivity)
                finish()
            }
        }
    }

    private fun getFeedNews(perPage: String = "", publishedAt: String = "") {
        viewModel.getFeed(perPage, publishedAt)
    }

    private fun getHighlight() {
        viewModel.getHighlight()
    }

    private fun myUI() {

        updateListRv()

        binding.feedBtLogout.setOnClickListener(this@FeedActivity)

        myAdapter.setOnItemClickListener { news ->
            Toast.makeText(this, news.description, Toast.LENGTH_LONG).show()
        }

        myAdapter.setOnItemFavClickListener { news, positior ->
            Toast.makeText(this, news.favorite.toString(), Toast.LENGTH_SHORT).show()
            news.favorite = !news.favorite
            myAdapter.notifyItemChanged(positior)
        }

        customCarousel()
    }

    private fun myObservers() {

        viewModel.feedListLiveData.observe(this, {
            it?.let { myResource ->
                when (myResource.status) {

                    Status.SUCCESS -> {

                        hideProgBarFeed()

                        myResource.data?.let { myData ->

                            myAdapter.differ.submitList(myData.news)
                            val totalPages = myData.pagination.totalItems / Constants.QUERY_PAGE_SIZE + 2
                            isLastPage = viewModel.newPage == totalPages

                            if (isLastPage) {
                                binding.feedRvNews.setPadding(0,0,0,0)
                            }
                        }
                    }

                    Status.ERROR -> {
                        hideProgBarFeed()
                        wrong(myResource.message!!)
                    }

                    Status.LOADING -> {
                        showProgBarFeed()
                    }
                }
            }
        })

        viewModel.hihlightLiveData.observe(this, {
            it?.let { myResource ->
                when (myResource.status) {

                    Status.SUCCESS -> {
                        myResource.data?.let { data -> viewModel.getCasousel(data.highlight) }
                        hideProgBarCarousel()

                    }

                    Status.ERROR -> {
                        hideProgBarCarousel()
                        wrong(myResource.message!!)
                    }

                    Status.LOADING -> {
                        showProgBarCarousel()
                    }
                }
            }
        })

        viewModel.carouselLiveData.observe( this, {
            binding.feedIcCarousel.apply {
                addData(it)
            }
        })
    }

    private fun hideProgBarFeed() {
        binding.feedPbNews.visibility = View.GONE
        isLoading = false
    }

    private fun showProgBarFeed() {
        binding.feedPbNews.visibility = View.VISIBLE
        isLoading = true
    }

    private fun hideProgBarCarousel() {
        binding.feedPbCarousel.visibility = View.GONE
    }

    private fun showProgBarCarousel() {
        binding.feedPbCarousel.visibility = View.VISIBLE
    }

    private fun updateListRv() {
        myAdapter = NewsAdapter()
        binding.feedRvNews.apply {
            adapter = myAdapter
            layoutManager = LinearLayoutManager(this@FeedActivity)
            addOnScrollListener(scrollListener)
        }
    }

    private fun customCarousel() {
        binding.feedIcCarousel.apply {
            autoPlay = true
            autoPlayDelay = 3000
            start()

            onItemClickListener = object : OnItemClickListener {
                override fun onClick(position: Int, carouselItem: CarouselItem) {
                    Toast.makeText(this@FeedActivity, carouselItem.caption, Toast.LENGTH_LONG)
                        .show()
                }

                override fun onLongClick(position: Int, dataObject: CarouselItem) {
                }
            }

            onScrollListener = object : CarouselOnScrollListener {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int, position: Int, carouselItem: CarouselItem?) {

                    super.onScrollStateChanged(recyclerView, newState, position, carouselItem)

                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                    val visibleItemCount = layoutManager.childCount
                    val totalItemCount = layoutManager.itemCount

                    val isAtLastItem = firstVisibleItemPosition + visibleItemCount > totalItemCount
                    val isNotAtBeginning = firstVisibleItemPosition >= -1
                    val shouldPaginate = isAtLastItem && isNotAtBeginning

                    if (shouldPaginate) {
                        stop()

                        CoroutineScope(Dispatchers.IO).launch {

                            currentPosition = -1
                            delay(1000)
                            binding.feedIcCarousel.start()
                        }
                    }

                }
            }
        }
    }

    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= Constants.QUERY_PAGE_SIZE
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning && isTotalMoreThanVisible && isScrolling

            if (shouldPaginate) {
                getFeedNews()
                isScrolling = false
            }

        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                isScrolling = true
            }
        }
    }

    private fun wrong(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }
}