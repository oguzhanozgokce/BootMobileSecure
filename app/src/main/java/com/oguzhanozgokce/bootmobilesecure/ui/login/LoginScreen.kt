package com.oguzhanozgokce.bootmobilesecure.ui.login

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
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
import com.oguzhanozgokce.bootmobilesecure.ui.components.BMFacebookButton
import com.oguzhanozgokce.bootmobilesecure.ui.components.BMFormCard
import com.oguzhanozgokce.bootmobilesecure.ui.components.BMGoogleButton
import com.oguzhanozgokce.bootmobilesecure.ui.components.BMLoginDivider
import com.oguzhanozgokce.bootmobilesecure.ui.components.BMPasswordField
import com.oguzhanozgokce.bootmobilesecure.ui.components.BMPrimaryButton
import com.oguzhanozgokce.bootmobilesecure.ui.components.BMTextField
import com.oguzhanozgokce.bootmobilesecure.ui.login.LoginContract.UiAction
import com.oguzhanozgokce.bootmobilesecure.ui.login.LoginContract.UiEffect
import com.oguzhanozgokce.bootmobilesecure.ui.login.LoginContract.UiState
import kotlinx.coroutines.flow.Flow

@Composable
fun LoginScreen(
    uiState: UiState,
    uiEffect: Flow<UiEffect>,
    onAction: (UiAction) -> Unit,
    onNavigateToHome: () -> Unit = {},
    onNavigateToForgotPassword: () -> Unit = {},
    onNavigateToRegister: () -> Unit = {},
) {
    val context = LocalContext.current


    BMBaseScreen(
        isLoading = uiState.isLoading,
        uiEffect = uiEffect,
        collectEffect = { effect ->
            when (effect) {
                UiEffect.NavigateToHome -> onNavigateToHome()
                is UiEffect.ShowError -> Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                UiEffect.NavigateToForgotPassword -> onNavigateToForgotPassword()
                UiEffect.NavigateToSignup -> onNavigateToRegister()
            }
        },
        containerColor = Color(0xFFE8F5E8)
    ) { paddingValues ->
        LoginContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            uiState = uiState,
            onAction = onAction,
        )
    }
}

@Composable
fun LoginContent(
    modifier: Modifier = Modifier,
    uiState: UiState,
    onAction: (UiAction) -> Unit
) {
    Box(
        modifier = modifier
            .background(Color(0xFFE8F5E8))
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

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

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Login to Access Your",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Spring Boot App",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF6DB33F),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(40.dp))

            BMFormCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                BMTextField(
                    value = uiState.username,
                    onValueChange = { onAction(UiAction.UsernameChanged(it)) },
                    placeholder = "Enter your username",
                    leadingIcon = Icons.Filled.Person,
                    isError = uiState.usernameError != null,
                    errorMessage = uiState.usernameError,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                BMPasswordField(
                    value = uiState.password,
                    onValueChange = { onAction(UiAction.PasswordChanged(it)) },
                    isError = uiState.passwordError != null,
                    errorMessage = uiState.passwordError,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = uiState.rememberMe,
                            onCheckedChange = { onAction(UiAction.RememberMeChanged(it)) },
                            colors = CheckboxDefaults.colors(
                                checkedColor = Color(0xFF6DB33F)
                            )
                        )
                        Text(
                            text = "Remember me",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }

                    Text(
                        text = "Forgot password?",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        modifier = Modifier.clickable {
                            onAction(UiAction.ForgotPasswordClicked)
                        }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                BMPrimaryButton(
                    text = "Login",
                    onClick = { onAction(UiAction.LoginClicked) },
                    isLoading = uiState.isLoading,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                BMLoginDivider(
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    BMGoogleButton(
                        onClick = { onAction(UiAction.GoogleLoginClicked) },
                        modifier = Modifier.weight(1f),
                        isLoading = uiState.isLoading
                    )

                    BMFacebookButton(
                        onClick = { onAction(UiAction.FacebookLoginClicked) },
                        modifier = Modifier.weight(1f),
                        isLoading = uiState.isLoading
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Don't have an account? ",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Text(
                    text = "Create an account",
                    fontSize = 14.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.clickable {
                        onAction(UiAction.CreateAccountClicked)
                    }
                )
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Preview
@Composable
fun LoginScreenPreview(
    @PreviewParameter(LoginScreenPreviewProvider::class) uiState: UiState,
) {
    LoginContent(
        modifier = Modifier.fillMaxSize(),
        uiState = uiState,
        onAction = {},
    )
}