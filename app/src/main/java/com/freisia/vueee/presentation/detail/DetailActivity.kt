package com.freisia.vueee.presentation.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.freisia.vueee.R
import com.freisia.vueee.core.domain.usecase.MovieUseCase
import com.freisia.vueee.core.domain.usecase.TVUseCase
import com.freisia.vueee.core.presentation.model.movie.Movie
import com.freisia.vueee.core.presentation.model.movie.SearchMovie
import com.freisia.vueee.core.presentation.model.tv.SearchTV
import com.freisia.vueee.core.presentation.model.tv.TV
import com.freisia.vueee.core.utils.Constant
import com.freisia.vueee.databinding.ActivityDetailBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import kotlin.properties.Delegates

class DetailActivity : AppCompatActivity() {

    private var coroutineJob : Job? = null
    private var isFavourite by Delegates.notNull<Boolean>()
    private lateinit var binding: ActivityDetailBinding

    companion object{
        const val EXTRA_DETAIL = "extra_detail"
        const val TYPE_DETAIL = "type"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initToolbar()
        when (val type = intent.getStringExtra(TYPE_DETAIL)) {
            "movies" -> {
                remoteMovie(type)
            }
            "localmovies" -> {
                localMovie(type)
            }
            "tvshows" -> {
                remoteTV(type)
            }
            "localtvshows" -> {
                localTV(type)
            }
        }
    }

    override fun onPause() {
        coroutineJob?.cancel()
        super.onPause()
    }

    override fun onDestroy() {
        coroutineJob?.cancel()
        super.onDestroy()
    }

    private fun initToolbar(){
        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun remoteMovie(type : String){
        coroutineJob?.cancel()
        val detailViewModel:DetailViewModel<MovieUseCase, Movie> by viewModel{parametersOf(type)}
        val movies = intent.getParcelableExtra(EXTRA_DETAIL) as SearchMovie
        coroutineJob = CoroutineScope(Dispatchers.IO).launch {
            detailViewModel.getData(movies.id)
        }
        detailViewModel.isLoading.observeForever(observeLoading())
        detailViewModel.isFound.observeForever {
            if(it){
                detailViewModel.listData.observeForever{ movie ->
                    val collapsingToolbar  = binding.main
                    val appBarLayout = binding.appbar
                    appBarLayout.setExpanded(true)
                    val image = binding.image
                    val rating = binding.rating
                    val duration = binding.duration
                    val genre = binding.genre
                    val broadcast = binding.broadcast
                    val description = binding.description
                    val review = binding.review
                    Glide.with(applicationContext).load(Uri.parse(
                        Constant.BASE_IMAGE_URL +
                                movie.poster_path)).error(R.mipmap.ic_launcher_round).into(image)
                    collapsingToolbar.title = movie.title
                    var check = 0
                    for(i in movie.releases.countries){
                        if(i.iso_3166_1 == "US"){
                            rating.text = i.certification
                            break
                        }
                        check ++
                        if(check == movie.releases.countries.size - 1){
                            rating.text = "-"
                        }
                    }
                    duration.text = duration(movie.runtime)
                    var temp: String? = ""
                    for(i in 0 until movie.genres.size){
                        temp += if(i != movie.genres.size - 1){
                            movie.genres[i].name + ", "
                        } else {
                            movie.genres[i].name
                        }
                    }
                    genre.text = temp
                    broadcast.text = movie.release_date
                    description.text = movie.overview
                    val reviews = movie.vote_average * 10
                    review.text = getString(R.string.review,reviews.toString())
                    found()
                    detailViewModel.localData?.observeForever {data ->
                        if(!data.isNullOrEmpty()){
                            for(i in data.indices){
                                if(data[i].id == movie.id){
                                    isFavourite = true
                                    break
                                } else {
                                    isFavourite = false
                                }
                            }
                            if(!isFavourite){
                                binding.fab.setImageResource(R.drawable.ic_action_favorite_off)
                            } else {
                                binding.fab.setImageResource(R.drawable.ic_action_favorite_on)
                            }
                        } else {
                            isFavourite = false
                            if(!isFavourite){
                                binding.fab.setImageResource(R.drawable.ic_action_favorite_off)
                            } else {
                                binding.fab.setImageResource(R.drawable.ic_action_favorite_on)
                            }
                        }
                        binding.fab.setOnClickListener{
                            if(!isFavourite){
                                detailViewModel.insert(movie)
                                binding.fab.setImageResource(R.drawable.ic_action_favorite_on)
                            } else {
                                detailViewModel.delete(movie)
                                binding.fab.setImageResource(R.drawable.ic_action_favorite_off)
                            }
                        }
                    }
                }
            } else {
                notFound()
            }
        }
    }

    private fun remoteTV(type : String){
        coroutineJob?.cancel()
        val detailViewModel by viewModel<DetailViewModel<TVUseCase, TV>> {parametersOf(type)}
        val movie = intent.getParcelableExtra(EXTRA_DETAIL) as SearchTV
        coroutineJob = CoroutineScope(Dispatchers.IO).launch {
            detailViewModel.getData(movie.id)
        }
        detailViewModel.isLoading.observeForever(observeLoading())
        detailViewModel.isFound.observeForever {
            if(it){
                detailViewModel.listData.observeForever{ its ->
                    detailViewModel.rate.observeForever{rate ->
                        val collapsingToolbar  = binding.main
                        val appBarLayout = binding.appbar
                        appBarLayout.setExpanded(true)
                        val image = binding.image
                        val rating = binding.rating
                        val duration = binding.duration
                        val genre = binding.genre
                        val broadcast = binding.broadcast
                        val description = binding.description
                        val review = binding.review
                        Glide.with(applicationContext).load(Uri.parse(
                            Constant.BASE_IMAGE_URL +
                                    its.poster_path)).error(R.mipmap.ic_launcher_round).into(image)
                        rating.text = rate
                        collapsingToolbar.title = its.name
                        duration.text = duration(its.episode_run_time[0])
                        var temp: String? = ""
                        for(i in 0 until its.genres.size){
                            temp += if(i != its.genres.size - 1){
                                its.genres[i].name + ", "
                            } else {
                                its.genres[i].name
                            }
                        }
                        genre.text = temp
                        broadcast.text = its.first_air_date
                        description.text = its.overview
                        val reviews = its.vote_average * 10
                        review.text = getString(R.string.review,reviews.toString())
                        found()
                        detailViewModel.localData?.observeForever {data ->
                            if(!data.isNullOrEmpty()){
                                for(i in data.indices){
                                    if(data[i].id == movie.id){
                                        isFavourite = true
                                        break
                                    } else {
                                        isFavourite = false
                                    }
                                }
                                if(!isFavourite){
                                    binding.fab.setImageResource(R.drawable.ic_action_favorite_off)
                                } else {
                                    binding.fab.setImageResource(R.drawable.ic_action_favorite_on)
                                }
                            } else {
                                isFavourite = false
                                if(!isFavourite){
                                    binding.fab.setImageResource(R.drawable.ic_action_favorite_off)
                                } else {
                                    binding.fab.setImageResource(R.drawable.ic_action_favorite_on)
                                }
                            }
                            binding.fab.setOnClickListener{
                                if(!isFavourite){
                                    detailViewModel.insert(its)
                                    binding.fab.setImageResource(R.drawable.ic_action_favorite_on)
                                } else {
                                    detailViewModel.delete(its)
                                    binding.fab.setImageResource(R.drawable.ic_action_favorite_off)
                                }
                            }
                        }
                    }
                }
            } else {
                notFound()
            }
        }
    }

    private fun localMovie(type : String){
        val detailViewModel:DetailViewModel<MovieUseCase, Movie> by viewModel{parametersOf(type)}
        val movies = intent.getParcelableExtra(EXTRA_DETAIL) as Movie
        detailViewModel.isLoading.observeForever(observeLoading())
        detailViewModel.isFound.observeForever {
            val collapsingToolbar  = binding.main
            val appBarLayout = binding.appbar
            appBarLayout.setExpanded(true)
            val image = binding.image
            val rating = binding.rating
            val duration = binding.duration
            val genre = binding.genre
            val broadcast = binding.broadcast
            val description = binding.description
            val review = binding.review
            Glide.with(applicationContext).load(Uri.parse(Constant.BASE_IMAGE_URL +
                    movies.poster_path)).error(R.mipmap.ic_launcher_round).into(image)
            collapsingToolbar.title = movies.title
            var check = 0
            for(i in movies.releases.countries){
                if(i.iso_3166_1 == "US"){
                    rating.text = i.certification
                    break
                }
                check ++
                if(check == movies.releases.countries.size - 1){
                    rating.text = "-"
                }
            }
            duration.text = duration(movies.runtime)
            var temp: String? = ""
            for(i in 0 until movies.genres.size){
                temp += if(i != movies.genres.size - 1){
                    movies.genres[i].name + ", "
                } else {
                    movies.genres[i].name
                }
            }
            genre.text = temp
            broadcast.text = movies.release_date
            description.text = movies.overview
            val reviews = movies.vote_average * 10
            review.text = getString(R.string.review,reviews.toString())
            found()
            detailViewModel.localData?.observeForever {data ->
                if(!data.isNullOrEmpty()){
                    for(i in data.indices){
                        if(data[i].id == movies.id){
                            isFavourite = true
                            break
                        } else {
                            isFavourite = false
                        }
                    }
                    if(!isFavourite){
                        binding.fab.setImageResource(R.drawable.ic_action_favorite_off)
                    } else {
                        binding.fab.setImageResource(R.drawable.ic_action_favorite_on)
                    }
                } else {
                    isFavourite = false
                    if(!isFavourite){
                        binding.fab.setImageResource(R.drawable.ic_action_favorite_off)
                    } else {
                        binding.fab.setImageResource(R.drawable.ic_action_favorite_on)
                    }
                }
                binding.fab.setOnClickListener{
                    if(!isFavourite){
                        detailViewModel.insert(movies)
                        binding.fab.setImageResource(R.drawable.ic_action_favorite_on)
                    } else {
                        detailViewModel.delete(movies)
                        binding.fab.setImageResource(R.drawable.ic_action_favorite_off)
                    }
                }
            }
        }
    }

    private fun localTV(type : String){
        coroutineJob?.cancel()
        val detailViewModel by viewModel<DetailViewModel<TVUseCase, TV>> {parametersOf(type)}
        val movie = intent.getParcelableExtra(EXTRA_DETAIL) as TV
        coroutineJob = CoroutineScope(Dispatchers.IO).launch {
            detailViewModel.getData(movie.id)
        }
        detailViewModel.isLoading.observeForever(observeLoading())
        detailViewModel.isFound.observeForever {
            detailViewModel.rate.observeForever{rate ->
                val collapsingToolbar = binding.main
                val appBarLayout = binding.appbar
                appBarLayout.setExpanded(true)
                val image = binding.image
                val rating = binding.rating
                val duration = binding.duration
                val genre = binding.genre
                val broadcast = binding.broadcast
                val description = binding.description
                val review = binding.review
                Glide.with(applicationContext).load(Uri.parse(Constant.BASE_IMAGE_URL +
                        movie.poster_path)).error(R.mipmap.ic_launcher_round).into(image)
                rating.text = rate
                collapsingToolbar.title = movie.name
                duration.text = duration(movie.episode_run_time[0])
                var temp: String? = ""
                for(i in 0 until movie.genres.size){
                    temp += if(i != movie.genres.size - 1){
                        movie.genres[i].name + ", "
                    } else {
                        movie.genres[i].name
                    }
                }
                genre.text = temp
                broadcast.text = movie.first_air_date
                description.text = movie.overview
                val reviews = movie.vote_average * 10
                review.text = getString(R.string.review,reviews.toString())
                found()
                detailViewModel.localData?.observeForever {data ->
                    if(!data.isNullOrEmpty()){
                        for(i in data.indices){
                            if(data[i].id == movie.id){
                                isFavourite = true
                                break
                            } else {
                                isFavourite = false
                            }
                        }
                        if(!isFavourite){
                            binding.fab.setImageResource(R.drawable.ic_action_favorite_off)
                        } else {
                            binding.fab.setImageResource(R.drawable.ic_action_favorite_on)
                        }
                    } else {
                        isFavourite = false
                        if(!isFavourite){
                            binding.fab.setImageResource(R.drawable.ic_action_favorite_off)
                        } else {
                            binding.fab.setImageResource(R.drawable.ic_action_favorite_on)
                        }
                    }
                    binding.fab.setOnClickListener{
                        if(!isFavourite){
                            detailViewModel.insert(movie)
                            binding.fab.setImageResource(R.drawable.ic_action_favorite_on)
                        } else {
                            detailViewModel.delete(movie)
                            binding.fab.setImageResource(R.drawable.ic_action_favorite_off)
                        }
                    }
                }
            }
        }
    }

    private fun notFound() {
        binding.view1.visibility = View.GONE
        binding.view2.visibility = View.GONE
        binding.view3.visibility = View.GONE
        binding.view4.visibility = View.GONE
        binding.view5.visibility = View.GONE
        binding.rating.visibility = View.GONE
        binding.ratings.visibility = View.GONE
        binding.genre.visibility = View.GONE
        binding.genres.visibility = View.GONE
        binding.duration.visibility = View.GONE
        binding.durations.visibility = View.GONE
        binding.broadcast.visibility = View.GONE
        binding.broadcasts.visibility = View.GONE
        binding.review.visibility = View.GONE
        binding.reviews.visibility = View.GONE
        binding.description.visibility = View.GONE
        binding.descriptions.visibility = View.GONE
        binding.loadings.visibility = View.GONE
        binding.whiteViews.visibility = View.GONE
        binding.nfs.visibility = View.VISIBLE
    }

    private fun found() {
        if(binding.nfs.visibility == View.VISIBLE){
            binding.nfs.visibility = View.GONE
        }
        binding.loadings.visibility = View.GONE
        binding.whiteViews.visibility = View.GONE
    }

    private fun duration(durations: Int) : String{
        var hour = 0
        var durasi = durations
        while(durasi >= 60){
            hour++
            durasi -= 60
        }
        return if(durations >= 60){
            hour.toString() + "h "+ durasi.toString() + "m"
        } else {
            durasi.toString() + "m"
        }
    }

    private fun observeLoading() : Observer<Boolean> {
        return Observer {
            if(it){
                binding.loadings.visibility = View.VISIBLE
                binding.whiteViews.visibility = View.VISIBLE
            } else {
                binding.loadings.visibility = View.GONE
                binding.whiteViews.visibility = View.GONE
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.favorite -> {
                val uri = Uri.parse("vueee://favorite")
                val intent = Intent(Intent.ACTION_VIEW,uri)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

}
