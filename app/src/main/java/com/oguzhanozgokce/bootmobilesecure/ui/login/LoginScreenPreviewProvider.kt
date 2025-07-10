package com.oguzhanozgokce.bootmobilesecure.ui.login

import androidx.compose.ui.tooling.preview.PreviewParameterProvider

class LoginScreenPreviewProvider : PreviewParameterProvider<LoginContract.UiState> {
    override val values: Sequence<LoginContract.UiState>
        get() = sequenceOf(
            LoginContract.UiState(
                isLoading = false,
                email = "",
                password = "",
                rememberMe = false,
                emailError = null,
                passwordError = null,
            ),
            LoginContract.UiState(
                isLoading = false,
                email = "user@example.com",
                password = "password123",
                rememberMe = true,
                emailError = null,
                passwordError = null,
            ),
            LoginContract.UiState(
                isLoading = false,
                email = "invalid-email",
                password = "123",
                rememberMe = false,
                emailError = "Please enter a valid email address",
                passwordError = "Password must be at least 8 characters",
            ),
            LoginContract.UiState(
                isLoading = true,
                email = "user@example.com",
                password = "password123",
                rememberMe = true,
                emailError = null,
                passwordError = null,
            ),
        )
}