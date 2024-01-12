package com.example.contacts

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.widget.addTextChangedListener
import com.example.contacts.databinding.InputSearchBinding

class SearchInput @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val binding = InputSearchBinding.inflate(LayoutInflater.from(context), this)
    private var searchListener: SearchListener? = null

    init {
        binding.etSearch.addTextChangedListener { content ->
            searchListener?.searchQuery(content.toString())
        }
        binding.ivClear.setOnClickListener {
            binding.etSearch.text?.clear()
            binding.etSearch.clearFocus()
        }
    }

    fun addSearchListener(listener: SearchListener) {
        searchListener = listener
    }
}

interface SearchListener {
    fun searchQuery(query: String?)
}
