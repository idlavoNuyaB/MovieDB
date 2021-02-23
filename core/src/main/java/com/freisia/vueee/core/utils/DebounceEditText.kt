package com.freisia.vueee.core.utils

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import com.ndroid.CoolEditText
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*

class DebounceEditText @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
    ) : CoolEditText(context,attributeSet) {
    private val debouncePeriod = 500L
    private var searchJob: Job? = null

    @FlowPreview
    @ExperimentalCoroutinesApi
    fun setOnDebounceTextWatcher(scope: CoroutineScope, onDebounceAction: (String) -> Unit) {
        searchJob?.cancel()
        searchJob = onDebounceTextChanged()
            .debounce(debouncePeriod)
            .distinctUntilChanged()
            .onEach { onDebounceAction(it) }
            .launchIn(scope)
    }

    fun removeOnDebounceTextWatcher() {
        searchJob?.cancel()
    }

    @ExperimentalCoroutinesApi
    private fun onDebounceTextChanged(): Flow<String> = channelFlow {
        val textWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) = Unit
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                offer(s.toString())
            }
        }

        addTextChangedListener(textWatcher)

        awaitClose {
            removeTextChangedListener(textWatcher)
        }
    }


}