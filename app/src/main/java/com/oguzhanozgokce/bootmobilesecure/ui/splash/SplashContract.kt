package com.oguzhanozgokce.bootmobilesecure.ui.splash

object SplashContract {
    data class UiState(
        val isLoading: Boolean = false,
        val list: List<String> = emptyList(),
    )

    sealed interface UiAction

    sealed interface UiEffect
}