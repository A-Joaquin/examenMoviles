package com.lainus.examen.di

import android.content.Context
import com.lainus.data.BookRepository
import com.lainus.data.book.IBookLocalDataSource
import com.lainus.data.book.IBookRemoteDataSource
import com.lainus.framework.service.RetrofitClient
import com.lainus.framework.book.BookRemoteDataSource
import com.lainus.framework.book.BookLocalDataSource
import com.lainus.usecases.AddFavorites
import com.lainus.usecases.GetFavoriteBooks
import com.lainus.usecases.SearchBooks

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Provides
    @Singleton
    fun provideRetrofitClient(): RetrofitClient = RetrofitClient

    @Provides
    @Singleton
    fun provideBookRemoteDataSource(retrofitClient: RetrofitClient): IBookRemoteDataSource {
        return BookRemoteDataSource(retrofitClient)
    }

    @Provides
    @Singleton
    fun provideBookRepository(remoteDataSource: IBookRemoteDataSource, localDataSource: IBookLocalDataSource): BookRepository {
        return BookRepository(remoteDataSource, localDataSource)
    }

    @Provides
    @Singleton
    fun provideSearchBooks(repository: BookRepository): SearchBooks {
        return SearchBooks(repository)
    }

    @Provides
    @Singleton
    fun provideBookLocalDataSource(@ApplicationContext context: Context) : IBookLocalDataSource {
        return BookLocalDataSource(context)
    }

    @Provides
    @Singleton
    fun provideAddToMyFavorites(repository: BookRepository): AddFavorites {
        return AddFavorites(repository)
    }

    @Provides
    @Singleton
    fun provideGetMyFavoriteBooks(repository: BookRepository): GetFavoriteBooks {
        return GetFavoriteBooks(repository)
    }

}
