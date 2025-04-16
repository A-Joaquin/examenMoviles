package com.lainus.examen.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.lainus.examen.booksearch.BookSearchScreen
import com.lainus.examen.favorites.FavoriteBooksScreen

@Composable
fun BooksNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = AppRoutes.BookSearch.route,
        modifier = modifier
    ) {
        composable(AppRoutes.BookSearch.route) {
            BookSearchScreen(
                onNavigateToFavorites = {
                    navController.navigate(AppRoutes.Favorites.route)
                }
            )
        }

        composable(AppRoutes.Favorites.route) {
            FavoriteBooksScreen(
                onNavigateBack = {
                    navController.navigateUp()
                }
            )
        }
    }
}