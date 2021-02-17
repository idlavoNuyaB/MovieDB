package com.freisia.vueee.presentation.list.movies

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.freisia.vueee.R
import com.freisia.vueee.core.presentation.adapter.CardAdapter
import com.freisia.vueee.core.presentation.model.movie.SearchMovie
import com.freisia.vueee.databinding.MoviesFragmentBinding
import com.freisia.vueee.presentation.detail.DetailActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MoviesFragment : Fragment(), CardAdapter.OnLoadMoreListener {

    companion object {
        const val data = "movies"
    }

    private lateinit var binding: MoviesFragmentBinding
    private val viewModel: MoviesViewModel by viewModel()
    private lateinit var cardAdapter: CardAdapter<SearchMovie>
    private var detail: ArrayList<SearchMovie> = ArrayList()
    private var coroutineJob : Job? = null
    private var check = 1
    private var temp = 0
    private var checkSpinner = 1
    private var isFound = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MoviesFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.retainInstance = true
        if(binding.nfs.visibility == View.VISIBLE) binding.nfs.visibility = View.GONE
        if(binding.list.visibility == View.VISIBLE) binding.list.visibility = View.GONE
        binding.loadings.visibility = View.VISIBLE
        coroutineJob?.cancel()
        viewModel.reset()
        coroutineJob = CoroutineScope(Dispatchers.IO).launch {
            viewModel.getData()
        }
        viewModel.isLoading.observeForever(observeLoading())
        viewModel.isFound.observeForever(observeFound())
        viewModel.listData.observeForever(observeData())
        spinnerCheck()
        cardGridRecyclerView()
    }

    private fun spinnerCheck(){
        val adapters = object : ArrayAdapter<String>(this.requireContext(),android.R.layout.simple_spinner_item,
            resources.getStringArray(R.array.spinner)){
            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                val view:TextView = super.getDropDownView(
                    position,
                    convertView,
                    parent
                ) as TextView
                view.setTypeface(view.typeface, Typeface.BOLD)
                view.setPadding(12)

                if (position == binding.spinner.selectedItemPosition){
                    view.background = ColorDrawable(Color.parseColor("#E61401"))
                    view.setTextColor(Color.parseColor("#FFFFFFFF"))
                }

                return view

            }

        }
        binding.spinner.adapter = adapters
        binding.spinner.gravity = Gravity.CENTER
        binding.spinner.onItemSelectedListener = object  : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when(position){
                    0 -> {
                        if(!detail.isNullOrEmpty()){
                            cardAdapter.resetData()
                            detail = ArrayList()
                        }
                        coroutineJob?.cancel()
                        viewModel.reset()
                        coroutineJob = CoroutineScope(Dispatchers.IO).launch {
                            viewModel.getData()
                        }
                        checkSpinner = 1
                        viewModel.isLoading.observeForever(observeLoading())
                        viewModel.isFound.observeForever(observeFound())
                        viewModel.listData.observeForever(observeData())
                    }
                    1 -> {
                        if(!detail.isNullOrEmpty()) {
                            cardAdapter.resetData()
                            detail = ArrayList()
                        }
                        coroutineJob?.cancel()
                        viewModel.reset()
                        coroutineJob = CoroutineScope(Dispatchers.IO).launch {
                            viewModel.getNowPlaying()
                        }
                        checkSpinner = 2
                        viewModel.isLoading.observeForever(observeLoading())
                        viewModel.isFound.observeForever(observeFound())
                        viewModel.listData.observeForever(observeData())
                    }
                    2 -> {
                        if(!detail.isNullOrEmpty()) {
                            cardAdapter.resetData()
                            detail = ArrayList()
                        }
                        coroutineJob?.cancel()
                        viewModel.reset()
                        coroutineJob = CoroutineScope(Dispatchers.IO).launch {
                            viewModel.getTopRated()
                        }
                        checkSpinner = 3
                        viewModel.isLoading.observeForever(observeLoading())
                        viewModel.isFound.observeForever(observeFound())
                        viewModel.listData.observeForever(observeData())
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }
    }

    override fun onPause() {
        coroutineJob?.cancel()
        viewModel.isFound.removeObserver(observeFound())
        viewModel.isLoading.removeObserver(observeLoading())
        viewModel.listData.removeObserver(observeData())
        super.onPause()
    }

    override fun onDestroy() {
        coroutineJob?.cancel()
        viewModel.isFound.removeObserver(observeFound())
        viewModel.isLoading.removeObserver(observeLoading())
        viewModel.listData.removeObserver(observeData())
        super.onDestroy()
    }

    private fun cardGridRecyclerView(){
        binding.list.itemAnimator = DefaultItemAnimator()
        binding.list.layoutManager = GridLayoutManager(this.requireActivity(),2)
        cardAdapter = CardAdapter(detail, binding.list)
        cardAdapter.onItemClick = {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_DETAIL, it)
            intent.putExtra(DetailActivity.TYPE_DETAIL,data)
            intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
            startActivity(intent)
        }
        cardAdapter.onLoadMoreListener = this
        binding.list.adapter = cardAdapter
    }

    override fun onGridLoadMore() {
        check++
        coroutineJob?.cancel()
        coroutineJob = CoroutineScope(Dispatchers.IO).launch {
            viewModel.onLoadMore(checkSpinner)
        }
    }

    private fun notFound() {
        if(binding.list.visibility == View.VISIBLE){
            binding.list.visibility = View.GONE
        }
        binding.loadings.visibility = View.GONE
        binding.nfs.visibility = View.VISIBLE
    }

    private fun found() {
        if(binding.nfs.visibility == View.VISIBLE){
            binding.nfs.visibility = View.GONE
        }
        binding.loadings.visibility = View.GONE
        binding.list.visibility = View.VISIBLE
    }

    private fun getData(){
        cardAdapter.setLoad()
        cardAdapter.setData(detail)
    }

    private fun observeLoading() : Observer<Boolean>{
        return Observer {
            if(it){
                binding.loadings.visibility = View.VISIBLE
                binding.list.visibility = View.GONE
            } else {
                binding.loadings.visibility = View.GONE
                binding.list.visibility = View.VISIBLE
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

    private fun observeData() : Observer<List<SearchMovie>>{
        return Observer{
            if(isFound){
                temp++
                if(check == temp){
                    detail.addAll(it as ArrayList<SearchMovie>)
                    found()
                    getData()
                } else {
                    temp = check
                }
            } else {
                notFound()
            }
        }
    }
}
