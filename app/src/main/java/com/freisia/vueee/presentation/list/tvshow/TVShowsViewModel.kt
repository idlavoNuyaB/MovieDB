package com.freisia.vueee.presentation.list.tvshow

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.freisia.vueee.core.data.remote.ApiResponse
import com.freisia.vueee.core.domain.usecase.TVUseCase
import com.freisia.vueee.core.presentation.model.tv.SearchTV
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TVShowsViewModel(private val useCase: TVUseCase) : ViewModel() {
    var listData = MutableLiveData<List<SearchTV>>()
    var isLoading = MutableLiveData<Boolean>()
    var isFound = MutableLiveData<Boolean>()
    private var page = 1
    private var totalPages = 500
    private var searchText: String = ""

    fun reset(){
        page = 1
        searchText = ""
    }

    fun getData() = viewModelScope.launch(Dispatchers.IO) {
        try{
            val response = useCase.getPopularRemoteData(page)
            withContext(Dispatchers.Main){
                response.onStart {isLoading.value = true}.catch {
                    isLoading.value = false
                    isFound.value = true
                }.collect {
                    when(it){
                        is ApiResponse.Success -> {
                            val data = it.data
                            if(!data.result.isNullOrEmpty()){
                                listData.value = data.result
                                totalPages = data.totalPages
                                isLoading.value = false
                                isFound.value = true
                            }
                            else{
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
        }  catch (e: Exception){
            withContext(Dispatchers.Main){
                isFound.value = false
                isLoading.value = false
            }
        }
    }

    fun getOnAirData() = viewModelScope.launch(Dispatchers.IO) {
        try{
            val response = useCase.getNowPlayingRemoteData(page)
            withContext(Dispatchers.Main){
                response.onStart {isLoading.value = true}.catch {
                    isLoading.value = false
                    isFound.value = true
                }.collect {
                    when(it){
                        is ApiResponse.Success -> {
                            val data = it.data
                            if(!data.result.isNullOrEmpty()){
                                listData.value = data.result
                                totalPages = data.totalPages
                                isLoading.value = false
                                isFound.value = true
                            }
                            else{
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
        }  catch (e: Exception){
            withContext(Dispatchers.Main){
                isFound.value = false
                isLoading.value = false
            }
        }
    }

    fun getTopRated() = viewModelScope.launch(Dispatchers.IO) {
        try{
            val response = useCase.getTopRatedRemoteData(page)
            withContext(Dispatchers.Main){
                response.onStart {isLoading.value = true}.catch {
                    isLoading.value = false
                    isFound.value = true
                }.collect {
                    when(it){
                        is ApiResponse.Success -> {
                            val data = it.data
                            if(!data.result.isNullOrEmpty()){
                                listData.value = data.result
                                totalPages = data.totalPages
                                isLoading.value = false
                                isFound.value = true
                            }
                            else{
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
        }  catch (e: Exception){
            withContext(Dispatchers.Main){
                isFound.value = false
                isLoading.value = false
            }
        }
    }

    fun getSearch(query: String) = viewModelScope.launch(Dispatchers.IO) {
        try{
            withContext(Dispatchers.Main){
                isLoading.value = true
            }
            if(searchText != query){
                reset()
            }
            searchText = query
            getDataSearch()
        }  catch (e: Exception){
            withContext(Dispatchers.Main){
                isFound.value = false
                isLoading.value = false
            }
        }
    }

    private fun getDataSearch() = viewModelScope.launch(Dispatchers.IO){
        try {
            val response = useCase.getSearchRemoteData(page,searchText)
            withContext(Dispatchers.Main){
                response.debounce(500).onStart {isLoading.value = true}.catch {
                    isLoading.value = false
                    isFound.value = true
                }.collect {
                    when(it){
                        is ApiResponse.Success -> {
                            val data = it.data
                            if(!data.result.isNullOrEmpty()){
                                listData.value = data.result
                                totalPages = data.totalPages
                                isLoading.value = false
                                isFound.value = true
                            }
                            else{
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
        } catch (e: Exception){
            withContext(Dispatchers.Main){
                isFound.value = false
                isLoading.value = false
            }
        }
    }

    fun onLoadMore(spinner: Int){
        if(spinner == 1){
            if(page <= totalPages){
                page++
                getData()
            }
        } else if(spinner == 2){
            if(page <= totalPages){
                page++
                getOnAirData()
            }
        } else if(spinner == 3){
            if(page <= totalPages){
                page++
                getTopRated()
            }
        } else{
            if(page <= totalPages){
                page++
                getDataSearch()
            }
        }
    }
}
