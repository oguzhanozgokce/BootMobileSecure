package com.oguzhanozgokce.bootmobilesecure.ui.splash

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.oguzhanozgokce.bootmobilesecure.ui.components.BMBaseScreen
import com.oguzhanozgokce.bootmobilesecure.ui.splash.SplashContract.UiAction
import com.oguzhanozgokce.bootmobilesecure.ui.splash.SplashContract.UiEffect
import com.oguzhanozgokce.bootmobilesecure.ui.splash.SplashContract.UiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Composable
fun SplashScreen(
    uiState: UiState,
    uiEffect: Flow<UiEffect>,
    onAction: (UiAction) -> Unit,
    onNavigateToLogin: () -> Unit,
    onNavigateToHome: () -> Unit
) {
    val context = LocalContext.current
    BMBaseScreen(
        isLoading = uiState.isLoading,
        uiEffect = uiEffect,
        collectEffect = { effect ->
            when (effect) {
                UiEffect.NavigateToHome -> onNavigateToHome()
                UiEffect.NavigateToLogin -> onNavigateToLogin()
                is UiEffect.ShowError -> Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
            }
        },
        containerColor = Color(0xFFE8F5E8)
    ) {
        SplashContent(
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Composable
fun SplashContent(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.background(Color(0xFFE8F5E8)),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .background(
                        color = Color.White.copy(alpha = 0.8f),
                        shape = RoundedCornerShape(60.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "🍃",
                    fontSize = 48.sp,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Spring Boot App",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF6DB33F),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Secure Mobile Application",
                fontSize = 16.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview(
    @PreviewParameter(SplashScreenPreviewProvider::class) uiState: UiState,
) {
    SplashScreen(
        uiState = uiState,
        uiEffect = emptyFlow(),
        onAction = {},
        onNavigateToLogin = {},
        onNavigateToHome = {}
    )
}