package com.practice.readerapp.screens.detailsimport android.util.Logimport androidx.compose.foundation.BorderStrokeimport androidx.compose.foundation.Imageimport androidx.compose.foundation.layout.*import androidx.compose.foundation.lazy.LazyColumnimport androidx.compose.foundation.shape.CircleShapeimport androidx.compose.foundation.shape.RoundedCornerShapeimport androidx.compose.material.icons.Iconsimport androidx.compose.material.icons.filled.ArrowBackimport androidx.compose.material3.*import androidx.compose.runtime.Composableimport androidx.compose.runtime.produceStateimport androidx.compose.ui.Alignmentimport androidx.compose.ui.Modifierimport androidx.compose.ui.draw.scaleimport androidx.compose.ui.graphics.RectangleShapeimport androidx.compose.ui.platform.LocalContextimport androidx.compose.ui.text.style.TextOverflowimport androidx.compose.ui.unit.dpimport androidx.core.text.HtmlCompatimport androidx.hilt.navigation.compose.hiltViewModelimport androidx.navigation.NavControllerimport androidx.navigation.navArgumentimport coil.compose.rememberAsyncImagePainterimport com.google.firebase.auth.FirebaseAuthimport com.google.firebase.firestore.FirebaseFirestoreimport com.practice.readerapp.components.ReaderAppBarimport com.practice.readerapp.components.RoundedButtonimport com.practice.readerapp.data.Resourceimport com.practice.readerapp.model.Itemimport com.practice.readerapp.model.MBookimport com.practice.readerapp.navigation.ReaderScreens@OptIn(ExperimentalMaterial3Api::class)@Composablefun BookDetailsScreen(    navController: NavController,    bookId: String,    viewModel: DetailsViewModel = hiltViewModel()) {    Scaffold(topBar = {        ReaderAppBar(            title = "Book Details",            icon = Icons.Default.ArrowBack,            showProfile = false,            navController = navController        ) {            navController.navigate(ReaderScreens.SearchScreen.name)        }    }) {        Surface(            modifier = Modifier                .padding(top = it.calculateTopPadding(), start = 3.dp, end = 13.dp)                .fillMaxSize()        ) {            Column(                modifier = Modifier.padding(top = 12.dp),                verticalArrangement = Arrangement.Top,                horizontalAlignment = Alignment.CenterHorizontally            ) {                val bookInfo = produceState<Resource<Item>>(initialValue = Resource.Loading()) {                    value = viewModel.getBookInfo(bookId)                }.value                if (bookInfo.data == null)                    Row(horizontalArrangement = Arrangement.SpaceAround) {                        LinearProgressIndicator()                        Text(text = "Loading...")                    }                else                    ShowBookDetails(bookInfo, navController)            }        }    }}@OptIn(ExperimentalMaterial3Api::class)@Composablefun ShowBookDetails(bookInfo: Resource<Item>, navController: NavController) {    val bookData = bookInfo.data?.volumeInfo    val googleBookId = bookInfo.data?.id    ElevatedCard(        modifier = Modifier.padding(34.dp),        shape = CircleShape,        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)    ) {        Image(            painter = rememberAsyncImagePainter(model = bookData!!.imageLinks.thumbnail),            contentDescription = "Cover of ${bookData.title}",            modifier = Modifier                .scale(1.45f)                .size(150.dp)                .padding(1.dp)        )    }    Text(        text = bookData?.title.toString(),        style = MaterialTheme.typography.titleLarge,        overflow = TextOverflow.Ellipsis,        maxLines = 19    )    Text(text = "Authors: ${bookData?.authors.toString()}")    Text(text = "Page Count: ${bookData?.pageCount.toString()}")    Text(        text = "Categories: ${bookData?.categories.toString()}",        style = MaterialTheme.typography.titleSmall,        maxLines = 3    )    Text(        text = "Published Date: ${bookData?.publishedDate.toString()}",        style = MaterialTheme.typography.titleSmall    )    //Text(text = "Language: ${bookData?.language.toString()}")    Spacer(modifier = Modifier.height(16.dp))    val cleanDescription =        HtmlCompat.fromHtml(bookData?.description.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY)            .toString()    val localDims = LocalContext.current.resources.displayMetrics    Surface(        modifier = Modifier            .height(localDims.heightPixels.dp.times(0.09f))            .padding(4.dp),        shape = RectangleShape,        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground)    ) {        LazyColumn(modifier = Modifier.padding(3.dp)) {            item {                Text(text = cleanDescription)            }        }    }    Row(modifier = Modifier.padding(top = 6.dp), horizontalArrangement = Arrangement.SpaceAround) {        RoundedButton(label = "Save") {            val book = MBook(                title = bookData?.title.toString(),                authors = bookData?.authors.toString(),                description = cleanDescription,                categories = bookData?.categories.toString(),                notes = "",                photoUrl = bookData?.imageLinks?.thumbnail,                publishedDate = bookData?.publishedDate,                pageCount = bookData?.pageCount.toString(),                rating = 0.0,                googleBookId = googleBookId,                userId = FirebaseAuth.getInstance().currentUser?.uid.toString()                //language = bookData?.language.toString()            )            saveToFireBase(book, navController = navController)        }        Spacer(modifier = Modifier.width(25.dp))        RoundedButton(label = "Cancel") {            navController.popBackStack()        }    }}fun saveToFireBase(book: Any, navController: NavController) {    val db = FirebaseFirestore.getInstance()    val dbCollection = db.collection("books")    if (book.toString().isNotEmpty())        dbCollection.add(book)            .addOnSuccessListener { documentRef ->                val docId = documentRef.id                dbCollection.document(docId).update(hashMapOf("id" to docId) as Map<String, Any>)                    .addOnCompleteListener{ task ->                        if (task.isSuccessful)                            navController.popBackStack()                    }                Log.d("BookDetails", "DocumentSnapshot added with ID: $docId")            }            .addOnFailureListener {                Log.w("BookDetails", "Error adding document", it)            }}