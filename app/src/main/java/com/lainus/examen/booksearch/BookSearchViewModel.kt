package com.lainus.examen.booksearch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lainus.domain.Book
import com.lainus.usecases.AddFavorites
import com.lainus.usecases.GetFavoriteBooks
import com.lainus.usecases.SearchBooks
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class BookSearchState(
    val books: List<Book> = emptyList(),
    val favoriteBooks: List<Book> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val searchPerformed: Boolean = false
)

@HiltViewModel
class BookSearchViewModel @Inject constructor(
    private val searchBooks: SearchBooks,
    private val getFavoriteBooks: GetFavoriteBooks,
    private val addFavorites: AddFavorites
) : ViewModel() {

    private val _booksState = MutableStateFlow(BookSearchState())
    val booksState: StateFlow<BookSearchState> = _booksState.asStateFlow()

    init {
        loadFavoriteBooks()
    }

    fun searchBooks(query: String) {
        viewModelScope.launch {
            _booksState.update { it.copy(isLoading = true, error = null) }
            try {
                val results = searchBooks.invoke(query)
                _booksState.update {
                    it.copy(
                        books = results,
                        isLoading = false,
                        searchPerformed = true
                    )
                }
            } catch (e: Exception) {
                _booksState.update {
                    it.copy(
                        isLoading = false,
                        error = "No se pudo conectar al servidor: ${e.message}",
                        searchPerformed = true
                    )
                }
            }
        }
    }

    private fun loadFavoriteBooks() {
        viewModelScope.launch {
            try {
                val favorites = getFavoriteBooks.invoke()
                _booksState.update { it.copy(favoriteBooks = favorites) }
            } catch (e: Exception) {
                // Manejo silencioso, no es crítico para la UI principal
            }
        }
    }

    fun toggleFavorite(book: Book) {
        viewModelScope.launch {
            val isFavorite = _booksState.value.favoriteBooks.contains(book)
            try {
                // Solo agregamos a favoritos si no está en favoritos
                if (!isFavorite) {
                    addFavorites.invoke(book)
                }

                // Actualizamos la lista de favoritos
                loadFavoriteBooks()
            } catch (e: Exception) {
                _booksState.update {
                    it.copy(error = "Error al actualizar favoritos: ${e.message}")
                }
            }
        }
    }
}