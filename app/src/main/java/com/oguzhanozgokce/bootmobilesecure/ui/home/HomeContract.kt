package com.oguzhanozgokce.bootmobilesecure.ui.home

import android.net.Uri
import com.oguzhanozgokce.bootmobilesecure.domain.model.User

object HomeContract {
    data class UiState(
        val isLoading: Boolean = false,
        val user: User? = null,
        val greeting: String = "",
        val quickActions: List<QuickAction> = emptyList(),
        val isUploadingImage: Boolean = false,
        val showImagePickerDialog: Boolean = false,
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
        object AvatarClicked : UiAction
        object DismissImagePickerDialog : UiAction
        data class QuickActionClicked(val actionId: String) : UiAction
        data class ImageSelected(val uri: Uri) : UiAction
    }

    sealed interface UiEffect {
        object NavigateToLogin : UiEffect
        object NavigateToProfile : UiEffect
        object NavigateToSettings : UiEffect
        data class ShowError(val message: String) : UiEffect
        data class ShowSuccess(val message: String) : UiEffect
    }
}