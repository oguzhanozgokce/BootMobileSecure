package com.oguzhanozgokce.bootmobilesecure.ui.home

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.oguzhanozgokce.bootmobilesecure.domain.model.User
import com.oguzhanozgokce.bootmobilesecure.ui.home.HomeContract.QuickAction

class HomeScreenPreviewProvider : PreviewParameterProvider<HomeContract.UiState> {
    override val values: Sequence<HomeContract.UiState>
        get() = sequenceOf(
            HomeContract.UiState(
                isLoading = false,
                user = User(
                    id = 1L,
                    username = "oguzhan33",
                    email = "oguzhan33@gmail.com",
                    firstName = "Oğuzhan",
                    lastName = "Özgökçe",
                    role = "User",
                    fullName = "Oğuzhan Özgökçe"
                ),
                greeting = "Good Morning",
                quickActions = listOf(
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
            ),
            HomeContract.UiState(
                isLoading = true,
                user = null,
                greeting = "",
                quickActions = emptyList()
            )
        )
}