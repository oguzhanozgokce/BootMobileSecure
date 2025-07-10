package com.oguzhanozgokce.bootmobilesecure.ui.password

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.sp
import com.oguzhanozgokce.bootmobilesecure.common.collectWithLifecycle
import com.oguzhanozgokce.bootmobilesecure.ui.password.PasswordContract.UiAction
import com.oguzhanozgokce.bootmobilesecure.ui.password.PasswordContract.UiEffect
import com.oguzhanozgokce.bootmobilesecure.ui.password.PasswordContract.UiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Composable
fun PasswordScreen(
    uiState: UiState,
    uiEffect: Flow<UiEffect>,
    onAction: (UiAction) -> Unit,
) {
    uiEffect.collectWithLifecycle {}

    PasswordContent(
        modifier = Modifier.fillMaxSize(),
        uiState = uiState,
        onAction = onAction,
    )
}

@Composable
fun PasswordContent(
    modifier: Modifier = Modifier,
    uiState: UiState,
    onAction: (UiAction) -> Unit,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "Password Content",
            fontSize = 24.sp,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PasswordScreenPreview(
    @PreviewParameter(PasswordScreenPreviewProvider::class) uiState: UiState,
) {
    PasswordScreen(
        uiState = uiState,
        uiEffect = emptyFlow(),
        onAction = {},
    )
}