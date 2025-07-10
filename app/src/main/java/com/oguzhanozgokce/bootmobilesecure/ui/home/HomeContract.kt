package com.oguzhanozgokce.bootmobilesecure.ui.home

import com.oguzhanozgokce.bootmobilesecure.domain.model.User

object HomeContract {
    data class UiState(
        val isLoading: Boolean = false,
        val user: User? = null,
        val greeting: String = "",
        val quickActions: List<QuickAction> = emptyList(),
    )

    data class QuickAction(
        val id: String,
        val title: String,
        val description: String,
        val icon: String,
        val action: String
    )

    sealed interface UiAction {
        object LoadUserData : UiAction
        object RefreshUserData : UiAction
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