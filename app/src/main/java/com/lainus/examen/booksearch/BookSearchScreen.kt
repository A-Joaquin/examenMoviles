package com.lainus.examen.booksearch

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.lainus.domain.Book

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookSearchScreen(
    onNavigateToFavorites: () -> Unit,
    viewModel: BookSearchViewModel = hiltViewModel()
) {
    val uiState by viewModel.booksState.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var isGridView by remember { mutableStateOf(true) }

    val gradientBackground = Brush.verticalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.surface,
            MaterialTheme.colorScheme.surfaceVariant
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBackground)
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            "Book Explorer",
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    ),
                    actions = {
                        IconToggleButton(
                            checked = !isGridView,
                            onCheckedChange = { isGridView = !isGridView }
                        ) {
                            Icon(
                                imageVector = Icons.Default.List,
                                contentDescription = "Cambiar vista",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        IconButton(onClick = onNavigateToFavorites) {
                            Icon(
                                imageVector = Icons.Default.Favorite,
                                contentDescription = "Ver favoritos",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                )
            },
            containerColor = Color.Transparent,
            floatingActionButton = {
                if (uiState.books.isNotEmpty()) {
                    FloatingActionButton(
                        onClick = { /* Scroll to top functionality */ },
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Volver arriba"
                        )
                    }
                }
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
            ) {
                // Buscador con estilo moderno
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    placeholder = { Text("Nombre del libro...") },
                    shape = RoundedCornerShape(24.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                    ),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Búsqueda"
                        )
                    },
                    trailingIcon = {
                        AnimatedVisibility(
                            visible = searchQuery.isNotEmpty(),
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            IconButton(
                                onClick = { searchQuery = "" }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.FavoriteBorder,
                                    contentDescription = "Limpiar"
                                )
                            }
                        }
                    },
                    singleLine = true
                )

                Button(
                    onClick = {
                        if (searchQuery.isNotBlank()) {
                            viewModel.searchBooks(searchQuery)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    enabled = searchQuery.isNotBlank(),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(
                        "BUSCAR",
                        modifier = Modifier.padding(vertical = 4.dp),
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Estado de la búsqueda
                when {
                    uiState.isLoading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(60.dp),
                                strokeWidth = 5.dp
                            )
                        }
                    }
                    uiState.error != null -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = null,
                                    modifier = Modifier.size(80.dp),
                                    tint = MaterialTheme.colorScheme.error
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = uiState.error ?: "Error desconocido",
                                    color = MaterialTheme.colorScheme.error,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                    uiState.searchPerformed && uiState.books.isEmpty() -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = null,
                                    modifier = Modifier.size(80.dp),
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = "No se encontraron libros con ese título",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                    uiState.books.isNotEmpty() -> {
                        if (isGridView) {
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(2),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(uiState.books) { book ->
                                    BookGridItem(
                                        book = book,
                                        isFavorite = uiState.favoriteBooks.contains(book),
                                        onFavoriteClick = { viewModel.toggleFavorite(book) }
                                    )
                                }
                            }
                        } else {
                            LazyColumn(
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(uiState.books) { book ->
                                    BookListItem(
                                        book = book,
                                        isFavorite = uiState.favoriteBooks.contains(book),
                                        onFavoriteClick = { viewModel.toggleFavorite(book) }
                                    )
                                }
                            }
                        }
                    }
                    else -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = null,
                                    modifier = Modifier.size(100.dp),
                                    tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = "Busca tus libros favoritos",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookGridItem(
    book: Book,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Si hay una URL de portada disponible, mostrarla aquí con Coil
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                // Placeholder para la imagen
                Text(
                    text = book.title.first().toString(),
                    modifier = Modifier.align(Alignment.Center),
                    style = MaterialTheme.typography.headlineLarge
                )
            }

            // Degradado inferior para mejorar legibilidad del texto
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.7f)
                            )
                        )
                    )
                    .padding(8.dp)
            ) {
                Column {
                    Text(
                        text = book.title,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White
                    )
                    Text(
                        text = book.publicationYear,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
            }

            // Botón de favorito
            IconButton(
                onClick = onFavoriteClick,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(4.dp)
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.4f))
            ) {
                Icon(
                    imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = if (isFavorite) "Quitar de favoritos" else "Agregar a favoritos",
                    tint = if (isFavorite) MaterialTheme.colorScheme.primary else Color.White
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookListItem(
    book: Book,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Miniatura
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = book.title.first().toString(),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            // Información del libro
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
            ) {
                Text(
                    text = book.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Autor(es): ${book.authors.joinToString(", ")}",
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Año: ${book.publicationYear}",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            // Botón de favorito
            IconButton(
                onClick = onFavoriteClick,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
            ) {
                Icon(
                    imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = if (isFavorite) "Quitar de favoritos" else "Agregar a favoritos",
                    tint = if (isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
    }
}