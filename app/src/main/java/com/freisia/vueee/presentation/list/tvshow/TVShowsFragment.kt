package com.freisia.vueee.presentation.list.tvshow

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Rect
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.TextView
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.freisia.vueee.R
import com.freisia.vueee.core.presentation.adapter.CardAdapter
import com.freisia.vueee.core.presentation.model.tv.SearchTV
import com.freisia.vueee.databinding.TvshowsFragmentBinding
import com.freisia.vueee.presentation.detail.DetailActivity
import kotlinx.coroutines.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.lang.ref.WeakReference

class TVShowsFragment: Fragment(), CardAdapter.OnLoadMoreListener {

    companion object {
        const val data = "tvshows"
    }

    private val viewModel by viewModel<TVShowsViewModel>()
    private lateinit var cardAdapter: CardAdapter<SearchTV>
    private lateinit var binding: TvshowsFragmentBinding
    private var detail: ArrayList<SearchTV> = ArrayList()
    private var coroutineJob : Job? = null
    private var check = 1
    private var temp = 0
    private var checkSpinner = 1
    private var isFound = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = TvshowsFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.retainInstance = true
        if(binding.nfs.visibility == View.VISIBLE) binding.nfs.visibility = View.GONE
        if(binding.list2.visibility == View.VISIBLE) binding.list2.visibility = View.GONE
        binding.loadings.visibility = View.VISIBLE
        coroutineJob?.cancel()
        viewModel.reset()
        coroutineJob = CoroutineScope(Dispatchers.IO).launch {
            viewModel.getData()
        }
        data()
        spinnerCheck()
        cardGridRecyclerView()
        binding.layout2.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                if (binding.search2.isFocusable) {
                    val outRect = Rect()
                    binding.search2.getGlobalVisibleRect(outRect)
                    if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                        binding.search2.clearFocus()
                        binding.layout2.performClick()
                        hideKeyboard(v)
                    }
                }
            }
            false
        }
        binding.search2.setOnDebounceTextWatcher(CoroutineScope(Dispatchers.Main)){
            if(it != ""){
                search(it,binding.search2)
            } else if(it == "" && checkSpinner == 0){
                search(it,binding.search2)
            }
        }
    }

    override fun onPause() {
        coroutineJob?.cancel()
        viewModel.isFound.removeObserver(observeFound())
        viewModel.isLoading.removeObserver(observeLoading())
        viewModel.listData.removeObserver(observeData())
        binding.search2.removeOnDebounceTextWatcher()
        super.onPause()
    }

    override fun onDestroy() {
        coroutineJob?.cancel()
        viewModel.isFound.removeObserver(observeFound())
        viewModel.isLoading.removeObserver(observeLoading())
        viewModel.listData.removeObserver(observeData())
        binding.search2.removeOnDebounceTextWatcher()
        super.onDestroy()
    }

    private fun hideKeyboard(v: View){
        val a: InputMethodManager = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        a.hideSoftInputFromWindow(v.windowToken, 0)
    }

    @ExperimentalCoroutinesApi
    @FlowPreview
    private fun search(it : String, editText: EditText){
        reset()
        coroutineJob?.cancel()
        coroutineJob = CoroutineScope(Dispatchers.IO).launch {
            viewModel.getSearch(WeakReference(editText),it)
        }
        data()
        binding.spinners2.setSelection(0)
        checkSpinner = 0
    }

    private fun reset(){
        if (!detail.isNullOrEmpty()) {
            cardAdapter.resetData()
            detail = ArrayList()
        }
        coroutineJob?.cancel()
        viewModel.reset()
    }

    private fun data(){
        viewModel.isLoading.observe(this.viewLifecycleOwner, observeLoading())
        viewModel.isFound.observe(this.viewLifecycleOwner, observeFound())
        viewModel.listData.observe(this.viewLifecycleOwner, observeData())
    }

    private fun spinnerCheck(){
        val adapters = object : ArrayAdapter<String>(this.requireContext(),android.R.layout.simple_spinner_item,
            resources.getStringArray(R.array.spinnerTV)){
            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                val view: TextView = super.getDropDownView(
                    position,
                    convertView,
                    parent
                ) as TextView
                view.setTypeface(view.typeface, Typeface.BOLD)
                view.setPadding(12)

                if (position == binding.spinners2.selectedItemPosition){
                    view.background = ColorDrawable(Color.parseColor("#E61401"))
                    view.setTextColor(Color.parseColor("#FFFFFFFF"))
                }

                return view

            }

        }
        binding.spinners2.adapter = adapters
        binding.spinners2.gravity = Gravity.CENTER
        binding.spinners2.onItemSelectedListener = object  : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when(position){
                    1 -> {
                        reset()
                        coroutineJob = CoroutineScope(Dispatchers.IO).launch {
                            viewModel.getData()
                        }
                        checkSpinner = 1
                        data()
                    }
                    2 -> {
                        reset()
                        coroutineJob = CoroutineScope(Dispatchers.IO).launch {
                            viewModel.getOnAirData()
                        }
                        checkSpinner = 2
                        data()
                    }
                    3 -> {
                        reset()
                        coroutineJob = CoroutineScope(Dispatchers.IO).launch {
                            viewModel.getTopRated()
                        }
                        checkSpinner = 3
                        data()
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
        binding.spinners2.setSelection(1)
    }

    private fun cardGridRecyclerView(){
        binding.list2.itemAnimator = DefaultItemAnimator()
        binding.list2.layoutManager = GridLayoutManager(this.requireActivity(),2)
        cardAdapter = CardAdapter(detail, binding.list2)
        cardAdapter.onItemClick = {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_DETAIL, it)
            intent.putExtra(DetailActivity.TYPE_DETAIL, data)
            intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
            startActivity(intent)
        }
        cardAdapter.onLoadMoreListener = this
        binding.list2.adapter = cardAdapter
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    override fun onGridLoadMore() {
        check++
        coroutineJob?.cancel()
        coroutineJob = CoroutineScope(Dispatchers.IO).launch {
            viewModel.onLoadMore(checkSpinner)
        }
    }

    private fun notFound() {
        if(binding.list2.visibility == View.VISIBLE){
            binding.list2.visibility = View.GONE
        }
        binding.loadings.visibility = View.GONE
        binding.nfs.visibility = View.VISIBLE
    }

    private fun found() {
        if(binding.nfs.visibility == View.VISIBLE){
            binding.nfs.visibility = View.GONE
        }
        binding.loadings.visibility = View.GONE
        binding.list2.visibility = View.VISIBLE
    }

    private fun getData(){
        cardAdapter.setLoad()
        cardAdapter.setData(detail)
    }

    private fun observeLoading() : Observer<Boolean> {
        return Observer {
            if(it){
                binding.loadings.visibility = View.VISIBLE
                binding.list2.visibility = View.GONE
            } else {
                binding.loadings.visibility = View.GONE
                binding.list2.visibility = View.VISIBLE
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

    private fun observeData() : Observer<List<SearchTV>>{
        return Observer{
            if(isFound){
                temp++
                if(check == temp){
                    detail.addAll(it as ArrayList<SearchTV>)
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
