package com.michaelrichards.collectivechronicles.screens.splashScreen

import android.widget.Toast
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.michaelrichards.collectivechronicles.R
import com.michaelrichards.collectivechronicles.navigation.Graphs
import com.michaelrichards.collectivechronicles.repositories.results.AuthenticationResults


@Composable
fun SplashScreen(
    navController: NavController,
    viewModel: SplashScreenViewModel = hiltViewModel(),
    darkTheme: Boolean = isSystemInDarkTheme()
) {

    val scale = remember {
        Animatable(0f)
    }

    val context = LocalContext.current

    val infiniteTransition = rememberInfiniteTransition("")

    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -360f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 6000,
                easing = FastOutLinearInEasing,
            ),
        ), label = "logo rotate"
    )

    var tryAgainButton by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(viewModel, context) {
        viewModel.authResults.collect { res ->
            when (res) {
                is AuthenticationResults.Authenticated -> {
                    navController.navigate(Graphs.MainGraph.graphName) {
                        popUpTo(Graphs.SplashGraph.graphName) {
                            inclusive = true
                        }
                    }
                }

                is AuthenticationResults.TimeOutError -> {
                    Toast.makeText(context, R.string.connection_error, Toast.LENGTH_LONG).show()
                    tryAgainButton = true
                }

                else -> {
                    navController.navigate(Graphs.AuthGraph.graphName) {
                        popUpTo(Graphs.SplashGraph.graphName) {
                            inclusive = true
                        }
                    }
                }
            }
        }

    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                modifier = Modifier.rotate(rotation),
                painter = painterResource(
                    id = if (darkTheme) R.drawable.collective_chronicles_logo_transparent
                            else R.drawable.collective_chronicles_logo_black_transparent),
                    contentDescription = null
                )

            if (tryAgainButton){
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = {
                    tryAgainButton = false
                    viewModel.authenticate()
                }) {
                    Text(text = stringResource(id = R.string.try_again))

                }
            }
        }
    }


}

@Preview
@Composable
fun PrevSplash(modifier: Modifier = Modifier) {
    SplashScreen(NavController(LocalContext.current))
}