package com.freisia.vueee.core.presentation.adapter

import android.annotation.SuppressLint
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.freisia.vueee.core.R
import com.freisia.vueee.core.presentation.model.movie.Movie
import com.freisia.vueee.core.presentation.model.tv.TV
import com.freisia.vueee.core.utils.Constant
import com.freisia.vueee.core.databinding.LayoutItemCardBinding

class CardFavoriteAdapter<T> :
    PagedListAdapter<T, CardFavoriteAdapter<T>.CardViewHolder>(
        object : DiffUtil.ItemCallback<T>(){
            override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
                var check = false
                if(oldItem is Movie && newItem is Movie){
                    check = oldItem.title == newItem.title && oldItem.poster_path ==
                            newItem.poster_path && oldItem.vote_average == newItem.vote_average
                } else if(oldItem is TV && newItem is TV){
                    check = oldItem.name == newItem.name && oldItem.poster_path ==
                            newItem.poster_path && oldItem.vote_average == newItem.vote_average
                }
                return check
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
                return oldItem == newItem
            }
        }
    ) {

    var onItemClick: ((T) -> Unit)? = null

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bindUser(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val binding = LayoutItemCardBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CardViewHolder(binding)
    }

    inner class CardViewHolder(binding: LayoutItemCardBinding) : RecyclerView.ViewHolder(binding.root) {
        private val profilImage = binding.imageView
        private val profilTitle = binding.nameGrid
        private val profilReview = binding.review

        init{
            binding.card.setOnClickListener {
                getItem(adapterPosition)?.let { it1 -> onItemClick?.invoke(it1) }
            }
        }

        fun bindUser(movie: T?) {
            if(movie is Movie){
                val image = Uri.parse(Constant.BASE_IMAGE_URL + movie.poster_path)
                Glide.with(itemView.context).load(image).error(R.mipmap.ic_launcher_round).into(profilImage)
                profilTitle.text = movie.title
                val review = movie.vote_average * 10
                profilReview.text = itemView.context.getString(R.string.review,review.toString())
            } else if(movie is TV){
                val image = Uri.parse(Constant.BASE_IMAGE_URL + movie.poster_path)
                Glide.with(itemView.context).load(image).error(R.mipmap.ic_launcher_round).into(profilImage)
                profilTitle.text = movie.name
                val review = movie.vote_average * 10
                profilReview.text = itemView.context.getString(R.string.review,review.toString())
            }
        }
    }
}