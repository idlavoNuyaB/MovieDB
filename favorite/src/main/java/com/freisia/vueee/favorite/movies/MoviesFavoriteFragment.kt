package com.freisia.vueee.favorite.movies

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.freisia.vueee.R
import com.freisia.vueee.core.presentation.adapter.CardFavoriteAdapter
import com.freisia.vueee.core.presentation.model.movie.Movie
import com.freisia.vueee.favorite.databinding.MoviesFavoriteFragmentBinding
import com.freisia.vueee.presentation.detail.DetailActivity
import kotlinx.coroutines.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.freisia.vueee.favorite.R as T

class MoviesFavoriteFragment : Fragment() {

    private val favoriteViewModel: MoviesFavoriteViewModel by viewModel()
    private lateinit var cardAdapter: CardFavoriteAdapter<Movie>
    private lateinit var binding: MoviesFavoriteFragmentBinding
    private var getJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MoviesFavoriteFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        this.retainInstance = true
        reload()
        getJob?.cancel()
        getJob = CoroutineScope(Dispatchers.IO).launch{
            favoriteViewModel.getData()
            delay(1000)
            withContext(Dispatchers.Main){
                favoriteViewModel.data.observe(viewLifecycleOwner,observer)
            }
        }
        binding.list3.itemAnimator = DefaultItemAnimator()
        binding.list3.layoutManager = GridLayoutManager(this.context,2)
        cardAdapter = CardFavoriteAdapter()
        cardAdapter.onItemClick = {
            val intent = Intent(activity, DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_DETAIL,it)
            intent.putExtra(DetailActivity.TYPE_DETAIL,"localmovies")
            activity?.startActivity(intent)
        }
        binding.list3.adapter = cardAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(T.menu.menu_favorite,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            T.id.delete -> {
                val positiveButtonClick = { _: DialogInterface, _: Int ->
                    favoriteViewModel.deleteAll()
                    reload()
                    getJob = CoroutineScope(Dispatchers.IO).launch{
                        favoriteViewModel.getData()
                        delay(1000)
                        withContext(Dispatchers.Main){
                            favoriteViewModel.data.observe(viewLifecycleOwner,observer)
                        }
                    }
                }
                val negativeButtonClick = { _: DialogInterface, _: Int ->
                    Toast.makeText(this.requireContext(), R.string.cancel, Toast.LENGTH_LONG).show()
                }
                val builder = AlertDialog.Builder(this.requireContext())
                with(builder) {
                    setTitle(R.string.delete)
                    setMessage(R.string.messagedelete)
                    setPositiveButton(
                        R.string.yes,
                        DialogInterface.OnClickListener(positiveButtonClick)
                    )
                    setNegativeButton(R.string.no, negativeButtonClick)
                    val dialog = create()
                    dialog.show()
                    val btnPositive = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                    val btnNegative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)

                    val layoutParams = btnPositive.layoutParams as LinearLayout.LayoutParams
                    layoutParams.weight = 10f
                    btnPositive.layoutParams = layoutParams
                    btnNegative.layoutParams = layoutParams
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private val observer = Observer<PagedList<Movie>>{
        if(it != null){
            cardAdapter.submitList(it)
            found()
        }
    }

    private fun found(){
        binding.loadings.visibility = View.GONE
        binding.whiteViews.visibility = View.GONE
    }

    private fun reload(){
        binding.loadings.visibility = View.VISIBLE
        binding.whiteViews.visibility = View.VISIBLE
    }

}
