package com.oguzhanozgokce.bootmobilesecure.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BMDividerWithText(
    text: String,
    modifier: Modifier = Modifier,
    textColor: Color = Color.Gray,
    dividerColor: Color = Color.Gray.copy(alpha = 0.3f),
    dividerThickness: Dp = 1.dp,
    textSize: Int = 14,
    fontWeight: FontWeight = FontWeight.Normal,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        HorizontalDivider(
            modifier = Modifier.weight(1f),
            thickness = dividerThickness,
            color = dividerColor
        )

        Text(
            text = text,
            fontSize = textSize.sp,
            fontWeight = fontWeight,
            color = textColor,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        HorizontalDivider(
            modifier = Modifier.weight(1f),
            thickness = dividerThickness,
            color = dividerColor
        )
    }
}

@Composable
fun BMOrDivider(
    modifier: Modifier = Modifier,
    text: String = "Or",
) {
    BMDividerWithText(
        text = text,
        modifier = modifier,
        textColor = Color.Gray,
        dividerColor = Color.Gray.copy(alpha = 0.3f),
        textSize = 14,
        fontWeight = FontWeight.Normal
    )
}

@Composable
fun BMLoginDivider(
    modifier: Modifier = Modifier,
) {
    BMDividerWithText(
        text = "Or login with",
        modifier = modifier,
        textColor = Color.Gray,
        dividerColor = Color.Gray.copy(alpha = 0.3f),
        textSize = 14,
        fontWeight = FontWeight.Normal
    )
}

@Composable
fun BMSignupDivider(
    modifier: Modifier = Modifier,
) {
    BMDividerWithText(
        text = "Or sign up with",
        modifier = modifier,
        textColor = Color.Gray,
        dividerColor = Color.Gray.copy(alpha = 0.3f),
        textSize = 14,
        fontWeight = FontWeight.Normal
    )
}

@Composable
fun BMRegisterDivider(
    modifier: Modifier = Modifier,
) {
    BMDividerWithText(
        text = "Or register with",
        modifier = modifier,
        textColor = Color.Gray,
        dividerColor = Color.Gray.copy(alpha = 0.3f),
        textSize = 14,
        fontWeight = FontWeight.Normal
    )
}

@Composable
fun BMSimpleDivider(
    modifier: Modifier = Modifier,
    color: Color = Color.Gray.copy(alpha = 0.3f),
    thickness: Dp = 1.dp,
) {
    HorizontalDivider(
        modifier = modifier.fillMaxWidth(),
        thickness = thickness,
        color = color
    )
}