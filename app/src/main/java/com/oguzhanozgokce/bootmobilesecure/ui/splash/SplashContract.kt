package com.oguzhanozgokce.bootmobilesecure.ui.splash

object SplashContract {
    data class UiState(
        val isLoading: Boolean = true,
    )

    sealed interface UiAction

    sealed interface UiEffect {
        object NavigateToHome : UiEffect
        object NavigateToLogin : UiEffect
        data class ShowError(val message: String) : UiEffect
    }
}