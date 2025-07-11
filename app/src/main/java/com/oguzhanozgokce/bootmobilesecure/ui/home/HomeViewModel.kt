package com.oguzhanozgokce.bootmobilesecure.ui.home

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oguzhanozgokce.bootmobilesecure.common.ImageUtils
import com.oguzhanozgokce.bootmobilesecure.data.network.TokenManager
import com.oguzhanozgokce.bootmobilesecure.data.network.getUserMessage
import com.oguzhanozgokce.bootmobilesecure.delegation.MVI
import com.oguzhanozgokce.bootmobilesecure.delegation.mvi
import com.oguzhanozgokce.bootmobilesecure.domain.repository.MainRepository
import com.oguzhanozgokce.bootmobilesecure.ui.home.HomeContract.QuickAction
import com.oguzhanozgokce.bootmobilesecure.ui.home.HomeContract.UiAction
import com.oguzhanozgokce.bootmobilesecure.ui.home.HomeContract.UiEffect
import com.oguzhanozgokce.bootmobilesecure.ui.home.HomeContract.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    private val tokenManager: TokenManager,
    @ApplicationContext private val context: Context
) : ViewModel(), MVI<UiState, UiAction, UiEffect> by mvi(UiState()) {

    init {
        updateGreeting()
        loadQuickActions()
        loadUserData()
    }

    override fun onAction(uiAction: UiAction) {
        viewModelScope.launch {
            when (uiAction) {
                UiAction.LoadUserData -> loadUserData()
                UiAction.RefreshUserData -> loadUserData(isRefresh = true)
                UiAction.LogoutClicked -> handleLogout()
                UiAction.ProfileClicked -> emitUiEffect(UiEffect.NavigateToProfile)
                UiAction.SettingsClicked -> emitUiEffect(UiEffect.NavigateToSettings)
                UiAction.AvatarClicked -> handleAvatarClicked()
                UiAction.DismissImagePickerDialog -> updateUiState { copy(showImagePickerDialog = false) }
                is UiAction.QuickActionClicked -> handleQuickAction(uiAction.actionId)
                is UiAction.ImageSelected -> handleImageSelected(uiAction.uri)
            }
        }
    }

    private fun loadUserData(isRefresh: Boolean = false) = viewModelScope.launch {
        updateUiState { copy(isLoading = true) }

        mainRepository.getCurrentUser()
            .onSuccess { user ->
                updateUiState { copy(user = user, isLoading = false) }
                Log.d("HomeViewModel", "User data loaded successfully: $user")
                if (isRefresh) {
                    emitUiEffect(UiEffect.ShowSuccess("Profile refreshed successfully"))
                }
            }
            .onFailure { exception ->
                updateUiState { copy(isLoading = false) }
                val errorMessage = (exception as? Exception ?: Exception(exception.message)).getUserMessage()
                emitUiEffect(UiEffect.ShowError(errorMessage))

                if (!tokenManager.isLoggedIn() || errorMessage.contains("401")) {
                    emitUiEffect(UiEffect.NavigateToLogin)
                }
            }
    }

    private fun updateGreeting() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)

        val greeting = when (hour) {
            in 0..11 -> "Good Morning"
            in 12..17 -> "Good Afternoon"
            else -> "Good Evening"
        }

        updateUiState { copy(greeting = greeting) }
    }

    private fun loadQuickActions() {
        val quickActions = listOf(
            QuickAction(
                id = "profile",
                title = "Edit Profile",
                description = "Update your personal information",
                icon = "👤",
                action = "profile"
            ),
            QuickAction(
                id = "settings",
                title = "Settings",
                description = "Configure app preferences",
                icon = "⚙️",
                action = "settings"
            ),
            QuickAction(
                id = "security",
                title = "Security",
                description = "Manage your account security",
                icon = "🔒",
                action = "security"
            ),
            QuickAction(
                id = "refresh",
                title = "Refresh Profile",
                description = "Reload your latest information",
                icon = "🔄",
                action = "refresh"
            ),
            QuickAction(
                id = "help",
                title = "Help & Support",
                description = "Get help and contact support",
                icon = "❓",
                action = "help"
            )
        )

        updateUiState { copy(quickActions = quickActions) }
    }

    private suspend fun handleLogout() {
        updateUiState { copy(isLoading = true) }

        try {
            tokenManager.clearToken()
            emitUiEffect(UiEffect.NavigateToLogin)
            emitUiEffect(UiEffect.ShowSuccess("Logged out successfully"))
        } catch (e: Exception) {
            emitUiEffect(UiEffect.ShowError("Logout failed"))
        } finally {
            updateUiState { copy(isLoading = false) }
        }
    }

    private suspend fun handleQuickAction(actionId: String) {
        when (actionId) {
            "profile" -> emitUiEffect(UiEffect.NavigateToProfile)
            "settings" -> emitUiEffect(UiEffect.NavigateToSettings)
            "security" -> emitUiEffect(UiEffect.ShowSuccess("Security settings opened"))
            "refresh" -> onAction(UiAction.RefreshUserData)
            "help" -> emitUiEffect(UiEffect.ShowSuccess("Help center opened"))
            else -> emitUiEffect(UiEffect.ShowError("Unknown action"))
        }
    }

    private fun handleAvatarClicked() {
        updateUiState { copy(showImagePickerDialog = true) }
    }

    private fun handleImageSelected(uri: Uri) = viewModelScope.launch {
        updateUiState { copy(isUploadingImage = true) }

        try {
            val tempFile = ImageUtils.createTempFileFromUri(context, uri)
            if (tempFile == null) {
                updateUiState { copy(isUploadingImage = false) }
                emitUiEffect(UiEffect.ShowError("Failed to process image"))
                return@launch
            }

            if (!ImageUtils.isValidImageFile(tempFile)) {
                updateUiState { copy(isUploadingImage = false) }
                emitUiEffect(UiEffect.ShowError("Invalid image file. Please select a valid image."))
                ImageUtils.cleanupTempFile(tempFile)
                return@launch
            }

            val processedFile = ImageUtils.compressImageIfNeeded(tempFile, 5 * 1024 * 1024) // 5MB limit
            if (processedFile == null) {
                updateUiState { copy(isUploadingImage = false) }
                emitUiEffect(UiEffect.ShowError("Failed to process image"))
                ImageUtils.cleanupTempFile(tempFile)
                return@launch
            }

            mainRepository.updateProfileImage(processedFile)
                .onSuccess { updatedUser ->
                    updateUiState {
                        copy(
                            user = updatedUser,
                            isUploadingImage = false
                        )
                    }
                    emitUiEffect(UiEffect.ShowSuccess("Profile image updated successfully"))
                }
                .onFailure { exception ->
                    updateUiState { copy(isUploadingImage = false) }
                    val errorMessage = exception.getUserMessage()
                    emitUiEffect(UiEffect.ShowError("Failed to upload image: $errorMessage"))

                    if (!tokenManager.isLoggedIn() || errorMessage.contains("401")) {
                        emitUiEffect(UiEffect.NavigateToLogin)
                    }
                }

            ImageUtils.cleanupTempFile(tempFile)
            if (processedFile != tempFile) {
                ImageUtils.cleanupTempFile(processedFile)
            }

        } catch (e: Exception) {
            updateUiState { copy(isUploadingImage = false) }
            emitUiEffect(UiEffect.ShowError("Error uploading image: ${e.message}"))
        }
    }
}