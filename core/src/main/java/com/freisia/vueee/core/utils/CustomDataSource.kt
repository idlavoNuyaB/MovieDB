package com.freisia.vueee.core.utils

import androidx.paging.PositionalDataSource

class CustomDataSource<T> (var data: List<T>) : PositionalDataSource<T>() {
    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<T>) {
        val totalCount = data.size
        val position = computeInitialLoadPosition(params,totalCount)
        val loadSize = computeInitialLoadSize(params,position,totalCount)
        val subList = data.subList(position,position + loadSize)
        callback.onResult(subList, position, totalCount)
    }

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<T>) {
        val totalCount = data.size
        val start = params.startPosition
        val end = params.startPosition + params.loadSize
        if(totalCount < end){
            callback.onResult(data.subList(start,totalCount))
        } else {
            callback.onResult(data.subList(start,end))
        }
    }
}