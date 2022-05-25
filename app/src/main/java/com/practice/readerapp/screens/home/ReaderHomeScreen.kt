package com.practice.readerapp.screens.homeimport android.util.Logimport android.widget.HorizontalScrollViewimport androidx.compose.foundation.clickableimport androidx.compose.foundation.horizontalScrollimport androidx.compose.foundation.layout.*import androidx.compose.foundation.rememberScrollStateimport androidx.compose.foundation.shape.RoundedCornerShapeimport androidx.compose.material.icons.Iconsimport androidx.compose.material.icons.filled.*import androidx.compose.material3.*import androidx.compose.runtime.Composableimport androidx.compose.ui.Alignmentimport androidx.compose.ui.Modifierimport androidx.compose.ui.draw.clipimport androidx.compose.ui.graphics.Colorimport androidx.compose.ui.platform.LocalContextimport androidx.compose.ui.text.TextStyleimport androidx.compose.ui.text.font.FontWeightimport androidx.compose.ui.text.style.TextOverflowimport androidx.compose.ui.tooling.preview.Previewimport androidx.compose.ui.unit.dpimport androidx.navigation.NavControllerimport com.google.firebase.auth.FirebaseAuthimport com.practice.readerapp.components.*import com.practice.readerapp.model.MBookimport com.practice.readerapp.navigation.ReaderScreensimport com.practice.readerapp.ui.theme.COLOR_APP_PRIMARY@ExperimentalMaterial3Api@Composablefun Home(navController: NavController) {    Scaffold(        topBar = { ReaderAppBar(title = "Reader App", navController = navController) },        floatingActionButton = {            FABContent {                navController.navigate(ReaderScreens.SearchScreen.name)            }        }) {        //PAra el scaffold con Material 3 hay que proveer a lo que tenemos dentro de padings        //usando el it para que no se sobrepongan los objetos.        Surface(            modifier = Modifier                .fillMaxSize()                .padding(top = it.calculateTopPadding())        ) {            HomeContent(navController = navController)        }    }}@OptIn(ExperimentalMaterial3Api::class)@Preview@Composablefun HomeContent(navController: NavController = NavController(LocalContext.current)) {    val listOfBooks = listOf(        MBook(            id = "1",            title = "Harry Potter",            authors = "J.K. Rowling",            notes = "This is a book about Harry Potter"        ),        MBook(            id = "2",            title = "Harry Potter",            authors = "J.K. Rowling",            notes = "This is a book about Harry Potter"        ),        MBook(            id = "3",            title = "Harry Potter",            authors = "J.K. Rowling",            notes = "This is a book about Harry Potter"        ),        MBook(            id = "4",            title = "Harry Potter",            authors = "J.K. Rowling",            notes = "This is a book about Harry Potter"        ),    )    val email = FirebaseAuth.getInstance().currentUser?.email    val currentUsername = if (!email.isNullOrBlank())        FirebaseAuth.getInstance().currentUser?.email?.split("@")?.get(0)    else "N/A"    Column(        Modifier.padding(2.dp),        verticalArrangement = Arrangement.Top    ) {        Row(modifier = Modifier.align(alignment = Alignment.Start)) {            TitleSection(label = "Your reading \n " + " activity right now...")            Spacer(modifier = Modifier.fillMaxWidth(0.7f))            Column {                Icon(                    imageVector = Icons.Filled.AccountCircle,                    contentDescription = "Profile",                    modifier = Modifier                        .clickable {                            navController.navigate(                                ReaderScreens.ReaderStatsScreen.name                            )                        }                        .size(45.dp),                    tint = COLOR_APP_PRIMARY.copy(alpha = 0.5f))                Text(                    text = currentUsername!!,                    modifier = Modifier.padding(2.dp),                    style = MaterialTheme.typography.bodyLarge,                    maxLines = 1,                    overflow = TextOverflow.Clip                )                Divider()            }        }        ReadingRightNowArea(books = listOf(), navController = navController)        TitleSection(label = "Reading List")        BookListArea(listOfBooks = listOfBooks, navController = navController)    }}@Composablefun BookListArea(listOfBooks: List<MBook>, navController: NavController) {    HorizontalScrollableComponent(listOfBooks) {        //TODO card click navigate to details    }}@Composablefun HorizontalScrollableComponent(listOfBooks: List<MBook>, onCardPressed: (String) -> Unit) {    val scrollState = rememberScrollState()    Row(        modifier = Modifier            .fillMaxWidth()            .heightIn(280.dp)            .horizontalScroll(scrollState)    ) {        for (book in listOfBooks) {            ListCard(book) {                onCardPressed(it)                Log.d("BOOK", "Card pressed: $it")            }        }    }}@Composablefun ReadingRightNowArea(books: List<MBook>, navController: NavController) {    ListCard()}