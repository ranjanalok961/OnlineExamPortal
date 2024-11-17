package com.example.onlineexamportal

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

// BookViewModel.kt
class BookViewModel : ViewModel() {

    private val _books = MutableLiveData<List<Book>>()
    val books: LiveData<List<Book>> get() = _books

    private val _category = MutableLiveData<String>()
    val category: LiveData<String> get() = _category

    fun fetchBooks(category: String) {
        viewModelScope.launch {
            val response : BookResponse = RetrofitInstance.bookApi.getBooks(category)
            _books.postValue(response.items ?: emptyList())
            Log.d("Movie",response.items[0].volumeInfo.title)
        }
    }

    fun setCategory(category: String) {
        fetchBooks(category)

    }
}
