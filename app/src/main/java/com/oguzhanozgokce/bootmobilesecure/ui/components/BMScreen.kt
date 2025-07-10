package com.oguzhanozgokce.bootmobilesecure.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.oguzhanozgokce.bootmobilesecure.common.collectWithLifecycle
import kotlinx.coroutines.flow.Flow

@Composable
fun <T> BMBaseScreen(
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    uiEffect: Flow<T>? = null,
    collectEffect: (suspend (T) -> Unit)? = null,
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    snackBarHost: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    floatingActionButtonPosition: FabPosition = FabPosition.End,
    containerColor: Color = Color.White,
    content: @Composable (PaddingValues) -> Unit,
) {
    uiEffect?.collectWithLifecycle {
        collectEffect?.invoke(it)
    }

    BMScaffold(
        modifier = Modifier
            .fillMaxSize()
            .then(modifier),
        topBar = topBar,
        bottomBar = bottomBar,
        snackBarHost = snackBarHost,
        floatingActionButton = floatingActionButton,
        floatingActionButtonPosition = floatingActionButtonPosition,
        containerColor = containerColor,
    ) {
        content(it)
        if (isLoading) LoadingBar()
    }
}

@Composable
fun BMScaffold(
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    snackBarHost: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    floatingActionButtonPosition: FabPosition = FabPosition.End,
    containerColor: Color = Color.White,
    content: @Composable (PaddingValues) -> Unit,
) {

    Scaffold(
        modifier = modifier,
        topBar = topBar,
        bottomBar = bottomBar,
        snackbarHost = snackBarHost,
        floatingActionButton = floatingActionButton,
        floatingActionButtonPosition = floatingActionButtonPosition,
        containerColor = containerColor,
        content = content,
    )
}