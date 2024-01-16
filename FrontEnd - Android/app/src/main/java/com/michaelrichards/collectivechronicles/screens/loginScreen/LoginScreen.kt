package com.michaelrichards.collectivechronicles.screens.loginScreen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.michaelrichards.collectivechronicles.R
import com.michaelrichards.collectivechronicles.components.AuthTextField
import com.michaelrichards.collectivechronicles.dtos.requests.AuthenticationRequest
import com.michaelrichards.collectivechronicles.navigation.Graphs
import com.michaelrichards.collectivechronicles.navigation.Screens
import com.michaelrichards.collectivechronicles.repositories.results.AuthenticationResults


@Composable
fun LoginScreen(
    navController: NavController,
    viewmodel: LoginViewModel = hiltViewModel(),
    darkTheme: Boolean = isSystemInDarkTheme()
) {

    val username = rememberSaveable {
        mutableStateOf("")
    }

    val password = rememberSaveable {
        mutableStateOf("")
    }

    var showPassword by rememberSaveable {
        mutableStateOf(false)
    }

    var error by remember {
        mutableStateOf(false)
    }

    var enabled by remember {
        mutableStateOf(true)
    }


    val context = LocalContext.current
    LaunchedEffect(viewmodel, context) {
        viewmodel.authResults.collect {
            when (it) {
                is AuthenticationResults.Authenticated -> {
                    navController.navigate(Graphs.MainGraph.graphName){
                        popUpTo(Graphs.AuthGraph.graphName){
                            inclusive = true
                        }
                    }
                }

                is AuthenticationResults.BadAuthenticationData -> {
                    error = true
                    username.value = ""
                    password.value = ""
                    Toast.makeText(
                        context,
                        R.string.incorrect_username_or_password,
                        Toast.LENGTH_LONG
                    ).show()
                    enabled = true
                }

                is AuthenticationResults.Loading -> {
                    enabled = false
                }

                is AuthenticationResults.TimeOutError -> {
                    Toast.makeText(context, R.string.connection_error, Toast.LENGTH_LONG).show()
                    enabled = true
                }

                is AuthenticationResults.UnknownError -> {
                    Toast.makeText(context, R.string.something_went_wrong, Toast.LENGTH_LONG).show()
                    enabled = true
                }
            }
        }
    }



    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        color = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground
    ) {

        Column {
            Box(
                modifier = Modifier
                    .weight(2f)
            ) {
                Image(
                    modifier = Modifier
                        .fillMaxSize()
                        .fillMaxWidth(),
                    painter = painterResource(id = if (darkTheme) R.drawable.collective_chronicles_logo_transparent else R.drawable.collective_chronicles_logo_black_transparent),
                    contentDescription = stringResource(id = R.string.site_logo)
                )
            }
            Column(
                modifier = Modifier.weight(5f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                AuthTextField(
                    modifier = Modifier.fillMaxWidth(),
                    textValueState = username,
                    isError = error,
                    label = stringResource(id = R.string.username),
                    usernameCharactersOnly = true,
                    maxCharacters = 20,
                    imeAction = ImeAction.Go,
                    keyboardActions = KeyboardActions(onGo = {
                        login(
                            username = username,
                            password = password,
                            viewmodel
                        )
                    })
                ) {

                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = null
                    )


                }

                Spacer(modifier = Modifier.height(16.dp))

                AuthTextField(
                    modifier = Modifier.fillMaxWidth(),
                    textValueState = password,
                    label = stringResource(id = R.string.password),
                    isError = error,
                    usernameCharactersOnly = true,
                    maxCharacters = 20,
                    visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation()
                ) {

                    Icon(
                        modifier = Modifier.clickable { showPassword = !showPassword },
                        imageVector = if (showPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = stringResource(id = if (showPassword) R.string.hide_password else R.string.show_password)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    ),
                    enabled = enabled,
                    onClick = { login(username = username, password = password, viewmodel) }
                ) {
                    Text(
                        modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
                        text = stringResource(id = R.string.login),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                if (!enabled) {
                    Spacer(modifier = Modifier.height(8.dp))
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                }
            }
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.Bottom) {
                OutlinedButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { navController.navigate(Screens.RegistrationScreen.routeName) }) {
                    Text(
                        modifier = Modifier.padding(8.dp),
                        text = stringResource(id = R.string.sign_up),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

    }
}

@Preview
@Composable
fun PreviewLoginScreen() {
    LoginScreen(navController = NavController(LocalContext.current))
}

private fun login(
    username: MutableState<String>,
    password: MutableState<String>,
    loginViewModel: LoginViewModel
) {
    loginViewModel.login(
        AuthenticationRequest(
            username = username.value,
            password = password.value
        )
    )
}