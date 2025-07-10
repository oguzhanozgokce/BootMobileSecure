package com.oguzhanozgokce.bootmobilesecure.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oguzhanozgokce.bootmobilesecure.delegation.MVI
import com.oguzhanozgokce.bootmobilesecure.delegation.mvi
import com.oguzhanozgokce.bootmobilesecure.ui.login.LoginContract.UiAction
import com.oguzhanozgokce.bootmobilesecure.ui.login.LoginContract.UiEffect
import com.oguzhanozgokce.bootmobilesecure.ui.login.LoginContract.UiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel(), MVI<UiState, UiAction, UiEffect> by mvi(UiState()) {

    override fun onAction(uiAction: UiAction) {
        viewModelScope.launch {
            when (uiAction) {
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

                is UiAction.RememberMeChanged -> {
                    updateUiState {
                        copy(rememberMe = uiAction.rememberMe)
                    }
                }

                UiAction.LoginClicked -> {
                    handleLogin()
                }

                UiAction.ForgotPasswordClicked -> {
                    emitUiEffect(UiEffect.NavigateToForgotPassword)
                }

                UiAction.GoogleLoginClicked -> {
                    handleGoogleLogin()
                }

                UiAction.FacebookLoginClicked -> {
                    handleFacebookLogin()
                }

                UiAction.CreateAccountClicked -> {
                    emitUiEffect(UiEffect.NavigateToSignup)
                }
            }
        }
    }

    private suspend fun handleLogin() {
        val currentState = uiState.value

        // Validate inputs
        val emailError = validateEmail(currentState.email)
        val passwordError = validatePassword(currentState.password)

        if (emailError != null || passwordError != null) {
            updateUiState {
                copy(
                    emailError = emailError,
                    passwordError = passwordError
                )
            }
            return
        }

        // Start loading
        updateUiState { copy(isLoading = true) }

        try {
            // Simulate API call
            delay(2000)

            // Check credentials (mock validation)
            if (currentState.email == "test@example.com" && currentState.password == "password123") {
                emitUiEffect(UiEffect.NavigateToHome)
            } else {
                emitUiEffect(UiEffect.ShowError("Invalid email or password"))
            }
        } catch (e: Exception) {
            emitUiEffect(UiEffect.ShowError("Login failed. Please try again."))
        } finally {
            updateUiState { copy(isLoading = false) }
        }
    }

    private suspend fun handleGoogleLogin() {
        updateUiState { copy(isLoading = true) }

        try {
            // Simulate Google login
            delay(1500)
            emitUiEffect(UiEffect.NavigateToHome)
        } catch (e: Exception) {
            emitUiEffect(UiEffect.ShowError("Google login failed"))
        } finally {
            updateUiState { copy(isLoading = false) }
        }
    }

    private suspend fun handleFacebookLogin() {
        updateUiState { copy(isLoading = true) }

        try {
            // Simulate Facebook login
            delay(1500)
            emitUiEffect(UiEffect.NavigateToHome)
        } catch (e: Exception) {
            emitUiEffect(UiEffect.ShowError("Facebook login failed"))
        } finally {
            updateUiState { copy(isLoading = false) }
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
            else -> null
        }
    }
}