package com.practice.readerapp.screens.homeimport android.annotation.SuppressLintimport androidx.annotation.DrawableResimport androidx.compose.foundation.Imageimport androidx.compose.foundation.layout.*import androidx.compose.foundation.shape.RoundedCornerShapeimport androidx.compose.material.icons.Iconsimport androidx.compose.material.icons.filled.Addimport androidx.compose.material.icons.filled.ExitToAppimport androidx.compose.material.icons.filled.Favoriteimport androidx.compose.material.icons.filled.Logoutimport androidx.compose.material3.*import androidx.compose.runtime.Composableimport androidx.compose.ui.Alignmentimport androidx.compose.ui.Modifierimport androidx.compose.ui.draw.clipimport androidx.compose.ui.draw.scaleimport androidx.compose.ui.graphics.Colorimport androidx.compose.ui.res.painterResourceimport androidx.compose.ui.text.TextStyleimport androidx.compose.ui.text.font.FontWeightimport androidx.compose.ui.unit.dpimport androidx.compose.ui.unit.spimport androidx.navigation.NavControllerimport com.google.firebase.auth.FirebaseAuthimport com.practice.readerapp.Rimport com.practice.readerapp.model.MBookimport com.practice.readerapp.navigation.ReaderScreensimport com.practice.readerapp.ui.theme.COLOR_APP_PRIMARY@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")@ExperimentalMaterial3Api@Composablefun Home(navController: NavController) {    Scaffold(topBar = {        ReaderAppBar(title = "Reader App", navController = navController)    },        floatingActionButton = {            FABContent {            }        }    ) {        Surface(modifier = Modifier.fillMaxSize()) {        }    }}@SuppressLint("ResourceType")@Composablefun ReaderAppBar(    title: String,    showProfile: Boolean = true,    navController: NavController) {    SmallTopAppBar(        title = {            Row(verticalAlignment = Alignment.CenterVertically) {                if (showProfile) {                    Icon(                        imageVector = Icons.Default.Favorite,                        contentDescription = "Logo Icon",                        modifier = Modifier                            .clip(RoundedCornerShape(12.dp))                            .scale(0.6f)                    )                    /*Image(                        painter = painterResource(                            id = R.drawable.ic_launcher_img                        ),                        contentDescription = "Icon"                    )*/                }                Text(                    text = title,                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp)                )                Spacer(modifier = Modifier.width(150.dp))            }        },        actions = {            IconButton(onClick = {                FirebaseAuth.getInstance().signOut().run {                    navController.navigate(ReaderScreens.LoginScreen.name)                }            }) {                Icon(                    imageVector = Icons.Default.Logout,                    contentDescription = "Logout",                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)                )            }        },        )}@Composablefun ReadingRightNowArea (books: List<MBook>, navController: NavController){}@Composablefun FABContent(onTap: () -> Unit) {    FloatingActionButton(        onClick = { onTap() },        shape = RoundedCornerShape(50.dp),        containerColor =  COLOR_APP_PRIMARY    ) {        Icon(imageVector = Icons.Default.Add,            contentDescription = "Add a Book", tint = Color.White)    }}