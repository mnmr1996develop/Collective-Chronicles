package com.michaelrichards.collectivechronicles.screens.loginScreen

import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.michaelrichards.collectivechronicles.R
import com.michaelrichards.collectivechronicles.components.AuthTextField
import com.michaelrichards.collectivechronicles.navigation.Screens


@Composable
fun LoginScreen(
    navController: NavController,
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
                    painter = painterResource(id = if(darkTheme) R.drawable.collective_chronicles_logo_transparent else R.drawable.collective_chronicles_logo_black_transparent),
                    contentDescription = "Site Logo"
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
                    label = "username",
                    usernameCharactersOnly = true,
                    maxCharacters = 20,
                    imeAction = ImeAction.Go,
                    keyboardActions = KeyboardActions(onGo = { /*TODO*/ })
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
                    label = "password",
                    usernameCharactersOnly = true,
                    maxCharacters = 20,
                    visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation()
                ) {

                    Icon(
                        modifier = Modifier.clickable { showPassword = !showPassword },
                        imageVector = if (showPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = if (showPassword) "Hide Password" else "Show Password"
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { /*TODO*/ }) {
                    Text(modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),text = "Login", style = MaterialTheme.typography.bodyMedium)
                }
            }
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.Bottom) {
                OutlinedButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { navController.navigate(Screens.RegistrationScreen.routeName) }) {
                    Text(
                        modifier = Modifier.padding(8.dp),
                        text = "Sign Up",
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