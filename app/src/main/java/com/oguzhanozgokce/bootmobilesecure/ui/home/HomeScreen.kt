package com.oguzhanozgokce.bootmobilesecure.ui.home

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.oguzhanozgokce.bootmobilesecure.common.collectWithLifecycle
import com.oguzhanozgokce.bootmobilesecure.domain.model.User
import com.oguzhanozgokce.bootmobilesecure.ui.components.BMBaseScreen
import com.oguzhanozgokce.bootmobilesecure.ui.home.HomeContract.QuickAction
import com.oguzhanozgokce.bootmobilesecure.ui.home.HomeContract.UiAction
import com.oguzhanozgokce.bootmobilesecure.ui.home.HomeContract.UiEffect
import com.oguzhanozgokce.bootmobilesecure.ui.home.HomeContract.UiState
import kotlinx.coroutines.flow.Flow

@Composable
fun HomeScreen(
    uiState: UiState,
    uiEffect: Flow<UiEffect>,
    onAction: (UiAction) -> Unit,
    onNavigateToLogin: () -> Unit,
    onNavigateToProfile: () -> Unit = {},
    onNavigateToSettings: () -> Unit = {},
) {
    val context = LocalContext.current

    BMBaseScreen(
        isLoading = uiState.isLoading,
        uiEffect = uiEffect,
        collectEffect = { effect ->
            when (effect) {
                is UiEffect.NavigateToLogin -> onNavigateToLogin()
                is UiEffect.NavigateToProfile -> onAction(UiAction.ProfileClicked)
                is UiEffect.NavigateToSettings -> onAction(UiAction.SettingsClicked)
                is UiEffect.ShowError -> Toast.makeText(
                    context,
                    effect.message,
                    Toast.LENGTH_SHORT
                ).show()
                is UiEffect.ShowSuccess -> {}
            }
        },
        containerColor = Color(0xFFF5F5F5)
    ) {
        HomeContent(
            modifier = Modifier
                .fillMaxSize(),
            uiState = uiState,
            onAction = onAction,
        )
    }
}

@Composable
fun HomeContent(
    modifier: Modifier = Modifier,
    uiState: UiState,
    onAction: (UiAction) -> Unit,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))

            // Header with greeting and logout
            HeaderSection(
                greeting = uiState.greeting,
                onAction = onAction
            )
        }

        item {
            // Welcome Card
            WelcomeCard(
                user = uiState.user,
                onAction = onAction
            )
        }

        item {
            // User Info Card
            UserInfoCard(
                user = uiState.user,
                onAction = onAction
            )
        }

        item {
            // Quick Actions
            QuickActionsSection(
                quickActions = uiState.quickActions,
                onAction = onAction
            )
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun HeaderSection(
    greeting: String,
    onAction: (UiAction) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = greeting,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        IconButton(
            onClick = { onAction(UiAction.LogoutClicked) }
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                contentDescription = "Logout",
                tint = Color.Gray
            )
        }
    }
}

@Composable
fun WelcomeCard(
    user: User?,
    onAction: (UiAction) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onAction(UiAction.ProfileClicked) },
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF6DB33F)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Avatar",
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Welcome Back!",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                if (user != null) {
                    Text(
                        text = "${user.firstName} ${user.lastName}",
                        fontSize = 16.sp,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }
            }

            Text(
                text = "🍃",
                fontSize = 32.sp
            )
        }
    }
}

@Composable
fun UserInfoCard(
    user: User?,
    onAction: (UiAction) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onAction(UiAction.ProfileClicked) },
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = "Account Information",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (user != null) {
                // Username
                UserInfoRow(
                    label = "Username",
                    value = "@${user.username}",
                    icon = "👤"
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Email
                UserInfoRow(
                    label = "Email",
                    value = user.email,
                    icon = "📧"
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Full Name
                UserInfoRow(
                    label = "Full Name",
                    value = "${user.firstName} ${user.lastName}",
                    icon = "👨‍💼"
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Member Since
                UserInfoRow(
                    label = "Member Since",
                    value = "10/07/2025",
                    icon = "📅"
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Last Login
                UserInfoRow(
                    label = "Last Login",
                    value = "10/07/2025 14:30",
                    icon = "🕐"
                )
            } else {
                Text(
                    text = "Loading user information...",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun UserInfoRow(
    label: String,
    value: String,
    icon: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = icon,
            fontSize = 18.sp,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = label,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Gray
            )
            Text(
                text = value,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Black
            )
        }
    }
}

@Composable
fun QuickActionsSection(
    quickActions: List<QuickAction>,
    onAction: (UiAction) -> Unit
) {
    Column {
        Text(
            text = "Quick Actions",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(quickActions) { action ->
                QuickActionCard(
                    action = action,
                    onClick = { onAction(UiAction.QuickActionClicked(action.id)) }
                )
            }
        }
    }
}

@Composable
fun QuickActionCard(
    action: QuickAction,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .size(120.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = action.icon,
                fontSize = 32.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = action.title,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview(
    @PreviewParameter(HomeScreenPreviewProvider::class) uiState: UiState,
) {
    HomeContent(
        modifier = Modifier.fillMaxSize(),
        uiState = uiState,
        onAction = {},
    )
}