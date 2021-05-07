package br.com.jlcampos.mesanews.presentation.favorites

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.jlcampos.mesanews.databinding.ActivityFavoritesBinding
import br.com.jlcampos.mesanews.presentation.adapter.FavoritesAdapter
import br.com.jlcampos.mesanews.utils.Status

class FavoritesActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityFavoritesBinding
    private lateinit var viewModel: FavoritesViewModel

    private var myAdapter = FavoritesAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityFavoritesBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(FavoritesViewModel::class.java)

        myUI()
        myObservers()

        viewModel.getFavorites()
    }

    override fun onClick(view: View) {
        when (view) {
            binding.favoritesBtBack -> {
                onBackPressed()
            }
        }
    }

    private fun myUI() {
        updateListFav()

        binding.favoritesBtBack.setOnClickListener(this)

        myAdapter.setOnItemClickListener { news ->
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(news.url)
            startActivity(i)
        }
    }

    private fun myObservers() {
        viewModel.favLiveData.observe(this, {
            it?.let { mResource ->
                when (mResource.status) {
                    Status.SUCCESS -> {
                        hideProgBar()

                        mResource.data?.let { mData ->

                            myAdapter.differ.submitList(mData)
                        }
                    }
                    Status.ERROR -> {
                        hideProgBar()
                        wrong(mResource.message)
                    }
                    Status.LOADING -> {
                        showProgBar()
                    }
                }
            }
        })
    }

    private fun wrong(msg: String?) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()

    }

    private fun showProgBar() {
        binding.favoritesPb.visibility = View.VISIBLE
    }

    private fun hideProgBar() {
        binding.favoritesPb.visibility = View.GONE
    }

    private fun updateListFav() {
        myAdapter = FavoritesAdapter()
        binding.favoritesRv.apply {
            adapter = myAdapter
            layoutManager = LinearLayoutManager(this@FavoritesActivity)
        }
    }
}