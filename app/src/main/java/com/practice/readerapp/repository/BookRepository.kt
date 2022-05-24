package com.practice.readerapp.repositoryimport android.content.ClipDataimport com.practice.readerapp.data.DataOrExceptionimport com.practice.readerapp.model.Bookimport com.practice.readerapp.model.Itemimport com.practice.readerapp.network.BooksApiimport javax.inject.Injectclass BookRepository @Inject constructor(    private val api: BooksApi) {    private val dataOrException =        DataOrException<List<Item>, Boolean, Exception>()    private val bookInfoDataOrException =        DataOrException<Item, Boolean, Exception>()    suspend fun getBooks(searchQuery: String): DataOrException<List<Item>, Boolean, Exception> {        try {            dataOrException.loading = true            dataOrException.data = api.getAllBooks(searchQuery).items            if (dataOrException.data.isNullOrEmpty())                dataOrException.loading = false        } catch (e: Exception) {            dataOrException.e = e        }        return dataOrException    }    suspend fun getBookInfo(bookId: String): DataOrException<Item, Boolean, Exception> {        try {            bookInfoDataOrException.loading = true            bookInfoDataOrException.data = api.getBookInfo(bookId = bookId)            if (bookInfoDataOrException.data.toString().isEmpty())                bookInfoDataOrException.loading = false        } catch (e: java.lang.Exception) {            bookInfoDataOrException.e = e        }        return bookInfoDataOrException    }}