package com.practice.readerapp.screens.loginimport android.util.Logimport androidx.compose.foundation.backgroundimport androidx.compose.foundation.clickableimport androidx.compose.foundation.layout.*import androidx.compose.foundation.rememberScrollStateimport androidx.compose.foundation.shape.CircleShapeimport androidx.compose.foundation.text.KeyboardActionsimport androidx.compose.foundation.verticalScrollimport androidx.compose.material3.*import androidx.compose.runtime.Composableimport androidx.compose.runtime.mutableStateOfimport androidx.compose.runtime.rememberimport androidx.compose.runtime.saveable.rememberSaveableimport androidx.compose.ui.Alignmentimport androidx.compose.ui.ExperimentalComposeUiApiimport androidx.compose.ui.Modifierimport androidx.compose.ui.draw.clipimport androidx.compose.ui.focus.FocusRequesterimport androidx.compose.ui.focus.focusRequesterimport androidx.compose.ui.platform.LocalSoftwareKeyboardControllerimport androidx.compose.ui.res.stringResourceimport androidx.compose.ui.text.font.FontWeightimport androidx.compose.ui.unit.dpimport androidx.navigation.NavControllerimport com.practice.readerapp.Rimport com.practice.readerapp.components.EmailInputimport com.practice.readerapp.components.PasswordInputimport com.practice.readerapp.components.ReaderLogoimport com.practice.readerapp.navigation.ReaderScreensimport com.practice.readerapp.ui.theme.COLOR_APP_PRIMARY@Composablefun ReaderLoginScreen(    navController: NavController,    viewModel: LoginScreenViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {    val showLoginForm = rememberSaveable { mutableStateOf(true) }    Surface(modifier = Modifier.fillMaxSize()) {        Column(            horizontalAlignment = Alignment.CenterHorizontally,            verticalArrangement = Arrangement.Top        ) {            ReaderLogo()            if (showLoginForm.value) {                UserForm(loading = false, isCreateAccount = false) { email, password ->                    viewModel.signInWithEmailAndPassword(email, password){                        navController.navigate(ReaderScreens.ReaderHomeScreen.name)                    }                }            } else {                UserForm(loading = false, isCreateAccount = true) { email, password ->                    viewModel.createUserWithEmailAndPassword(email, password){                        navController.navigate(ReaderScreens.ReaderHomeScreen.name)                    }                }            }            Spacer(modifier = Modifier.height(16.dp))            Row(                modifier = Modifier.padding(16.dp),                horizontalArrangement = Arrangement.Center,                verticalAlignment = Alignment.CenterVertically            ) {                val text = if (showLoginForm.value) "Sign Up" else "Login"                Text(text = "New User?")                Text(                    text = text,                    modifier = Modifier                        .clickable {                            showLoginForm.value = !showLoginForm.value                        }                        .padding(start = 8.dp),                    fontWeight = FontWeight.Bold,                    color = COLOR_APP_PRIMARY                )            }        }    }}@OptIn(ExperimentalComposeUiApi::class)//@Preview@Composablefun UserForm(    loading: Boolean = false,    isCreateAccount: Boolean = false,    onDone: (String, String) -> Unit = { email, pwd -> },) {    val email = rememberSaveable { mutableStateOf("") }    val password = rememberSaveable { mutableStateOf("") }    val passwordVisibility = remember { mutableStateOf(false) }    val passwordFocusRequest = FocusRequester.Default    val keyboardController = LocalSoftwareKeyboardController.current    val valid = remember(email.value, password.value) {        email.value.trim().isNotEmpty() && password.value.trim().isNotEmpty()    }    val modifier = Modifier        .height(250.dp)        .background(MaterialTheme.colorScheme.background)        .verticalScroll(rememberScrollState())    Column(        modifier,        horizontalAlignment = Alignment.CenterHorizontally    ) {        if (isCreateAccount) Text(            text = stringResource(id = R.string.create_acct),            modifier = Modifier.padding(start = 16.dp, end = 16.dp)        ) else Text(text = "")        EmailInput(            emailState = email,            enabled = !loading,            onAction = KeyboardActions {                passwordFocusRequest.requestFocus()            })        PasswordInput(            modifier = Modifier.focusRequester(passwordFocusRequest),            passwordState = password,            label = "Password",            enabled = !loading,            passwordVisibility = passwordVisibility,            onAction = KeyboardActions {                if (!valid) return@KeyboardActions                onDone(email.value.trim(), password.value.trim())            })        SubmitButton(            textId = if (isCreateAccount) "Create Account" else "Login",            loading = loading,            validInputs = valid        ) {            onDone(email.value.trim(), password.value.trim())            keyboardController?.hide()        }    }}@Composablefun SubmitButton(    textId: String,    loading: Boolean,    validInputs: Boolean,    onClick: () -> Unit) {    Button(        onClick = onClick,        modifier = Modifier            .padding(8.dp)            .fillMaxWidth(),        enabled = validInputs && !loading,        shape = CircleShape    ) {        if (loading) CircularProgressIndicator(modifier = Modifier.size(24.dp))        else Text(text = textId, modifier = Modifier.padding(5.dp))    }}