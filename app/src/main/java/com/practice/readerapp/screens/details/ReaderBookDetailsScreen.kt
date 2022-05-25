package com.practice.readerapp.screens.detailsimport androidx.compose.foundation.layout.Arrangementimport androidx.compose.foundation.layout.Columnimport androidx.compose.foundation.layout.fillMaxSizeimport androidx.compose.foundation.layout.paddingimport androidx.compose.material.icons.Iconsimport androidx.compose.material.icons.filled.ArrowBackimport androidx.compose.material3.ExperimentalMaterial3Apiimport androidx.compose.material3.Scaffoldimport androidx.compose.material3.Surfaceimport androidx.compose.material3.Textimport androidx.compose.runtime.Composableimport androidx.compose.ui.Alignmentimport androidx.compose.ui.Modifierimport androidx.compose.ui.unit.dpimport androidx.navigation.NavControllerimport com.practice.readerapp.components.ReaderAppBarimport com.practice.readerapp.navigation.ReaderScreens@OptIn(ExperimentalMaterial3Api::class)@Composablefun BookDetailsScreen(navController: NavController, bookId: String) {    Scaffold(topBar = {        ReaderAppBar(            title = "Book Details",            icon = Icons.Default.ArrowBack,            showProfile = false,            navController = navController        ) {            navController.navigate(ReaderScreens.SearchScreen.name)        }    }) {        Surface(            modifier = Modifier                .padding(top = it.calculateTopPadding(), start = 3.dp, end = 13.dp)                .fillMaxSize()        ) {            Column(                modifier = Modifier.padding(top = 12.dp),                verticalArrangement = Arrangement.Top,                horizontalAlignment = Alignment.CenterHorizontally            ) {                Text(text = "Book Details Screen")            }        }    }}