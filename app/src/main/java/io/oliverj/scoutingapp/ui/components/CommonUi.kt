package io.oliverj.scoutingapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource

@Composable
fun DataCard(
    title: String,
    supportingText: String,
    body: @Composable () -> Unit,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier.clickable {
            onClick()
        }
    ) {
        Text(title)
        Text(supportingText, style = MaterialTheme.typography.labelSmall, color = Color.LightGray)
    }
}