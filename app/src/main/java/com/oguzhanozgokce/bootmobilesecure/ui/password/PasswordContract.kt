package com.oguzhanozgokce.bootmobilesecure.ui.password

object PasswordContract {
    data class UiState(
        val isLoading: Boolean = false,
        val list: List<String> = emptyList(),
    )

    sealed interface UiAction

    sealed interface UiEffect
}