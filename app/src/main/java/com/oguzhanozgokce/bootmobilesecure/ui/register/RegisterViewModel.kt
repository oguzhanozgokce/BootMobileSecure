package com.oguzhanozgokce.bootmobilesecure.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oguzhanozgokce.bootmobilesecure.data.model.RegisterRequest
import com.oguzhanozgokce.bootmobilesecure.data.network.getUserMessage
import com.oguzhanozgokce.bootmobilesecure.delegation.MVI
import com.oguzhanozgokce.bootmobilesecure.delegation.mvi
import com.oguzhanozgokce.bootmobilesecure.domain.ValidationHelper
import com.oguzhanozgokce.bootmobilesecure.domain.repository.MainRepository
import com.oguzhanozgokce.bootmobilesecure.ui.register.RegisterContract.UiAction
import com.oguzhanozgokce.bootmobilesecure.ui.register.RegisterContract.UiEffect
import com.oguzhanozgokce.bootmobilesecure.ui.register.RegisterContract.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel(), MVI<UiState, UiAction, UiEffect> by mvi(UiState()) {

    override fun onAction(uiAction: UiAction) {
        viewModelScope.launch {
            when (uiAction) {
                is UiAction.UsernameChanged -> updateUiState {
                    copy(username = uiAction.username, usernameError = null)
                }

                is UiAction.EmailChanged -> updateUiState {
                    copy(email = uiAction.email, emailError = null)
                }

                is UiAction.PasswordChanged -> updateUiState {
                    copy(password = uiAction.password, passwordError = null)
                }

                is UiAction.FirstNameChanged -> updateUiState {
                    copy(firstName = uiAction.firstName, firstNameError = null)
                }

                is UiAction.LastNameChanged -> updateUiState {
                    copy(lastName = uiAction.lastName, lastNameError = null)
                }

                UiAction.RegisterClicked -> handleRegister()
                UiAction.LoginClicked -> emitUiEffect(UiEffect.NavigateToLogin)
                UiAction.BackClicked -> emitUiEffect(UiEffect.NavigateBack)
                UiAction.GoogleRegisterClicked -> emitUiEffect(UiEffect.ShowError("Google registration coming soon"))
                UiAction.FacebookRegisterClicked -> emitUiEffect(UiEffect.ShowError("Facebook registration coming soon"))
            }
        }
    }

    private fun handleRegister() = viewModelScope.launch {
        val currentState = uiState.value

        val validationErrors = ValidationHelper.validateRegister(
            username = currentState.username,
            email = currentState.email,
            password = currentState.password,
            firstName = currentState.firstName,
            lastName = currentState.lastName
        )

        if (validationErrors.hasErrors()) {
            updateUiState {
                copy(
                    usernameError = validationErrors.usernameError,
                    emailError = validationErrors.emailError,
                    passwordError = validationErrors.passwordError,
                    firstNameError = validationErrors.firstNameError,
                    lastNameError = validationErrors.lastNameError
                )
            }
            return@launch
        }

        updateUiState { copy(isLoading = true) }

        val registerRequest = RegisterRequest(
            username = currentState.username.trim(),
            email = currentState.email.trim().lowercase(),
            password = currentState.password,
            firstName = currentState.firstName.trim(),
            lastName = currentState.lastName.trim()
        )

        mainRepository.register(registerRequest)
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