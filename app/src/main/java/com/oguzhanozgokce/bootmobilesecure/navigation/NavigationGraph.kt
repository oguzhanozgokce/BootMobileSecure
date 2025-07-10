package com.oguzhanozgokce.bootmobilesecure.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.oguzhanozgokce.bootmobilesecure.navigation.Screen.*
import com.oguzhanozgokce.bootmobilesecure.ui.home.HomeScreen
import com.oguzhanozgokce.bootmobilesecure.ui.home.HomeViewModel
import com.oguzhanozgokce.bootmobilesecure.ui.login.LoginScreen
import com.oguzhanozgokce.bootmobilesecure.ui.login.LoginViewModel
import com.oguzhanozgokce.bootmobilesecure.ui.password.PasswordScreen
import com.oguzhanozgokce.bootmobilesecure.ui.password.PasswordViewModel
import com.oguzhanozgokce.bootmobilesecure.ui.register.RegisterScreen
import com.oguzhanozgokce.bootmobilesecure.ui.register.RegisterViewModel
import com.oguzhanozgokce.bootmobilesecure.ui.splash.SplashScreen
import com.oguzhanozgokce.bootmobilesecure.ui.splash.SplashViewModel

@Composable
fun NavigationGraph(
    navController: NavHostController,
    startDestination: Screen,
    modifier: Modifier = Modifier,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination,
    ) {
        composable<Splash> {
            val viewModel = viewModel<SplashViewModel>(it)
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val uiEffect = viewModel.uiEffect
            SplashScreen(
                uiState = uiState,
                uiEffect = uiEffect,
                onAction = viewModel::onAction
            )
        }
        composable<Login> {
            val viewModel = viewModel<LoginViewModel>(it)
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val uiEffect = viewModel.uiEffect
            LoginScreen(
                uiState = uiState,
                uiEffect = uiEffect,
                onAction = viewModel::onAction
            )
        }
        composable<Register> {
            val viewModel = viewModel<RegisterViewModel>(it)
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val uiEffect = viewModel.uiEffect
            RegisterScreen(
                uiState = uiState,
                uiEffect = uiEffect,
                onAction = viewModel::onAction
            )
        }
        composable<Password> {
            val viewModel = viewModel<PasswordViewModel>(it)
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val uiEffect = viewModel.uiEffect
            PasswordScreen(
                uiState = uiState,
                uiEffect = uiEffect,
                onAction = viewModel::onAction
            )
        }
        composable<Home> {
            val viewModel = viewModel<HomeViewModel>(it)
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val uiEffect = viewModel.uiEffect
            HomeScreen(
                uiState = uiState,
                uiEffect = uiEffect,
                onAction = viewModel::onAction
            )
        }
    }
}