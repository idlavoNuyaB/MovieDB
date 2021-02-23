package com.freisia.vueee.presentation.list.tvshow

import android.widget.EditText
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.freisia.vueee.core.data.remote.ApiResponse
import com.freisia.vueee.core.domain.usecase.TVUseCase
import com.freisia.vueee.core.presentation.model.tv.SearchTV
import com.freisia.vueee.core.utils.textChanges
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.lang.ref.WeakReference

class TVShowsViewModel(private val useCase: TVUseCase) : ViewModel() {
    var listData = MutableLiveData<List<SearchTV>>()
    var isLoading = MutableLiveData<Boolean>()
    var isFound = MutableLiveData<Boolean>()
    private var page = 1
    private var pageNP = 1
    private var pageTR = 1
    private var pageS = 1
    private var totalPages = 500
    private lateinit var weakReference : WeakReference<EditText>
    private var searchText = ""
    private var temp = 0

    fun reset(){
        page = 1
        pageNP = 1
        pageTR = 1
        pageS = 1
        searchText = ""
        temp = 0
    }

    fun getData() = viewModelScope.launch(Dispatchers.IO) {
        try{
            val response = useCase.getPopularRemoteData(page)
            withContext(Dispatchers.Main) {
                temp++
                if(temp == 1) {
                    response.onStart { isLoading.value = true }.catch {
                        isLoading.value = false
                        isFound.value = true
                    }.collect {
                        when (it) {
                            is ApiResponse.Success -> {
                                val data = it.data
                                if (!data.result.isNullOrEmpty()) {
                                    listData.value = data.result
                                    totalPages = data.totalPages
                                    isLoading.value = false
                                    isFound.value = true
                                } else {
                                    isLoading.value = false
                                    isFound.value = false
                                }
                            }
                            is ApiResponse.Empty -> {
                                isLoading.value = false
                                isFound.value = false
                            }
                            else -> {
                                isLoading.value = false
                                isFound.value = false
                            }
                        }
                    }
                }
            }
        }  catch (e: Exception){
            withContext(Dispatchers.Main){
                isFound.value = false
                isLoading.value = false
            }
        }
    }

    fun getOnAirData() = viewModelScope.launch(Dispatchers.IO) {
        try{
            val response = useCase.getNowPlayingRemoteData(pageNP)
            withContext(Dispatchers.Main) {
                temp++
                if(temp == 1) {
                    response.onStart { isLoading.value = true }.catch {
                        isLoading.value = false
                        isFound.value = true
                    }.collect {
                        when (it) {
                            is ApiResponse.Success -> {
                                val data = it.data
                                if (!data.result.isNullOrEmpty()) {
                                    listData.value = data.result
                                    totalPages = data.totalPages
                                    isLoading.value = false
                                    isFound.value = true
                                } else {
                                    isLoading.value = false
                                    isFound.value = false
                                }
                            }
                            is ApiResponse.Empty -> {
                                isLoading.value = false
                                isFound.value = false
                            }
                            else -> {
                                isLoading.value = false
                                isFound.value = false
                            }
                        }
                    }
                }
            }
        }  catch (e: Exception){
            withContext(Dispatchers.Main){
                isFound.value = false
                isLoading.value = false
            }
        }
    }

    fun getTopRated() = viewModelScope.launch(Dispatchers.IO) {
        try{
            val response = useCase.getTopRatedRemoteData(pageTR)
            withContext(Dispatchers.Main) {
                temp++
                if(temp == 1) {
                    response.onStart { isLoading.value = true }.catch {
                        isLoading.value = false
                        isFound.value = true
                    }.collect {
                        when (it) {
                            is ApiResponse.Success -> {
                                val data = it.data
                                if (!data.result.isNullOrEmpty()) {
                                    listData.value = data.result
                                    totalPages = data.totalPages
                                    isLoading.value = false
                                    isFound.value = true
                                } else {
                                    isLoading.value = false
                                    isFound.value = false
                                }
                            }
                            is ApiResponse.Empty -> {
                                isLoading.value = false
                                isFound.value = false
                            }
                            else -> {
                                isLoading.value = false
                                isFound.value = false
                            }
                        }
                    }
                }
            }
        }  catch (e: Exception){
            withContext(Dispatchers.Main){
                isFound.value = false
                isLoading.value = false
            }
        }
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    fun getSearch(data: WeakReference<EditText>, query: String) = viewModelScope.launch(Dispatchers.IO) {
        try {
            withContext(Dispatchers.Main) {
                isLoading.value = true
            }
            if (searchText != query) {
                reset()
            }
            weakReference = data
            searchText = query
            getDataSearch()
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                isFound.value = false
                isLoading.value = false
            }
        }
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    private fun getDataSearch() = viewModelScope.launch(Dispatchers.IO){
        withContext(Dispatchers.Main) {
            val editText = weakReference.get() as EditText
            editText.textChanges()
                .distinctUntilChanged()
                .debounce(500)
                .flatMapLatest {
                    useCase.getSearchRemoteData(pageS, it.toString())
                }.onStart {
                    isLoading.value = true
                }.catch { e ->
                    isFound.value = false
                    isLoading.value = false
                }.collect {
                    temp++
                    if(temp == 1) {
                        if (it is ApiResponse.Success) {
                            val data = it.data
                            if (!data.result.isNullOrEmpty()) {
                                listData.value = data.result
                                totalPages = data.totalPages
                                isLoading.value = false
                                isFound.value = true
                            } else {
                                isLoading.value = false
                                isFound.value = false
                            }
                        } else {
                            isLoading.value = false
                            isFound.value = false
                        }
                    }
                }
        }
    }

    @ExperimentalCoroutinesApi
    @FlowPreview
    fun onLoadMore(spinner: Int){
        temp = 0
        if(spinner == 1){
            if(page <= totalPages){
                page++
                getData()
            }
        } else if(spinner == 2){
            if(pageNP <= totalPages){
                pageNP++
                getOnAirData()
            }
        } else if(spinner == 3){
            if(pageTR <= totalPages){
                pageTR++
                getTopRated()
            }
        } else if(spinner == 0){
            if(pageS <= totalPages){
                pageS++
                getDataSearch()
            }
        }
    }
}
