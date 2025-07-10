package com.oguzhanozgokce.bootmobilesecure.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oguzhanozgokce.bootmobilesecure.delegation.MVI
import com.oguzhanozgokce.bootmobilesecure.delegation.mvi
import com.oguzhanozgokce.bootmobilesecure.ui.register.RegisterContract.UiAction
import com.oguzhanozgokce.bootmobilesecure.ui.register.RegisterContract.UiEffect
import com.oguzhanozgokce.bootmobilesecure.ui.register.RegisterContract.UiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel(), MVI<UiState, UiAction, UiEffect> by mvi(UiState()) {

    override fun onAction(uiAction: UiAction) {
        viewModelScope.launch {
            when (uiAction) {
                is UiAction.UsernameChanged -> {
                    updateUiState {
                        copy(
                            username = uiAction.username,
                            usernameError = null
                        )
                    }
                }

                is UiAction.EmailChanged -> {
                    updateUiState {
                        copy(
                            email = uiAction.email,
                            emailError = null
                        )
                    }
                }

                is UiAction.PasswordChanged -> {
                    updateUiState {
                        copy(
                            password = uiAction.password,
                            passwordError = null
                        )
                    }
                }

                is UiAction.FirstNameChanged -> {
                    updateUiState {
                        copy(
                            firstName = uiAction.firstName,
                            firstNameError = null
                        )
                    }
                }

                is UiAction.LastNameChanged -> {
                    updateUiState {
                        copy(
                            lastName = uiAction.lastName,
                            lastNameError = null
                        )
                    }
                }

                UiAction.RegisterClicked -> {
                    handleRegister()
                }

                UiAction.LoginClicked -> {
                    emitUiEffect(UiEffect.NavigateToLogin)
                }

                UiAction.BackClicked -> {
                    emitUiEffect(UiEffect.NavigateBack)
                }

                UiAction.GoogleRegisterClicked -> {
                    handleGoogleRegister()
                }

                UiAction.FacebookRegisterClicked -> {
                    handleFacebookRegister()
                }
            }
        }
    }

    // Update state example: updateUiState { UiState(isLoading = false) }
    // or // updateUiState { copy(isLoading = false) }

    // Update effect example: emitUiEffect(UiEffect.ShowError(it.message.orEmpty()))
    // Use within a coroutine scope, e.g., viewModelScope.launch { ... }

    private suspend fun handleRegister() {
        val currentState = uiState.value

        // Validate inputs
        val usernameError = validateUsername(currentState.username)
        val emailError = validateEmail(currentState.email)
        val passwordError = validatePassword(currentState.password)
        val firstNameError = validateFirstName(currentState.firstName)
        val lastNameError = validateLastName(currentState.lastName)

        if (usernameError != null || emailError != null || passwordError != null ||
            firstNameError != null || lastNameError != null
        ) {
            updateUiState {
                copy(
                    usernameError = usernameError,
                    emailError = emailError,
                    passwordError = passwordError,
                    firstNameError = firstNameError,
                    lastNameError = lastNameError
                )
            }
            return
        }

        // Start loading
        updateUiState { copy(isLoading = true) }

        try {
            // Simulate API call
            delay(2000)

            // Mock registration success
            emitUiEffect(UiEffect.NavigateToHome)
        } catch (e: Exception) {
            emitUiEffect(UiEffect.ShowError("Registration failed. Please try again."))
        } finally {
            updateUiState { copy(isLoading = false) }
        }
    }

    private suspend fun handleGoogleRegister() {
        updateUiState { copy(isLoading = true) }

        try {
            // Simulate Google registration
            delay(1500)
            emitUiEffect(UiEffect.NavigateToHome)
        } catch (e: Exception) {
            emitUiEffect(UiEffect.ShowError("Google registration failed"))
        } finally {
            updateUiState { copy(isLoading = false) }
        }
    }

    private suspend fun handleFacebookRegister() {
        updateUiState { copy(isLoading = true) }

        try {
            // Simulate Facebook registration
            delay(1500)
            emitUiEffect(UiEffect.NavigateToHome)
        } catch (e: Exception) {
            emitUiEffect(UiEffect.ShowError("Facebook registration failed"))
        } finally {
            updateUiState { copy(isLoading = false) }
        }
    }

    private fun validateUsername(username: String): String? {
        return when {
            username.isBlank() -> "Username is required"
            username.length < 3 -> "Username must be at least 3 characters"
            username.length > 20 -> "Username must be less than 20 characters"
            !username.matches(Regex("^[a-zA-Z0-9_]+$")) -> "Username can only contain letters, numbers and underscore"
            else -> null
        }
    }

    private fun validateEmail(email: String): String? {
        return when {
            email.isBlank() -> "Email is required"
            !email.contains("@") -> "Please enter a valid email address"
            !email.matches(Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) -> "Please enter a valid email address"
            else -> null
        }
    }

    private fun validatePassword(password: String): String? {
        return when {
            password.isBlank() -> "Password is required"
            password.length < 6 -> "Password must be at least 6 characters"
            password.length > 100 -> "Password must be less than 100 characters"
            else -> null
        }
    }

    private fun validateFirstName(firstName: String): String? {
        return when {
            firstName.isBlank() -> "First name is required"
            firstName.length < 2 -> "First name must be at least 2 characters"
            firstName.length > 50 -> "First name must be less than 50 characters"
            else -> null
        }
    }

    private fun validateLastName(lastName: String): String? {
        return when {
            lastName.isBlank() -> "Last name is required"
            lastName.length < 2 -> "Last name must be at least 2 characters"
            lastName.length > 50 -> "Last name must be less than 50 characters"
            else -> null
        }
    }
}