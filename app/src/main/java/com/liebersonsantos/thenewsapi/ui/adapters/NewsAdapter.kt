package com.liebersonsantos.thenewsapi.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.liebersonsantos.thenewsapi.R
import com.liebersonsantos.thenewsapi.data.model.Article
import kotlinx.android.synthetic.main.item_article_preview.view.*

class NewsAdapter(private val itemClickLister: ((articleClicked: Article) -> Unit)): RecyclerView.Adapter<NewsAdapter.ArticleViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_article_preview, parent, false)
        return ArticleViewHolder(itemView, itemClickLister)
    }

    override fun getItemCount() = differ.currentList.size


    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    class ArticleViewHolder(itemView: View, private val itemClickLister: (articleClicked: Article) -> Unit): RecyclerView.ViewHolder(itemView){
        private val textSource = itemView.textSource
        private val textTitle = itemView.textTitle
        private val textDescription = itemView.textDescription
        private val textPublishedAt = itemView.textPublishedAt
        private val imageArticleImage = itemView.imageArticleImage

        fun bind(article: Article){
            textSource.text = article.source?.name
            textTitle.text = article.title
            textDescription.text = article.description
            textPublishedAt.text = article.publishedAt

            Glide.with(itemView.context).load(article.urlToImage).into(imageArticleImage)

            itemView.setOnClickListener {
                itemClickLister.invoke(article)
            }
        }
    }

    val differCallback = object : DiffUtil.ItemCallback<Article>(){
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

}