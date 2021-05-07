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

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bindView(differ.currentList[position], position)
    }

    private val differCallback = object : DiffUtil.ItemCallback<News>(){
        override fun areItemsTheSame(oldItem: News, newItem: News): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: News, newItem: News): Boolean {
            return  oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    private var onItemClickListener: ((News) -> Unit)? = null

    private var onItemFavClickListener: ((News, Int) -> Unit)? = null

    fun setOnItemClickListener(listener: (News) -> Unit) {
        onItemClickListener = listener
    }

    fun setOnItemFavClickListener(listener: (News, Int) -> Unit) {
        onItemFavClickListener = listener
    }

    override fun getItemCount() = differ.currentList.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val img = view.findViewById<ImageView>(R.id.item_news_img)
        private val title = view.findViewById<TextView>(R.id.item_news_tv_title)
        private val author = view.findViewById<TextView>(R.id.item_news_tv_author)
        private val description = view.findViewById<TextView>(R.id.item_news_tv_description)
        private val favorite = view.findViewById<ImageView>(R.id.item_news_iv_favorite)
        private val cl = view.findViewById<ConstraintLayout>(R.id.item_news_cl)

        fun bindView(news: News, position: Int) {
            title.text = news.title
            author.text  = news.author
            description.text = news.description
            Picasso.get().load(news.imageUrl).error(R.drawable.ic_baseline_photo_24).into(img)
            favorite.setImageResource(if (news.favorite) R.drawable.ic_baseline_favorite_24 else R.drawable.ic_baseline_favorite_border_24)

            cl.setOnClickListener{
                onItemClickListener?.let { it(news) }
            }

            favorite.setOnClickListener{
                onItemFavClickListener?.let { it(news, position) }
            }

        }

    }

}