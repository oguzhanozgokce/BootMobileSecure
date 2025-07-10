package com.oguzhanozgokce.bootmobilesecure.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oguzhanozgokce.bootmobilesecure.data.model.LoginRequest
import com.oguzhanozgokce.bootmobilesecure.data.network.getUserMessage
import com.oguzhanozgokce.bootmobilesecure.delegation.MVI
import com.oguzhanozgokce.bootmobilesecure.delegation.mvi
import com.oguzhanozgokce.bootmobilesecure.domain.ValidationHelper
import com.oguzhanozgokce.bootmobilesecure.domain.repository.MainRepository
import com.oguzhanozgokce.bootmobilesecure.ui.login.LoginContract.UiAction
import com.oguzhanozgokce.bootmobilesecure.ui.login.LoginContract.UiEffect
import com.oguzhanozgokce.bootmobilesecure.ui.login.LoginContract.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel(),
    MVI<UiState, UiAction, UiEffect> by mvi(UiState()) {

    override fun onAction(uiAction: UiAction) {
        viewModelScope.launch {
            when (uiAction) {
                is UiAction.UsernameChanged -> updateUiState {
                    copy(username = uiAction.username, usernameError = null)
                }

                is UiAction.PasswordChanged -> updateUiState {
                    copy(password = uiAction.password, passwordError = null)
                }

                is UiAction.RememberMeChanged -> updateUiState {
                    copy(rememberMe = uiAction.rememberMe)
                }

                UiAction.LoginClicked -> handleLogin()
                UiAction.ForgotPasswordClicked -> emitUiEffect(UiEffect.NavigateToForgotPassword)
                UiAction.CreateAccountClicked -> emitUiEffect(UiEffect.NavigateToSignup)
                UiAction.GoogleLoginClicked -> emitUiEffect(UiEffect.ShowError("Google login coming soon"))
                UiAction.FacebookLoginClicked -> emitUiEffect(UiEffect.ShowError("Facebook login coming soon"))
            }
        }
    }

    private fun handleLogin() = viewModelScope.launch {
        val currentState = uiState.value

        val validationErrors = ValidationHelper.validateLoginWithUsername(
            username = currentState.username,
            password = currentState.password
        )

        if (validationErrors.hasErrors()) {
            updateUiState {
                copy(
                    usernameError = validationErrors.usernameError,
                    passwordError = validationErrors.passwordError
                )
            }
            return@launch
        }

        updateUiState { copy(isLoading = true) }

        val loginRequest = LoginRequest(
            username = currentState.username.trim(),
            password = currentState.password
        )

        mainRepository.login(loginRequest)
            .onSuccess {
                updateUiState { copy(isLoading = false) }
                emitUiEffect(UiEffect.NavigateToHome)
            }
            .onFailure { throwable ->
                updateUiState { copy(isLoading = false) }
                emitUiEffect(UiEffect.ShowError(throwable.getUserMessage()))
            }
    }
}