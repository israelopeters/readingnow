package com.example.readingnow

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.readingnow.service.UserViewModel
import com.example.readingnow.ui.components.AppAlertDialog
import com.example.readingnow.ui.components.AppScreen
import com.example.readingnow.ui.components.CustomBottomAppBar
import com.example.readingnow.ui.components.CustomTopAppBar
import com.example.readingnow.ui.screens.HomeScreen
import com.example.readingnow.ui.screens.SignUpScreen
import com.example.readingnow.ui.screens.WelcomeScreen

@Composable
fun ReadingNowApp(
    modifier: Modifier = Modifier,
    userViewModel: UserViewModel = hiltViewModel(),
    navController: NavHostController = rememberNavController()
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = AppScreen.valueOf(
        backStackEntry?.destination?.route ?: AppScreen.Welcome.name
    )
    var openSignOutAlertDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            val noTopBarScreens = listOf(
                AppScreen.Welcome.name,
                AppScreen.SignUp.name,
            )
            if (!noTopBarScreens.contains(currentScreen.name)) {
                CustomTopAppBar(
                    currentScreen = currentScreen,
                    canNavigate = // Remove back back button from home screen
                        if (currentScreen.name == AppScreen.Home.name) {
                            false
                        } else {
                            navController.previousBackStackEntry != null
                        },
                    navigateUp = {  },
                    onSignOutClicked = {
                        openSignOutAlertDialog = true
                    }
                )
            }
        },
        bottomBar = {
            val noBottomAppBarScreens = listOf(
                AppScreen.Welcome.name,
                AppScreen.SignUp.name,
            )
            if (!noBottomAppBarScreens.contains(currentScreen.name)) {
                CustomBottomAppBar(
                    onHomeClicked = { navController.navigate(AppScreen.Home.name) },
                    onProfileClicked = { }
                )
            }
        },
        modifier = modifier

    ) { innerPadding ->

        when {
            openSignOutAlertDialog -> {
                AppAlertDialog(
                    onDismissRequest = { openSignOutAlertDialog = false },
                    onConfirmation = {
                        openSignOutAlertDialog = false
                        userViewModel.signOut()
                        navController.navigate(AppScreen.Welcome.name)
                    },
                    dialogTitle = stringResource(R.string.sign_out_confirmation),
                    dialogText = stringResource(R.string.sign_out_confirmation_text),
                    icon = Icons.Filled.Info,
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }

        NavHost(
            navController = navController,
            startDestination = AppScreen.Welcome.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = AppScreen.Welcome.name) {
                WelcomeScreen(
                    userViewModel = userViewModel,
                    onContinueClicked = { navController.navigate(AppScreen.Home.name) },
                    onSignUpClicked = { navController.navigate(AppScreen.SignUp.name) },
                    modifier = modifier.fillMaxSize()
                )
            }
            composable(route = AppScreen.Home.name) {
                HomeScreen(userViewModel = userViewModel)
            }
            composable(route = AppScreen.SignUp.name) {
                SignUpScreen(
                    userViewModel = userViewModel,
                    onSignInClicked = {
                        userViewModel.clearAddedUser()
                        navController.navigate(AppScreen.Welcome.name)
                    }
                )
            }
        }
    }
}