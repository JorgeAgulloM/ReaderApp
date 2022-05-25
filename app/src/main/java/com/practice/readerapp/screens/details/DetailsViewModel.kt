package com.practice.readerapp.screens.detailsimport androidx.lifecycle.ViewModelimport com.practice.readerapp.data.Resourceimport com.practice.readerapp.model.Itemimport com.practice.readerapp.repository.BookRepositoryimport dagger.hilt.android.lifecycle.HiltViewModelimport javax.inject.Inject@HiltViewModelclass DetailsViewModel @Inject constructor(private val repository: BookRepository): ViewModel() {    suspend fun getBookInfo(bookId: String): Resource<Item> {        return repository.getBookInfo(bookId)    }}