package com.freisia.vueee.presentation.list.movies

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.freisia.vueee.core.data.remote.ApiResponse
import com.freisia.vueee.core.domain.usecase.MovieUseCase
import com.freisia.vueee.core.presentation.model.movie.SearchMovie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MoviesViewModel(private val useCase: MovieUseCase) : ViewModel() {
    var listData = MutableLiveData<List<SearchMovie>>()
    var isLoading = MutableLiveData<Boolean>()
    var isFound = MutableLiveData<Boolean>()
    private var pageNP = 1
    private var page = 1
    private var pageTP = 1
    private var totalPages = 500

    fun reset(){
        page = 1
        pageNP = 1
        pageTP = 1
    }

    fun getData() = viewModelScope.launch(Dispatchers.IO){
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

    fun getNowPlaying()= viewModelScope.launch(Dispatchers.IO) {
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

    fun getTopRated()= viewModelScope.launch(Dispatchers.IO) {
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

    fun onLoadMore(spinner: Int){
        if(spinner == 1){
            if(page <= totalPages){
                page++
                getData()
            }
        } else if(spinner == 2){
            if(pageNP <= totalPages){
                pageNP++
                getNowPlaying()
            }
        } else if(spinner == 3){
            if(pageTP <= totalPages){
                pageTP++
                getTopRated()
            }
        }
    }
}