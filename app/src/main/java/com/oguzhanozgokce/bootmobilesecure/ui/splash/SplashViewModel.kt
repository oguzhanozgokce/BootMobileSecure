package com.oguzhanozgokce.bootmobilesecure.ui.splash

import androidx.lifecycle.ViewModel
import com.oguzhanozgokce.bootmobilesecure.delegation.MVI
import com.oguzhanozgokce.bootmobilesecure.delegation.mvi
import com.oguzhanozgokce.bootmobilesecure.ui.splash.SplashContract.UiAction
import com.oguzhanozgokce.bootmobilesecure.ui.splash.SplashContract.UiEffect
import com.oguzhanozgokce.bootmobilesecure.ui.splash.SplashContract.UiState

class SplashViewModel : ViewModel(), MVI<UiState, UiAction, UiEffect> by mvi(UiState()) {

    override fun onAction(uiAction: UiAction) {

    }

    // Update state example: updateUiState { UiState(isLoading = false) }
    // or // updateUiState { copy(isLoading = false) }

    // Update effect example: emitUiEffect(UiEffect.ShowError(it.message.orEmpty()))
    // Use within a coroutine scope, e.g., viewModelScope.launch { ... }
}