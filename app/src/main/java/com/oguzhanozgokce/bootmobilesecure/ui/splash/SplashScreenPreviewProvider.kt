package com.oguzhanozgokce.bootmobilesecure.ui.splash

import androidx.compose.ui.tooling.preview.PreviewParameterProvider

class SplashScreenPreviewProvider : PreviewParameterProvider<SplashContract.UiState> {
    override val values: Sequence<SplashContract.UiState>
        get() = sequenceOf(
            SplashContract.UiState(
                isLoading = true,
            ),
            SplashContract.UiState(
                isLoading = false,
            ),
        )
}