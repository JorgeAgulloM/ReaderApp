package com.practice.readerapp.screens.searchimport android.util.Logimport androidx.compose.foundation.Imageimport androidx.compose.foundation.clickableimport androidx.compose.foundation.layout.*import androidx.compose.foundation.lazy.LazyColumnimport androidx.compose.foundation.lazy.itemsimport androidx.compose.foundation.text.KeyboardActionsimport androidx.compose.material.icons.Iconsimport androidx.compose.material.icons.filled.ArrowBackimport androidx.compose.material3.*import androidx.compose.runtime.Composableimport androidx.compose.runtime.mutableStateOfimport androidx.compose.runtime.rememberimport androidx.compose.runtime.saveable.rememberSaveableimport androidx.compose.ui.Alignmentimport androidx.compose.ui.ExperimentalComposeUiApiimport androidx.compose.ui.Modifierimport androidx.compose.ui.graphics.RectangleShapeimport androidx.compose.ui.platform.LocalContextimport androidx.compose.ui.platform.LocalSoftwareKeyboardControllerimport androidx.compose.ui.tooling.preview.Previewimport androidx.compose.ui.unit.dpimport androidx.navigation.NavControllerimport coil.compose.rememberAsyncImagePainterimport com.practice.readerapp.components.InputFieldimport com.practice.readerapp.components.ReaderAppBarimport com.practice.readerapp.model.MBookimport com.practice.readerapp.navigation.ReaderScreens@Preview@OptIn(ExperimentalMaterial3Api::class)@Composablefun SearchScreen(navController: NavController = NavController(LocalContext.current)) {    Scaffold(topBar = {        ReaderAppBar(            title = "Search Books",            icon = Icons.Default.ArrowBack,            navController = navController,            showProfile = false        ) {            navController.navigate(ReaderScreens.ReaderHomeScreen.name)            //navController.popBackStack()        }    }) {        Surface(modifier = Modifier.padding(top = it.calculateTopPadding())) {            Column {                SearchForm(                    modifier = Modifier                        .fillMaxWidth()                        .padding(8.dp)                ) {                    Log.d("TAG", "Searching for $it")                }                Spacer(modifier = Modifier.height(8.dp))                BookList(navController = navController)            }        }    }}@Composablefun BookList(navController: NavController) {    val listOfBooks = listOf(        MBook(            id = "1",            title = "Harry Potter",            author = "J.K. Rowling",            notes = "This is a book about Harry Potter"        ),        MBook(            id = "2",            title = "Harry Potter",            author = "J.K. Rowling",            notes = "This is a book about Harry Potter"        ),        MBook(            id = "3",            title = "Harry Potter",            author = "J.K. Rowling",            notes = "This is a book about Harry Potter"        ),        MBook(            id = "4",            title = "Harry Potter",            author = "J.K. Rowling",            notes = "This is a book about Harry Potter"        ),    )    LazyColumn(        modifier = Modifier.fillMaxSize(),        contentPadding = PaddingValues(16.dp)    ) {        items(listOfBooks) { book ->            BookRow(book = book, navController = navController)        }    }}@OptIn(ExperimentalMaterial3Api::class)@Composablefun BookRow(book: MBook, navController: NavController) {    ElevatedCard(        modifier = Modifier            .clickable { }            .fillMaxWidth()            .height(100.dp)            .padding(8.dp),        shape = RectangleShape,        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 7.dp)    ) {        Row(            modifier = Modifier.padding(8.dp),            verticalAlignment = Alignment.Top        ) {            val imageUrl = "https://nttdata-solutions.com/wp-content/usermedia/userphoto/59.jpg"            Image(                painter = rememberAsyncImagePainter(model = imageUrl),                contentDescription = "Book image",                modifier = Modifier                    .fillMaxHeight()                    .padding(end = 4.dp)            )            Column() {                Text(                    text = book.title.toString(),                    style = MaterialTheme.typography.titleMedium                )                Text(                    text = book.author.toString(),                    style = MaterialTheme.typography.bodyLarge                )            }        }    }}@OptIn(ExperimentalComposeUiApi::class)@Composablefun SearchForm(    modifier: Modifier = Modifier,    loading: Boolean = false,    hint: String = "Search Books",    onSearch: (String) -> Unit = {}) {    Column {        val searchQueryState = rememberSaveable { mutableStateOf("") }        val keyboardController = LocalSoftwareKeyboardController.current        val valid = remember(searchQueryState.value) {            searchQueryState.value.trim().isNotEmpty()        }        InputField(            valueState = searchQueryState,            labelId = "Search",            enabled = true,            onAction = KeyboardActions {                if (!valid) return@KeyboardActions                onSearch(searchQueryState.value.trim())                searchQueryState.value = ""                keyboardController?.hide()            })    }}