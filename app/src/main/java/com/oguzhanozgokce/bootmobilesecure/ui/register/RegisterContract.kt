package com.oguzhanozgokce.bootmobilesecure.ui.register

object RegisterContract {
    data class UiState(
        val isLoading: Boolean = false,
        val username: String = "",
        val email: String = "",
        val password: String = "",
        val firstName: String = "",
        val lastName: String = "",
        val usernameError: String? = null,
        val emailError: String? = null,
        val passwordError: String? = null,
        val firstNameError: String? = null,
        val lastNameError: String? = null,
    )

    sealed interface UiAction {
        data class UsernameChanged(val username: String) : UiAction
        data class EmailChanged(val email: String) : UiAction
        data class PasswordChanged(val password: String) : UiAction
        data class FirstNameChanged(val firstName: String) : UiAction
        data class LastNameChanged(val lastName: String) : UiAction
        object RegisterClicked : UiAction
        object LoginClicked : UiAction
        object BackClicked : UiAction
        object GoogleRegisterClicked : UiAction
        object FacebookRegisterClicked : UiAction
    }

    sealed interface UiEffect {
        object NavigateToLogin : UiEffect
        object NavigateBack : UiEffect
        object NavigateToHome : UiEffect
        data class ShowError(val message: String) : UiEffect
    }
}