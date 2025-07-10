package com.oguzhanozgokce.bootmobilesecure.ui.home

object HomeContract {
    data class UiState(
        val isLoading: Boolean = false,
        val user: User? = null,
        val greeting: String = "",
        val quickActions: List<QuickAction> = emptyList(),
    )

    data class User(
        val id: String = "",
        val username: String = "",
        val email: String = "",
        val firstName: String = "",
        val lastName: String = "",
        val avatar: String? = null,
        val joinDate: String = "",
        val lastLogin: String = "",
    )

    data class QuickAction(
        val id: String,
        val title: String,
        val description: String,
        val icon: String,
        val action: String
    )

    sealed interface UiAction {
        object LogoutClicked : UiAction
        object ProfileClicked : UiAction
        object SettingsClicked : UiAction
        data class QuickActionClicked(val actionId: String) : UiAction
    }

    sealed interface UiEffect {
        object NavigateToLogin : UiEffect
        object NavigateToProfile : UiEffect
        object NavigateToSettings : UiEffect
        data class ShowError(val message: String) : UiEffect
        data class ShowSuccess(val message: String) : UiEffect
    }
}