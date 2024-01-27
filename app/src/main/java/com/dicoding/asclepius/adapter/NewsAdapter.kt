package com.dicoding.asclepius.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.asclepius.R
import com.dicoding.asclepius.data.response.ArticlesItem
import com.dicoding.asclepius.databinding.ItemNewsBinding
import com.dicoding.asclepius.view.detailNews.DetailNewsActivity
import com.dicoding.asclepius.view.detailNews.DetailNewsActivity.Companion.AUTHOR
import com.dicoding.asclepius.view.detailNews.DetailNewsActivity.Companion.DESCRIPTION
import com.dicoding.asclepius.view.detailNews.DetailNewsActivity.Companion.IMAGE
import com.dicoding.asclepius.view.detailNews.DetailNewsActivity.Companion.PUBLISHED_AT
import com.dicoding.asclepius.view.detailNews.DetailNewsActivity.Companion.SOURCE_NAME
import com.dicoding.asclepius.view.detailNews.DetailNewsActivity.Companion.TITLE

class NewsAdapter: ListAdapter<ArticlesItem, NewsAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val news = getItem(position)
        holder.bind(news)
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailNewsActivity::class.java)
            intent.putExtra(TITLE, news.title)
            intent.putExtra(AUTHOR, news.author)
            if (news.urlToImage != null) {
                intent.putExtra(IMAGE, news.urlToImage.toString())
            } else {
                intent.putExtra(IMAGE, "null")
            }
            intent.putExtra(SOURCE_NAME, news.source.name)
            intent.putExtra(PUBLISHED_AT, news.publishedAt)
            intent.putExtra(DESCRIPTION, news.description)
            holder.itemView.context.startActivity(intent)
        }
    }
    class MyViewHolder(private val binding: ItemNewsBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(news: ArticlesItem) {
            binding.tvTitle.text = news.title
            binding.tvAuthor.text = news.author
            if (news.urlToImage != null) {
                Glide.with(itemView.context)
                    .load(news.urlToImage)
                    .into(binding.ivNews)
            } else {
                binding.ivNews.setImageResource(R.drawable.iv_placeholder)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object: DiffUtil.ItemCallback<ArticlesItem>() {
            override fun areItemsTheSame(oldItem: ArticlesItem, newItem: ArticlesItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ArticlesItem, newItem: ArticlesItem): Boolean {
                return oldItem == newItem
            }

        }
    }
}