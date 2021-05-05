package br.com.jlcampos.mesanews.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
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
        viewHolder.bindView(differ.currentList[position])
    }

    private val differCallbak = object : DiffUtil.ItemCallback<News>(){
        override fun areItemsTheSame(oldItem: News, newItem: News): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: News, newItem: News): Boolean {
            return  oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallbak)

    private var onItemClickListener: ((News) -> Unit)? = null

    fun setOnItemClickListener(listener: (News) -> Unit) {
        onItemClickListener = listener
    }

    override fun getItemCount() = differ.currentList.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val img = view.findViewById<ImageView>(R.id.item_news_img)
        private val title = view.findViewById<TextView>(R.id.item_news_tv_title)
        private val author = view.findViewById<TextView>(R.id.item_news_tv_author)
        private val description = view.findViewById<TextView>(R.id.item_news_tv_description)
        private val favorite = view.findViewById<ImageView>(R.id.item_news_iv_favorite)

        fun bindView(news: News) {
            title.text = news.title
            author.text  = news.author
            description.text = news.description
            Picasso.get().load(news.imageUrl).error(R.drawable.ic_launcher_foreground).into(img)

            itemView.setOnClickListener{
                onItemClickListener?.let { it(news) }
            }

        }

    }

}