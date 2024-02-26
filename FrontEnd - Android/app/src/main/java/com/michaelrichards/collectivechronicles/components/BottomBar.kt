package com.michaelrichards.collectivechronicles.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.TravelExplore
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.TravelExplore
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.michaelrichards.collectivechronicles.navigation.Screens


@Composable
fun BottomBar(modifier: Modifier = Modifier, navController: NavController, selectedValue: Int) {


    val icons = mutableListOf(
        BottomRowIcon(
            "Find Stories",
            "Find Stories",
            Icons.Filled.TravelExplore,
            Icons.Outlined.TravelExplore
        ) {
            navController.navigate(Screens.FindStoriesScreen.routeName)
        },
        BottomRowIcon(
            "Home",
            "Home",
            Icons.Filled.Home,
            Icons.Outlined.Home
        ) {
            /*if (navController.currentDestination == Screens.HomeScreen.routeName)
            navController.navigate(Screens.HomeScreen.routeName)
            else{
                navController.popBackStack(Screens.HomeScreen.routeName, true, saveState = true)
            }*/
        },
        BottomRowIcon(
            "Account",
            "Account Screen",
            Icons.Filled.Person,
            Icons.Outlined.Person
        ) {
            navController.navigate(Screens.AccountScreen.routeName)
        }
    )


    BottomAppBar(
        modifier = modifier.clip(RoundedCornerShape(topEnd = 12.dp, topStart = 12.dp))
    ) {
        TabRow(selectedTabIndex = selectedValue) {
            icons.forEachIndexed { index, bottomRowIcon ->
                Tab(selected = selectedValue%icons.size == index, onClick = { bottomRowIcon.onClick() }, modifier = Modifier.padding(15.dp)) {
                    Column(modifier = Modifier.fillMaxHeight(), horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = if (selectedValue % icons.size == index) bottomRowIcon.selectedIcon else bottomRowIcon.unselectedIcon,
                            contentDescription = bottomRowIcon.description
                        )
                        Text(text = bottomRowIcon.rowName, style = MaterialTheme.typography.labelMedium)
                    }
                }
            }
        }
    }
}

data class BottomRowIcon(
    val rowName: String,
    val description: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val onClick: () -> Unit
)

@Preview
@Composable
private fun PrevBottomBar() {
    BottomBar(navController = NavController(LocalContext.current), selectedValue = 1)
}