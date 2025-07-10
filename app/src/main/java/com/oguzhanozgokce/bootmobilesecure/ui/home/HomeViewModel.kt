package com.oguzhanozgokce.bootmobilesecure.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oguzhanozgokce.bootmobilesecure.delegation.MVI
import com.oguzhanozgokce.bootmobilesecure.delegation.mvi
import com.oguzhanozgokce.bootmobilesecure.ui.home.HomeContract.QuickAction
import com.oguzhanozgokce.bootmobilesecure.ui.home.HomeContract.UiAction
import com.oguzhanozgokce.bootmobilesecure.ui.home.HomeContract.UiEffect
import com.oguzhanozgokce.bootmobilesecure.ui.home.HomeContract.UiState
import com.oguzhanozgokce.bootmobilesecure.ui.home.HomeContract.User
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Calendar

class HomeViewModel : ViewModel(), MVI<UiState, UiAction, UiEffect> by mvi(UiState()) {

    init {
        loadUserData()
        updateGreeting()
        loadQuickActions()
    }

    override fun onAction(uiAction: UiAction) {
        viewModelScope.launch {
            when (uiAction) {
                UiAction.LogoutClicked -> handleLogout()
                UiAction.ProfileClicked -> emitUiEffect(UiEffect.NavigateToProfile)
                UiAction.SettingsClicked -> emitUiEffect(UiEffect.NavigateToSettings)
                is UiAction.QuickActionClicked -> handleQuickAction(uiAction.actionId)
            }
        }
    }

    private fun loadUserData() {
        viewModelScope.launch {
            updateUiState { copy(isLoading = true) }

            try {
                // Simulate API call
                delay(1000)

                // Mock user data
                val mockUser = User(
                    id = "1",
                    username = "oguzhan33",
                    email = "oguzhan33@gmail.com",
                    firstName = "Oğuzhan",
                    lastName = "Özgökçe",
                    avatar = null,
                    joinDate = "January 2024",
                    lastLogin = "2 minutes ago"
                )

                updateUiState {
                    copy(
                        user = mockUser,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                updateUiState { copy(isLoading = false) }
                emitUiEffect(UiEffect.ShowError("Failed to load user data"))
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

        updateUiState {
            copy(greeting = greeting)
        }
    }

    private fun loadQuickActions() {
        val mockQuickActions = listOf(
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
                id = "help",
                title = "Help & Support",
                description = "Get help and contact support",
                icon = "❓",
                action = "help"
            )
        )

        updateUiState { copy(quickActions = mockQuickActions) }
    }

    private suspend fun handleLogout() {
        updateUiState { copy(isLoading = true) }

        try {
            // Simulate logout API call
            delay(1000)
            emitUiEffect(UiEffect.NavigateToLogin)
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
            "help" -> emitUiEffect(UiEffect.ShowSuccess("Help center opened"))
            else -> emitUiEffect(UiEffect.ShowError("Unknown action"))
        }
    }
}