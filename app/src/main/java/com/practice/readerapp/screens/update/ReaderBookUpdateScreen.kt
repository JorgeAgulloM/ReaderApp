package com.practice.readerapp.screens.updateimport android.util.Logimport androidx.compose.foundation.Imageimport androidx.compose.foundation.backgroundimport androidx.compose.foundation.clickableimport androidx.compose.foundation.layout.*import androidx.compose.foundation.shape.CircleShapeimport androidx.compose.foundation.shape.RoundedCornerShapeimport androidx.compose.foundation.text.KeyboardActionsimport androidx.compose.material.icons.Iconsimport androidx.compose.material.icons.filled.ArrowBackimport androidx.compose.material3.*import androidx.compose.runtime.Composableimport androidx.compose.runtime.mutableStateOfimport androidx.compose.runtime.produceStateimport androidx.compose.runtime.rememberimport androidx.compose.runtime.saveable.rememberSaveableimport androidx.compose.ui.Alignmentimport androidx.compose.ui.ExperimentalComposeUiApiimport androidx.compose.ui.Modifierimport androidx.compose.ui.draw.alphaimport androidx.compose.ui.draw.clipimport androidx.compose.ui.graphics.Colorimport androidx.compose.ui.platform.LocalContextimport androidx.compose.ui.platform.LocalSoftwareKeyboardControllerimport androidx.compose.ui.text.font.FontWeightimport androidx.compose.ui.text.style.TextOverflowimport androidx.compose.ui.unit.dpimport androidx.hilt.navigation.compose.hiltViewModelimport androidx.navigation.NavControllerimport coil.compose.rememberAsyncImagePainterimport com.google.firebase.Timestampimport com.google.firebase.firestore.FirebaseFirestoreimport com.practice.readerapp.components.InputFieldimport com.practice.readerapp.components.RatingBarimport com.practice.readerapp.components.ReaderAppBarimport com.practice.readerapp.components.RoundedButtonimport com.practice.readerapp.data.DataOrExceptionimport com.practice.readerapp.model.MBookimport com.practice.readerapp.screens.home.HomeScreenViewModel@OptIn(ExperimentalMaterial3Api::class)@Composablefun BookUpdateScreen(    navController: NavController,    bookItemId: String,    viewModel: HomeScreenViewModel = hiltViewModel()) {    Scaffold(topBar = {        ReaderAppBar(            title = "Update Book",            icon = Icons.Default.ArrowBack,            showProfile = false,            navController = navController        ) {            navController.popBackStack()        }    }) {        val bookInfo = produceState<DataOrException<List<MBook>, Boolean, Exception>>(            initialValue = DataOrException(data = emptyList(), true, Exception(""))        ) {            value = viewModel.data.value        }.value        Surface(            modifier = Modifier                .fillMaxSize()                .padding(3.dp)        ) {            Column(                modifier = Modifier.padding(top = it.calculateTopPadding()),                verticalArrangement = Arrangement.Top,                horizontalAlignment = Alignment.CenterHorizontally            ) {                Log.d("INFO", "bookInfo.data.size: ${viewModel.data.value.data.toString()}")                if (bookInfo.loading == true) {                    LinearProgressIndicator()                    bookInfo.loading = false                } else {                    Surface(                        modifier = Modifier                            .padding(2.dp)                            .fillMaxWidth(),                        shape = CircleShape,                        shadowElevation = 4.dp                    ) {                        ShowBookUpdate(bookInfo = viewModel.data.value, bookItemId = bookItemId)                    }                    ShowSimpleForm(book = viewModel.data.value.data?.first { mBook ->                        mBook.googleBookId == bookItemId                    }, navController = navController)                }            }        }    }}@Composablefun ShowSimpleForm(book: MBook?, navController: NavController) {    val notesText = remember { mutableStateOf("") }    val isStartedReading = remember { mutableStateOf(false) }    val isFinishedReading = remember { mutableStateOf(false) }    val ratingVal = remember { mutableStateOf(0) }    SimpleForm(defaultValue = book?.notes.toString().ifEmpty {        "No thoughts available :("    }) { note ->        notesText.value = note    }    Row(        modifier = Modifier.padding(4.dp), verticalAlignment = Alignment.CenterVertically,        horizontalArrangement = Arrangement.Start    ) {        TextButton(            onClick = {                isStartedReading.value = true            },            enabled = book?.startedReading == null        ) {            if (book?.startedReading == null) {                if (!isStartedReading.value) {                    Text(text = "Start Reading")                } else {                    Text(                        text = "Started Reading!",                        modifier = Modifier.alpha(0.6f),                        color = Color.Red.copy(alpha = 0.6f)                    )                }            } else {                Text(                    text = "Started on: ${book.startedReading}",                    style = MaterialTheme.typography.bodyLarge.copy(                        fontWeight = FontWeight.Bold                    )                )            }        }        Spacer(modifier = Modifier.height(4.dp))        TextButton(            onClick = {                isFinishedReading.value = true            },            enabled = book?.finishedReading == null        ) {            if (book?.finishedReading == null) {                if (!isFinishedReading.value) {                    Text(text = "Mark as Read")                } else {                    Text(                        text = "Finished Reading!",                        modifier = Modifier.alpha(0.6f),                        color = Color.Red.copy(alpha = 0.6f)                    )                }            } else {                Text(                    text = "Finished on: ${book.finishedReading}",                    style = MaterialTheme.typography.bodyLarge.copy(                        fontWeight = FontWeight.Bold                    )                )            }        }    }    Text(text = "Rating", modifier = Modifier.padding(bottom = 3.dp))    book?.rating?.toInt().let {        RatingBar(rating = it!!) { rating ->            ratingVal.value = rating        }    }    Spacer(modifier = Modifier.padding(bottom = 15.dp))    Row() {        val changedNotes = book?.notes != notesText.value        val changedRating = book?.rating?.toInt() != ratingVal.value        val isFinishedTimeStamp =            if (isFinishedReading.value) Timestamp.now() else book?.finishedReading        val isStartedTimeStamp =            if (isStartedReading.value) Timestamp.now() else book?.startedReading        val bookUpdate =            changedNotes || changedRating || isStartedReading.value || isFinishedReading.value        val bookToUpdate = hashMapOf(            "finished_reading_at" to isFinishedTimeStamp,            "started_reading_at" to isStartedTimeStamp,            "rating" to ratingVal.value,            "notes" to notesText.value        ).toMap()        RoundedButton(label = "Update") {            if (bookUpdate) {                FirebaseFirestore.getInstance().collection("books")                    .document(book?.id!!)                    .update(bookToUpdate)                    .addOnCompleteListener() { task ->                        Log.d("INFO", "task.isSuccessful: ${task.result.toString()}")                        navController.popBackStack()                    }                    .addOnFailureListener {                        Log.d("INFO", "Book update failed")                    }            } else {                navController.popBackStack()            }        }        Spacer(modifier = Modifier.width(100.dp))        RoundedButton(label = "Delete") {        }    }}@OptIn(ExperimentalComposeUiApi::class)@Composablefun SimpleForm(    modifier: Modifier = Modifier,    loading: Boolean = false,    defaultValue: String = "Great Book!",    onSearch: (String) -> Unit,) {    val textFieldValue = rememberSaveable { mutableStateOf(defaultValue) }    val keyboardController = LocalSoftwareKeyboardController.current    val valid = remember {        textFieldValue.value.trim().isNotEmpty()    }    InputField(        modifier = modifier            .fillMaxWidth()            .height(140.dp)            .padding(3.dp)            .background(                color = MaterialTheme.colorScheme.surface,                shape = CircleShape            )            .padding(horizontal = 20.dp, vertical = 12.dp),        valueState = textFieldValue,        labelId = "Enter Your Thoughts",        enabled = true,        onAction = KeyboardActions {            if (!valid) return@KeyboardActions            onSearch(textFieldValue.value.trim())            keyboardController?.hide()        })}@Composablefun ShowBookUpdate(bookInfo: DataOrException<List<MBook>, Boolean, Exception>, bookItemId: String) {    Row() {        Spacer(modifier = Modifier.width(45.dp))        if (bookInfo.data != null) {            Column(modifier = Modifier.padding(4.dp), verticalArrangement = Arrangement.Center) {                CardListItem(book = bookInfo.data!!.first { mBook ->                    mBook.googleBookId == bookItemId                }, onPressDetails = {                    Log.d("INFO", "bookInfo.data.size: ${bookInfo.data.toString()}")                })            }        }    }}@OptIn(ExperimentalMaterial3Api::class)@Composablefun CardListItem(book: MBook, onPressDetails: () -> Int) {    Card(        modifier = Modifier            .padding(                start = 4.dp,                end = 4.dp,                top = 4.dp,                bottom = 8.dp            )            .clip(RoundedCornerShape(10.dp))            .clickable {            },        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),        ) {        Row(            horizontalArrangement = Arrangement.Start,            modifier = Modifier.background(MaterialTheme.colorScheme.surface)        ) {            Image(                painter = rememberAsyncImagePainter(model = book.photoUrl.toString()),                contentDescription = "Book Cover",                modifier = Modifier                    .height(100.dp)                    .width(120.dp)                    .padding(4.dp)                    .clip(                        RoundedCornerShape(                            topStart = 120.dp,                            topEnd = 20.dp,                            bottomEnd = 0.dp,                            bottomStart = 0.dp,                        )                    )            )            Column {                Text(                    text = book.title.toString(),                    style = MaterialTheme.typography.titleSmall,                    modifier = Modifier                        .padding(start = 8.dp, end = 8.dp)                        .width(120.dp),                    fontWeight = FontWeight.SemiBold,                    maxLines = 2,                    overflow = TextOverflow.Ellipsis                )                Text(                    text = book.authors.toString(),                    style = MaterialTheme.typography.titleSmall,                    modifier = Modifier                        .padding(start = 8.dp, end = 8.dp, top = 2.dp, bottom = 0.dp),                )                Text(                    text = book.publishedDate.toString(),                    style = MaterialTheme.typography.bodyMedium,                    modifier = Modifier.padding(                        start = 8.dp,                        end = 8.dp,                        top = 0.dp,                        bottom = 8.dp                    ),                )                //Text(text = book.description.toString(), style = MaterialTheme.typography.bodyLarge)            }        }    }}