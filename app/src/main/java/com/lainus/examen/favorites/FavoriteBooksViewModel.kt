package com.lainus.examen.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lainus.domain.Book
import com.lainus.usecases.GetFavoriteBooks
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteBooksViewModel @Inject constructor(
    private val getFavoriteBooks: GetFavoriteBooks
) : ViewModel() {

    private val _favoriteBooks = MutableStateFlow<List<Book>>(emptyList())
    val favoriteBooks: StateFlow<List<Book>> = _favoriteBooks.asStateFlow()

    fun loadFavorites() {
        viewModelScope.launch {
            try {
                _favoriteBooks.value = getFavoriteBooks.invoke()
            } catch (e: Exception) {
                // Podríamos manejar el error actualizando otro StateFlow
                _favoriteBooks.value = emptyList()
            }
        }
    }
}