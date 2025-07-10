package com.oguzhanozgokce.bootmobilesecure.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oguzhanozgokce.bootmobilesecure.data.network.TokenManager
import com.oguzhanozgokce.bootmobilesecure.delegation.MVI
import com.oguzhanozgokce.bootmobilesecure.delegation.mvi
import com.oguzhanozgokce.bootmobilesecure.ui.splash.SplashContract.UiAction
import com.oguzhanozgokce.bootmobilesecure.ui.splash.SplashContract.UiEffect
import com.oguzhanozgokce.bootmobilesecure.ui.splash.SplashContract.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val tokenManager: TokenManager
) : ViewModel(), MVI<UiState, UiAction, UiEffect> by mvi(UiState()) {

    init {
        checkAuthenticationStatus()
    }

    private fun checkAuthenticationStatus() {
        viewModelScope.launch {
            updateUiState { copy(isLoading = true) }

            try {
                delay(1000)
                val isLoggedIn = tokenManager.isLoggedIn()
                updateUiState { copy(isLoading = false) }
                if (isLoggedIn) {
                    emitUiEffect(UiEffect.NavigateToHome)
                } else {
                    emitUiEffect(UiEffect.NavigateToLogin)
                }

            } catch (e: Exception) {
                updateUiState { copy(isLoading = false) }
                emitUiEffect(UiEffect.ShowError("Authentication check failed"))
            }
        }
    }
}
