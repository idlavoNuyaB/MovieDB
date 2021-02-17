package com.freisia.vueee.favorite.tvshow

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
import com.freisia.vueee.core.presentation.model.tv.TV
import com.freisia.vueee.favorite.databinding.TvshowsFavoriteFragmentBinding
import com.freisia.vueee.presentation.detail.DetailActivity
import kotlinx.coroutines.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.freisia.vueee.favorite.R as T

class TVShowsFavoriteFragment: Fragment() {

    private val viewModel by viewModel<TVShowsFavoriteViewModel>()
    private lateinit var cardAdapter: CardFavoriteAdapter<TV>
    private lateinit var binding: TvshowsFavoriteFragmentBinding
    private var getJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = TvshowsFavoriteFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.list4.visibility = View.GONE
        setHasOptionsMenu(true)
        this.retainInstance = true
        reload()
        getJob?.cancel()
        getJob = CoroutineScope(Dispatchers.IO).launch{
            viewModel.getData()
            delay(1200)
            withContext(Dispatchers.Main){
                viewModel.data.observe(viewLifecycleOwner,observer)
            }
        }
        binding.list4.itemAnimator = DefaultItemAnimator()
        binding.list4.layoutManager = GridLayoutManager(this.context,2)
        cardAdapter = CardFavoriteAdapter()
        cardAdapter.onItemClick = {
            val intent = Intent(activity, DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_DETAIL,it)
            intent.putExtra(DetailActivity.TYPE_DETAIL,"localtvshows")
            activity?.startActivity(intent)
        }
        binding.list4.adapter = cardAdapter
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
                    viewModel.deleteAll()
                    reload()
                    getJob = CoroutineScope(Dispatchers.IO).launch{
                        viewModel.getData()
                        delay(1200)
                        withContext(Dispatchers.Main){
                            viewModel.data.observe(viewLifecycleOwner,observer)
                        }
                    }
                }
                val negativeButtonClick = { _: DialogInterface, _: Int ->
                    Toast.makeText(this.requireContext(), R.string.cancel, Toast.LENGTH_LONG).show()
                }
                val builder = AlertDialog.Builder(this.requireContext())
                with(builder) {
                    setTitle(R.string.deleteTV)
                    setMessage(R.string.messagedeleteTV)
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

    private val observer = Observer<PagedList<TV>>{
        if(it.isNotEmpty()){
            cardAdapter.submitList(it)
            found()
        } else{
            notFound()
        }
    }

    private fun notFound(){
        binding.loadings.visibility = View.GONE
        binding.viewEmpty.root.visibility = View.VISIBLE
    }

    private fun found(){
        binding.loadings.visibility = View.GONE
        binding.list4.visibility = View.VISIBLE
    }

    private fun reload(){
        binding.loadings.visibility = View.VISIBLE
        binding.list4.visibility = View.GONE
        binding.viewEmpty.root.visibility = View.GONE
    }

}
