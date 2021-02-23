package com.freisia.vueee.presentation.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
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
import com.google.android.material.appbar.AppBarLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import kotlin.math.abs
import kotlin.properties.Delegates

class DetailActivity : AppCompatActivity(), AppBarLayout.OnOffsetChangedListener {

    private var coroutineJob : Job? = null
    private var isFavourite by Delegates.notNull<Boolean>()
    private lateinit var binding: ActivityDetailBinding
    private lateinit var tempMovie : Movie
    private lateinit var tempTV : TV
    private var isFound = false
    private var maxScrollSize = 0
    private val percentageShowImage = 20
    private var mIsImageHidden = false

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
        when (val type = intent.getStringExtra(TYPE_DETAIL)) {
            "movies" -> {
                val detailViewModel:DetailViewModel<MovieUseCase, Movie> by viewModel{parametersOf(type)}
                val movies = intent.getParcelableExtra(EXTRA_DETAIL) as SearchMovie
                detailViewModel.isLoading.removeObserver(observeLoading())
                detailViewModel.isFound.removeObserver(observeFound())
                detailViewModel.listData.removeObserver(observeRemoteDataMovie())
                detailViewModel.localData?.removeObserver(observeLocalDataMovie(movies.id,detailViewModel))
            }
            "localmovies" -> {
                val detailViewModel:DetailViewModel<MovieUseCase, Movie> by viewModel{parametersOf(type)}
                val movies = intent.getParcelableExtra(EXTRA_DETAIL) as Movie
                detailViewModel.isLoading.removeObserver(observeLoading())
                detailViewModel.isFound.removeObserver(observeFound())
                detailViewModel.localData?.removeObserver(observeLocalDataMovie(movies.id,detailViewModel))
            }
            "tvshows" -> {
                val detailViewModel by viewModel<DetailViewModel<TVUseCase, TV>> {parametersOf(type)}
                val movie = intent.getParcelableExtra(EXTRA_DETAIL) as SearchTV
                detailViewModel.isLoading.removeObserver(observeLoading())
                detailViewModel.isFound.removeObserver(observeFound())
                detailViewModel.listData.removeObserver(observeRemoteDataTV())
                detailViewModel.rate.removeObserver(observeRateTV(type,binding.rating))
                detailViewModel.localData?.removeObserver(observeLocalDataTV(movie.id,detailViewModel))
            }
            "localtvshows" -> {
                val detailViewModel by viewModel<DetailViewModel<TVUseCase, TV>> {parametersOf(type)}
                val movie = intent.getParcelableExtra(EXTRA_DETAIL) as TV
                detailViewModel.isLoading.removeObserver(observeLoading())
                detailViewModel.isFound.removeObserver(observeFound())
                detailViewModel.localData?.removeObserver(observeLocalDataTV(movie.id,detailViewModel))
                detailViewModel.rate.removeObserver(observeRateTV(type,binding.rating))
            }
        }
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

    private fun remoteMovie(type: String){
        coroutineJob?.cancel()
        val detailViewModel:DetailViewModel<MovieUseCase, Movie> by viewModel{parametersOf(type)}
        val movies = intent.getParcelableExtra(EXTRA_DETAIL) as SearchMovie
        coroutineJob = CoroutineScope(Dispatchers.IO).launch {
            detailViewModel.getData(movies.id)
        }
        detailViewModel.isLoading.observe(this,observeLoading())
        detailViewModel.isFound.observe(this,observeFound())
        detailViewModel.listData.observe(this,observeRemoteDataMovie())
        detailViewModel.localData?.observe(this,observeLocalDataMovie(movies.id,detailViewModel))
    }

    private fun remoteTV(type: String){
        coroutineJob?.cancel()
        val detailViewModel by viewModel<DetailViewModel<TVUseCase, TV>> {parametersOf(type)}
        val movie = intent.getParcelableExtra(EXTRA_DETAIL) as SearchTV
        coroutineJob = CoroutineScope(Dispatchers.IO).launch {
            detailViewModel.getData(movie.id)
        }
        detailViewModel.isLoading.observe(this,observeLoading())
        detailViewModel.isFound.observe(this,observeFound())
        detailViewModel.listData.observe(this,observeRemoteDataTV())
        detailViewModel.rate.observe(this,observeRateTV(type,binding.rating))
        detailViewModel.localData?.observe(this,observeLocalDataTV(movie.id,detailViewModel))
    }

    private fun localMovie(type: String){
        val detailViewModel:DetailViewModel<MovieUseCase, Movie> by viewModel{parametersOf(type)}
        val movies = intent.getParcelableExtra(EXTRA_DETAIL) as Movie
        detailViewModel.isLoading.observeForever(observeLoading())
        detailViewModel.isFound.observeForever(observeFound())
        if(isFound){
            val collapsingToolbar  = binding.main
            val appBarLayout = binding.appbar
            appBarLayout.setExpanded(true)
            appBarLayout.addOnOffsetChangedListener(this)
            val image = binding.image
            val rating = binding.rating
            val duration = binding.duration
            val genre = binding.genre
            val broadcast = binding.broadcast
            val description = binding.description
            val review = binding.review
            Glide.with(applicationContext).load(
                Uri.parse(
                    Constant.BASE_IMAGE_URL +
                            movies.poster_path
                )
            ).error(R.mipmap.ic_launcher_round).into(image)
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
            review.text = getString(R.string.review, reviews.toString())
            found()
            tempMovie = movies
        } else {
            notFound()
        }
        detailViewModel.localData?.observe(this,observeLocalDataMovie(movies.id,detailViewModel))
    }

    private fun localTV(type: String){
        coroutineJob?.cancel()
        val detailViewModel by viewModel<DetailViewModel<TVUseCase, TV>> {parametersOf(type)}
        val movie = intent.getParcelableExtra(EXTRA_DETAIL) as TV
        coroutineJob = CoroutineScope(Dispatchers.IO).launch {
            detailViewModel.getData(movie.id)
        }
        detailViewModel.isLoading.observeForever(observeLoading())
        detailViewModel.isFound.observeForever(observeFound())
        if(isFound){
            val collapsingToolbar = binding.main
            val appBarLayout = binding.appbar
            appBarLayout.setExpanded(true)
            appBarLayout.addOnOffsetChangedListener(this)
            val image = binding.image
            val duration = binding.duration
            val genre = binding.genre
            val broadcast = binding.broadcast
            val description = binding.description
            val review = binding.review
            Glide.with(applicationContext).load(
                Uri.parse(
                    Constant.BASE_IMAGE_URL +
                            movie.poster_path
                )
            ).error(R.mipmap.ic_launcher_round).into(image)
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
            review.text = getString(R.string.review, reviews.toString())
            tempTV = movie
            detailViewModel.rate.observe(this,observeRateTV(type,binding.rating))
        } else {
            notFound()
        }
        detailViewModel.localData?.observe(this,observeLocalDataTV(movie.id,detailViewModel))
    }

    private fun checkDataMovie(
        data: List<Movie>,
        id: Int,
        detailViewModel: DetailViewModel<MovieUseCase, Movie>
    ){
        if(!data.isNullOrEmpty()){
            for(i in data.indices){
                if(data[i].id == id){
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
                detailViewModel.insert(tempMovie)
                binding.fab.setImageResource(R.drawable.ic_action_favorite_on)
            } else {
                detailViewModel.delete(tempMovie)
                binding.fab.setImageResource(R.drawable.ic_action_favorite_off)
            }
        }
    }

    private fun checkDataTV(
        data: List<TV>,
        id: Int,
        detailViewModel: DetailViewModel<TVUseCase, TV>
    ){
        if(!data.isNullOrEmpty()){
            for(i in data.indices){
                if(data[i].id == id){
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
                detailViewModel.insert(tempTV)
                binding.fab.setImageResource(R.drawable.ic_action_favorite_on)
            } else {
                detailViewModel.delete(tempTV)
                binding.fab.setImageResource(R.drawable.ic_action_favorite_off)
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
        binding.nfs.visibility = View.VISIBLE
        binding.fab.visibility = View.VISIBLE
    }

    private fun found() {
        if(binding.nfs.visibility == View.VISIBLE){
            binding.nfs.visibility = View.GONE
        }
        binding.loadings.visibility = View.GONE
        binding.fab.visibility = View.VISIBLE
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
                binding.appbar.visibility = View.GONE
            } else {
                binding.loadings.visibility = View.GONE
                binding.view1.visibility = View.VISIBLE
                binding.view2.visibility = View.VISIBLE
                binding.view3.visibility = View.VISIBLE
                binding.view4.visibility = View.VISIBLE
                binding.view5.visibility = View.VISIBLE
                binding.rating.visibility = View.VISIBLE
                binding.ratings.visibility = View.VISIBLE
                binding.genre.visibility = View.VISIBLE
                binding.genres.visibility = View.VISIBLE
                binding.duration.visibility = View.VISIBLE
                binding.durations.visibility = View.VISIBLE
                binding.broadcast.visibility = View.VISIBLE
                binding.broadcasts.visibility = View.VISIBLE
                binding.review.visibility = View.VISIBLE
                binding.reviews.visibility = View.VISIBLE
                binding.description.visibility = View.VISIBLE
                binding.descriptions.visibility = View.VISIBLE
                binding.appbar.visibility = View.VISIBLE
            }
        }
    }

    private fun observeFound() : Observer<Boolean> {
        return Observer {
            isFound = if(!it){
                !it
            } else{
                it
            }
        }
    }

    private fun observeLocalDataTV(id: Int,
                                   detailViewModel: DetailViewModel<TVUseCase, TV>
    ) : Observer<List<TV>> {
        return Observer { data ->
            checkDataTV(data, id, detailViewModel)
        }
    }

    private fun observeLocalDataMovie(id: Int,
                                      detailViewModel: DetailViewModel<MovieUseCase, Movie>
    ) : Observer<List<Movie>> {
        return Observer { data ->
            checkDataMovie(data, id, detailViewModel)
        }
    }

    private fun observeRemoteDataMovie() : Observer<Movie>{
        return Observer { movie ->
            if(isFound){
                if(movie != null){
                    val collapsingToolbar  = binding.main
                    val appBarLayout = binding.appbar
                    appBarLayout.setExpanded(true)
                    appBarLayout.addOnOffsetChangedListener(this)
                    val image = binding.image
                    val rating = binding.rating
                    val duration = binding.duration
                    val genre = binding.genre
                    val broadcast = binding.broadcast
                    val description = binding.description
                    val review = binding.review
                    Glide.with(applicationContext).load(
                        Uri.parse(
                            Constant.BASE_IMAGE_URL +
                                    movie.poster_path
                        )
                    ).error(R.mipmap.ic_launcher_round).into(image)
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
                    review.text = getString(R.string.review, reviews.toString())
                    tempMovie = movie
                    found()
                }
                else {
                    notFound()
                }
            }
            else {
                notFound()
            }
        }
    }

    private fun observeRemoteDataTV() : Observer<TV>{
        return Observer { its ->
            if(isFound) {
                if(its !=null){
                    val collapsingToolbar  = binding.main
                    val appBarLayout = binding.appbar
                    appBarLayout.setExpanded(true)
                    appBarLayout.addOnOffsetChangedListener(this)
                    val image = binding.image
                    val duration = binding.duration
                    val genre = binding.genre
                    val broadcast = binding.broadcast
                    val description = binding.description
                    val review = binding.review
                    Glide.with(applicationContext).load(
                        Uri.parse(
                            Constant.BASE_IMAGE_URL +
                                    its.poster_path
                        )
                    ).error(R.mipmap.ic_launcher_round).into(image)
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
                    review.text = getString(R.string.review, reviews.toString())
                    tempTV = its
                    found()
                } else {
                    notFound()
                }
            } else {
                notFound()
            }
        }
    }

    private fun observeRateTV(type:String, rating: TextView) : Observer<String>{
        return Observer { rate ->
            if(type == "tvshows"){
                if(rate != null){
                    rating.text = rate
                }
            } else {
                rating.text = rate
                found()
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
                val intent = Intent(Intent.ACTION_VIEW, uri)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
        if (maxScrollSize == 0) maxScrollSize = appBarLayout!!.totalScrollRange

        val currentScrollPercentage: Int = (abs(verticalOffset) * 100
                / maxScrollSize)

        if (currentScrollPercentage >= percentageShowImage) {
            if (!mIsImageHidden) {
                mIsImageHidden = true
                ViewCompat.animate(binding.fab).scaleY(0f).scaleX(0f).start()
            }
        }

        if (currentScrollPercentage < percentageShowImage) {
            if (mIsImageHidden) {
                mIsImageHidden = false
                ViewCompat.animate(binding.fab).scaleY(1f).scaleX(1f).start()
            }
        }
    }

}