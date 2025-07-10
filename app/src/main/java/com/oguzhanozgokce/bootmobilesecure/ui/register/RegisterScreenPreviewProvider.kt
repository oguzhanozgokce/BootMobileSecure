package com.oguzhanozgokce.bootmobilesecure.ui.register

import androidx.compose.ui.tooling.preview.PreviewParameterProvider

class RegisterScreenPreviewProvider : PreviewParameterProvider<RegisterContract.UiState> {
    override val values: Sequence<RegisterContract.UiState>
        get() = sequenceOf(
            RegisterContract.UiState(
                isLoading = false,
                username = "",
                email = "",
                password = "",
                firstName = "",
                lastName = "",
                usernameError = null,
                emailError = null,
                passwordError = null,
                firstNameError = null,
                lastNameError = null,
            ),
            RegisterContract.UiState(
                isLoading = false,
                username = "oguzhan33",
                email = "oguzhan33@gmail.com",
                password = "password123",
                firstName = "Oğuzhan",
                lastName = "Özgökçe",
                usernameError = null,
                emailError = null,
                passwordError = null,
                firstNameError = null,
                lastNameError = null,
            ),
            RegisterContract.UiState(
                isLoading = false,
                username = "ab",
                email = "invalid-email",
                password = "123",
                firstName = "A",
                lastName = "B",
                usernameError = "Username must be at least 3 characters",
                emailError = "Please enter a valid email address",
                passwordError = "Password must be at least 6 characters",
                firstNameError = "First name must be at least 2 characters",
                lastNameError = "Last name must be at least 2 characters",
            ),
            RegisterContract.UiState(
                isLoading = true,
                username = "oguzhan33",
                email = "oguzhan33@gmail.com",
                password = "password123",
                firstName = "Oğuzhan",
                lastName = "Özgökçe",
                usernameError = null,
                emailError = null,
                passwordError = null,
                firstNameError = null,
                lastNameError = null,
            ),
        )
}