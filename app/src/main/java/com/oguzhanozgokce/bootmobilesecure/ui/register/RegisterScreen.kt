package com.oguzhanozgokce.bootmobilesecure.ui.register

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
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.oguzhanozgokce.bootmobilesecure.common.collectWithLifecycle
import com.oguzhanozgokce.bootmobilesecure.ui.components.BMBaseScreen
import com.oguzhanozgokce.bootmobilesecure.ui.components.BMFacebookButton
import com.oguzhanozgokce.bootmobilesecure.ui.components.BMFormCard
import com.oguzhanozgokce.bootmobilesecure.ui.components.BMGoogleButton
import com.oguzhanozgokce.bootmobilesecure.ui.components.BMPrimaryButton
import com.oguzhanozgokce.bootmobilesecure.ui.components.BMRegisterDivider
import com.oguzhanozgokce.bootmobilesecure.ui.components.BMTextField
import com.oguzhanozgokce.bootmobilesecure.ui.register.RegisterContract.UiAction
import com.oguzhanozgokce.bootmobilesecure.ui.register.RegisterContract.UiEffect
import com.oguzhanozgokce.bootmobilesecure.ui.register.RegisterContract.UiState
import kotlinx.coroutines.flow.Flow

@Composable
fun RegisterScreen(
    uiState: UiState,
    uiEffect: Flow<UiEffect>,
    onAction: (UiAction) -> Unit,
) {
    uiEffect.collectWithLifecycle {}

    BMBaseScreen(
        isLoading = uiState.isLoading,
        uiEffect = uiEffect,
        containerColor = Color(0xFFE8F5E8)
    ) { paddingValues ->
        RegisterContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            uiState = uiState,
            onAction = onAction,
        )
    }
}

@Composable
fun RegisterContent(
    modifier: Modifier = Modifier,
    uiState: UiState,
    onAction: (UiAction) -> Unit,
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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { onAction(UiAction.BackClicked) },
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.Black,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
            }


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

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Create Your Account",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Join Spring Boot App",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF6DB33F),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            BMFormCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    BMTextField(
                        value = uiState.firstName,
                        onValueChange = { onAction(UiAction.FirstNameChanged(it)) },
                        placeholder = "First name",
                        isError = uiState.firstNameError != null,
                        errorMessage = uiState.firstNameError,
                        modifier = Modifier.weight(1f)
                    )

                    BMTextField(
                        value = uiState.lastName,
                        onValueChange = { onAction(UiAction.LastNameChanged(it)) },
                        placeholder = "Last name",
                        isError = uiState.lastNameError != null,
                        errorMessage = uiState.lastNameError,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                BMTextField(
                    value = uiState.username,
                    onValueChange = { onAction(UiAction.UsernameChanged(it)) },
                    placeholder = "Enter your username",
                    leadingIcon = Icons.Default.Person,
                    isError = uiState.usernameError != null,
                    errorMessage = uiState.usernameError,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                BMTextField(
                    value = uiState.email,
                    onValueChange = { onAction(UiAction.EmailChanged(it)) },
                    placeholder = "Enter your email",
                    leadingIcon = Icons.Default.Email,
                    keyboardType = KeyboardType.Email,
                    isError = uiState.emailError != null,
                    errorMessage = uiState.emailError,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                BMTextField(
                    value = uiState.password,
                    onValueChange = { onAction(UiAction.PasswordChanged(it)) },
                    placeholder = "Enter your password",
                    leadingIcon = Icons.Default.Lock,
                    isPassword = true,
                    isError = uiState.passwordError != null,
                    errorMessage = uiState.passwordError,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                BMPrimaryButton(
                    text = "Create Account",
                    onClick = { onAction(UiAction.RegisterClicked) },
                    isLoading = uiState.isLoading,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                BMRegisterDivider(
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Social Register Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    BMGoogleButton(
                        onClick = { onAction(UiAction.GoogleRegisterClicked) },
                        modifier = Modifier.weight(1f),
                        isLoading = uiState.isLoading
                    )

                    BMFacebookButton(
                        onClick = { onAction(UiAction.FacebookRegisterClicked) },
                        modifier = Modifier.weight(1f),
                        isLoading = uiState.isLoading
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Login Link
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Already have an account? ",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Text(
                    text = "Login",
                    fontSize = 14.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.clickable {
                        onAction(UiAction.LoginClicked)
                    }
                )
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Preview
@Composable
fun RegisterScreenPreview(
    @PreviewParameter(RegisterScreenPreviewProvider::class) uiState: UiState,
) {
    RegisterContent(
        modifier = Modifier.fillMaxSize(),
        uiState = uiState,
        onAction = {},
    )
}