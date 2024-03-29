package com.michaelrichards.collectivechronicles.screens.registrationScreen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.michaelrichards.collectivechronicles.R
import com.michaelrichards.collectivechronicles.components.AuthTextField
import com.michaelrichards.collectivechronicles.dtos.requests.RegistrationRequest
import com.michaelrichards.collectivechronicles.navigation.Graphs
import com.michaelrichards.collectivechronicles.repositories.results.AuthenticationResults
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationScreen(
    navController: NavController,
    viewmodel: RegistrationViewModel = hiltViewModel(),
    darkTheme: Boolean = isSystemInDarkTheme()
) {

    val context = LocalContext.current

    val firstNameError = remember {
        mutableStateOf(false)
    }

    val lastNameError = remember {
        mutableStateOf(false)
    }

    val usernameError = remember {
        mutableStateOf(false)
    }

    val emailError = remember {
        mutableStateOf(false)
    }

    val passwordError = remember {
        mutableStateOf(false)
    }

    val birthdayError = remember {
        mutableStateOf(false)
    }


    LaunchedEffect(viewmodel.authResults, context) {
        viewmodel.authResults.collect {results ->
            when (results) {
                is AuthenticationResults.Authenticated -> {
                    navController.navigate(Graphs.MainGraph.graphName){
                        popUpTo(Graphs.AuthGraph.graphName){
                            inclusive = true
                        }
                    }
                }

                is AuthenticationResults.Unauthenticated -> {
                    Toast.makeText(context, results.data, Toast.LENGTH_LONG).show()
                    when(results.data){
                        "firstName" -> {
                            firstNameError.value = true
                        }
                        "lastName" -> {
                            lastNameError.value = true
                        }
                        "username" -> {
                            usernameError.value = true
                        }
                        "email" -> {
                            emailError.value = true
                        }
                        "password" -> {
                            passwordError.value = true
                        }
                        "birthday" -> {
                            birthdayError.value = true
                        }
                    }
                }
                is AuthenticationResults.Loading -> {

                }
                is AuthenticationResults.TimeOutError -> {
                   Toast.makeText(context, R.string.connection_error, Toast.LENGTH_LONG).show()
                }
                is AuthenticationResults.UnknownError -> {
                    Toast.makeText(context, R.string.something_went_wrong, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    val firstName = rememberSaveable {
        mutableStateOf("")
    }

    val lastName = rememberSaveable {
        mutableStateOf("")
    }

    val username = rememberSaveable {
        mutableStateOf("")
    }

    val email = rememberSaveable {
        mutableStateOf("")
    }

    val password = rememberSaveable {
        mutableStateOf("")
    }

    var showPassword by rememberSaveable {
        mutableStateOf(false)
    }



    Scaffold(
        modifier = Modifier.padding(8.dp),
        topBar = { TopAppBar(title = { Text(text = "") }) }
    ) { paddingValues ->
        Surface(modifier = Modifier.padding(paddingValues = paddingValues)) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Image(
                    modifier = Modifier
                        .height(200.dp)
                        .fillMaxWidth(),
                    painter = painterResource(id = if (darkTheme) R.drawable.collective_chronicles_logo_transparent else R.drawable.collective_chronicles_logo_black_transparent),
                    contentDescription = stringResource(id = R.string.site_logo)
                )

                AuthTextField(
                    modifier = Modifier.fillMaxWidth(),
                    textValueState = firstName,
                    label = stringResource(id = R.string.first_name),
                    maxCharacters = 20,
                    isError = firstNameError,
                    onlyLetters = true,
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ) {
                    Icon(
                        imageVector = Icons.Filled.AccountCircle,
                        contentDescription = stringResource(
                            id = R.string.first_name_field
                        )
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                AuthTextField(
                    modifier = Modifier.fillMaxWidth(),
                    textValueState = lastName,
                    label = stringResource(id = R.string.last_name),
                    maxCharacters = 20,
                    isError = lastNameError,
                    onlyLetters = true,
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ) {
                    Icon(
                        imageVector = Icons.Filled.AccountCircle,
                        contentDescription = stringResource(
                            id = R.string.first_name_field
                        )
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                AuthTextField(
                    modifier = Modifier.fillMaxWidth(),
                    textValueState = username,
                    label = stringResource(id = R.string.username),
                    maxCharacters = 20,
                    isError = usernameError,
                    usernameCharactersOnly = true,
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ) {
                    Icon(
                        imageVector = Icons.Filled.AccountCircle,
                        contentDescription = stringResource(
                            id = R.string.username_field
                        )
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                AuthTextField(
                    modifier = Modifier.fillMaxWidth(),
                    textValueState = email,
                    label = stringResource(id = R.string.email),
                    maxCharacters = 20,
                    isError = emailError,
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ) {
                    Icon(
                        imageVector = Icons.Filled.Email,
                        contentDescription = stringResource(
                            id = R.string.email_field
                        )
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                AuthTextField(
                    modifier = Modifier.fillMaxWidth(),
                    textValueState = password,
                    label = stringResource(id = R.string.password),
                    maxCharacters = 20,
                    isError = passwordError,
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next,
                    visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation()
                ) {
                    Icon(
                        modifier = Modifier.clickable { showPassword = !showPassword },
                        imageVector = if (showPassword) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                        contentDescription = stringResource(
                            id = if (showPassword) R.string.hide_password else R.string.show_password
                        )
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    onClick = {
                        register(
                            firstName = firstName.value,
                            lastName = lastName.value,
                            username = username.value,
                            email = email.value,
                            birthday = LocalDate.now(),
                            password = password.value,
                            viewmodel = viewmodel
                        )
                    }) {
                    Text(text = stringResource(id = R.string.register))
                }

            }
        }
    }
}

private fun register(
    firstName: String,
    lastName: String,
    username: String,
    email: String,
    password: String,
    birthday: LocalDate,
    viewmodel: RegistrationViewModel
) {
    val registrationRequest = RegistrationRequest(
        firstName = firstName,
        lastName = lastName,
        username = username,
        email = email,
        password = password,
        birthday = birthday
    )
    viewmodel.register(registrationRequest = registrationRequest)
}