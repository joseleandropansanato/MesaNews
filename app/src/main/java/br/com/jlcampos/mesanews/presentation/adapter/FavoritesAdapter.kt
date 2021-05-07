package br.com.jlcampos.mesanews.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import br.com.jlcampos.mesanews.R
import br.com.jlcampos.mesanews.data.model.News
import com.squareup.picasso.Picasso

class FavoritesAdapter : RecyclerView.Adapter<FavoritesAdapter.FavoritesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_favorite_news, parent, false)
        return FavoritesViewHolder(itemView)
    }

    override fun onBindViewHolder(viewHolder: FavoritesViewHolder, position: Int) {
        viewHolder.bindView(differ.currentList[position])
    }

    override fun getItemCount() = differ.currentList.size

    private val differCallback = object : DiffUtil.ItemCallback<News>() {
        override fun areItemsTheSame(oldItem: News, newItem: News): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: News, newItem: News): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    private var onItemClickListener: ((News) -> Unit)? = null

    fun setOnItemClickListener(listener: (News) -> Unit) {
        onItemClickListener = listener
    }

    inner class FavoritesViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val img = view.findViewById<ImageView>(R.id.item_favorite_news_img)
        private val title = view.findViewById<TextView>(R.id.item_favorite_news_tv_title)
        private val author = view.findViewById<TextView>(R.id.item_favorite_news_tv_author)
        private val description = view.findViewById<TextView>(R.id.item_favorite_news_tv_description)
        private val favorite = view.findViewById<ImageView>(R.id.item_favorite_news_iv_favorite)
        private val cl = view.findViewById<ConstraintLayout>(R.id.item_favorite_news_cl)

        fun bindView(news: News) {
            title.text = news.title
            author.text  = news.author
            description.text = news.description
            Picasso.get().load(news.imageUrl).error(R.drawable.ic_baseline_photo_24).into(img)
            favorite.setImageResource(if (news.favorite) R.drawable.ic_baseline_favorite_24 else R.drawable.ic_baseline_favorite_border_24)

            cl.setOnClickListener{
                onItemClickListener?.let { it(news) }
            }

        }

    }
}