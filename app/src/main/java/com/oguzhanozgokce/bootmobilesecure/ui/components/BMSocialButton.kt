package com.oguzhanozgokce.bootmobilesecure.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BMSocialButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    enabled: Boolean = true,
    icon: @Composable () -> Unit = {},
    backgroundColor: Color = Color.White,
    borderColor: Color = Color.Gray.copy(alpha = 0.3f),
    textColor: Color = Color.Black,
    cornerRadius: Int = 12,
) {
    Box(
        modifier = modifier
            .height(48.dp)
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(cornerRadius.dp)
            )
            .border(
                width = 1.dp,
                color = if (enabled) borderColor else borderColor.copy(alpha = 0.5f),
                shape = RoundedCornerShape(cornerRadius.dp)
            )
            .clip(RoundedCornerShape(cornerRadius.dp))
            .clickable(enabled = enabled && !isLoading) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                color = textColor,
                strokeWidth = 2.dp
            )
        } else {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                icon()
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = text,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = textColor
                )
            }
        }
    }
}

@Composable
fun BMGoogleButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    enabled: Boolean = true,
) {
    BMSocialButton(
        text = "Google",
        onClick = onClick,
        modifier = modifier,
        isLoading = isLoading,
        enabled = enabled,
        icon = {
            Text(
                text = "G",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF4285F4)
            )
        }
    )
}

@Composable
fun BMFacebookButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    enabled: Boolean = true,
) {
    BMSocialButton(
        text = "Facebook",
        onClick = onClick,
        modifier = modifier,
        isLoading = isLoading,
        enabled = enabled,
        icon = {
            Text(
                text = "f",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1877F2)
            )
        }
    )
}

@Composable
fun BMTwitterButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    enabled: Boolean = true,
) {
    BMSocialButton(
        text = "Twitter",
        onClick = onClick,
        modifier = modifier,
        isLoading = isLoading,
        enabled = enabled,
        icon = {
            Text(
                text = "🐦",
                fontSize = 16.sp
            )
        }
    )
}

@Composable
fun BMGitHubButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    enabled: Boolean = true,
) {
    BMSocialButton(
        text = "GitHub",
        onClick = onClick,
        modifier = modifier,
        isLoading = isLoading,
        enabled = enabled,
        icon = {
            Text(
                text = "⚫",
                fontSize = 16.sp
            )
        }
    )
}