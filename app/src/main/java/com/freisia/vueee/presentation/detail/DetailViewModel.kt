package com.freisia.vueee.presentation.detail

import android.content.Context
import androidx.lifecycle.*
import com.freisia.vueee.R
import com.freisia.vueee.core.data.remote.ApiResponse
import com.freisia.vueee.core.domain.usecase.MovieUseCase
import com.freisia.vueee.core.domain.usecase.TVUseCase
import com.freisia.vueee.core.presentation.model.movie.Movie
import com.freisia.vueee.core.presentation.model.tv.TV
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailViewModel <T,Z>(private val usecase: T, private val context: Context) : ViewModel(){
    var listData = MutableLiveData<Z>()
    var isLoading = MutableLiveData<Boolean>()
    var isFound = MutableLiveData<Boolean>()
    var rate = MutableLiveData<String>()
    val localData : LiveData<List<Z>>?

    init{
        localData = getLocalData()
        isLoading.value =false
        isFound.value = true
    }

    @JvmName("getLocalData1")
    private fun getLocalData() : LiveData<List<Z>> {
        return when (usecase) {
            is TVUseCase -> {
                usecase.getListDetailLocalData().asLiveData() as LiveData<List<Z>>
            }
            is MovieUseCase -> {
                usecase.getListDetailLocalData().asLiveData() as LiveData<List<Z>>
            }
            else -> {
                val useCase = usecase as TVUseCase
                useCase.getListDetailLocalData().asLiveData() as LiveData<List<Z>>
            }
        }
    }

    fun getData(id: Int) = viewModelScope.launch(Dispatchers.IO) {
        try{
            withContext(Dispatchers.Main) {
                isLoading.value = true
                if (usecase is MovieUseCase) {
                    val response = usecase.getDetailRemoteData(id)
                    response.onStart {isLoading.value = true}.catch {
                        isLoading.value = false
                        isFound.value = true
                    }.collect {
                        when (it) {
                            is ApiResponse.Success -> {
                                val data = it.data
                                listData.value = data as Z
                                isLoading.value = false
                                isFound.value = true
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
                } else if(usecase is TVUseCase){
                    val response = usecase.getDetailRemoteData(id)
                    val rating = usecase.getRatingTV(id)
                    response.onStart {isLoading.value = true}.catch {
                        isLoading.value = false
                        isFound.value = true
                    }.collect {
                        when (it) {
                            is ApiResponse.Success -> {
                                val data = it.data
                                listData.value = data as Z
                                isLoading.value = false
                                isFound.value = true
                                rating.collect { its ->
                                    when (its){
                                        is ApiResponse.Success -> {
                                            for(i in 0 until its.data.results.size){
                                                if(its.data.results[i].iso_3166_1 == "US"){
                                                    rate.value = its.data.results[i].rating
                                                    break
                                                }
                                            }

                                        }
                                        else -> {
                                            rate.value = context.resources
                                                        .getString(R.string.notfoundrating)
                                        }
                                    }
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
        } catch (e: Exception){
            withContext(Dispatchers.Main){
                isFound.value = false
                isLoading.value = false
            }
        }
    }

    fun insert(data: Z){
        if(usecase is MovieUseCase){
            viewModelScope.launch(Dispatchers.IO) {
                if(data is Movie){
                    usecase.insertFavoriteLocalData(data)
                }
            }
        } else if(usecase is TVUseCase){
            viewModelScope.launch(Dispatchers.IO) {
                if(data is TV){
                    usecase.insertFavoriteLocalData(data)
                }
            }
        }
    }

    fun delete(data: Z) {
        if(usecase is MovieUseCase){
            viewModelScope.launch(Dispatchers.IO) {
                if(data is Movie){
                    usecase.deleteFavoriteLocalData(data)
                }
            }
        } else if(usecase is TVUseCase){
            viewModelScope.launch(Dispatchers.IO) {
                if(data is TV){
                    usecase.deleteFavoriteLocalData(data)
                }
            }
        }
    }
}