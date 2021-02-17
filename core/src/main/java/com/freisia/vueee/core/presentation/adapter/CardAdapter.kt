package com.freisia.vueee.core.presentation.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.freisia.vueee.core.R
import com.freisia.vueee.core.presentation.model.movie.SearchMovie
import com.freisia.vueee.core.presentation.model.tv.SearchTV
import com.freisia.vueee.core.utils.Constant
import com.freisia.vueee.core.databinding.LayoutItemCardBinding

class CardAdapter<T>(movie: ArrayList<T>, recyclerView: RecyclerView) : RecyclerView.Adapter<CardAdapter.CardViewHolder<T>>() {
    private var filtered : ArrayList<T> = ArrayList()
    private var loading: Boolean = false
    lateinit var onLoadMoreListener: OnLoadMoreListener
    var onItemClick: ((T) -> Unit)? = null

    init {
        filtered = movie
        if(recyclerView.layoutManager is GridLayoutManager){
            val linearLayoutManager = recyclerView.layoutManager as GridLayoutManager
            recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener(){
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val totalItemCount = linearLayoutManager.itemCount
                    val lastVisible = linearLayoutManager.findLastCompletelyVisibleItemPosition()
                    if(!loading && totalItemCount - 1 <= lastVisible && lastVisible > filtered.size - 2){
                        onLoadMoreListener.onGridLoadMore()
                        loading = true
                    }
                }
            })
        }
    }

    inner class ViewHolder(binding: LayoutItemCardBinding) : CardViewHolder<T>(binding) {
        private val profilImage = binding.imageView
        private val profilTitle = binding.nameGrid
        private val profilReview = binding.review
        init{
            binding.root.setOnClickListener {
                onItemClick?.invoke(filtered[adapterPosition])
            }
        }
        override fun bindUser(movie: T) {
            if(movie is SearchMovie){
                val image = Uri.parse(Constant.BASE_IMAGE_URL + movie.poster_path)
                Glide.with(itemView.context).load(image).error(R.mipmap.ic_launcher_round).into(profilImage)
                profilTitle.text = movie.title
                val review = movie.vote_average * 10
                profilReview.text = itemView.context.getString(R.string.review,review.toString())
            } else if(movie is SearchTV){
                val image = Uri.parse(Constant.BASE_IMAGE_URL + movie.poster_path)
                Glide.with(itemView.context).load(image).error(R.mipmap.ic_launcher_round).into(profilImage)
                profilTitle.text = movie.name
                val review = movie.vote_average * 10
                profilReview.text = itemView.context.getString(R.string.review,review.toString())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LayoutItemCardBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = filtered.size

    override fun onBindViewHolder(holder: CardViewHolder<T>, position: Int) {
        filtered[position].let { holder.bindUser(it) }
    }

    interface OnLoadMoreListener {
        fun onGridLoadMore()
    }

    fun setLoad(){
        loading = false
    }

    fun setData(result: ArrayList<T>){
        filtered = result
        notifyDataSetChanged()
    }

    fun resetData(){
        filtered = ArrayList()
        notifyDataSetChanged()
    }

    abstract class CardViewHolder<T> (binding: LayoutItemCardBinding) : RecyclerView.ViewHolder(binding.root){
        abstract fun bindUser(movie: T)
    }

}