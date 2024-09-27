package io.oliverj.scoutingapp.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import io.oliverj.scoutingapp.R
import io.oliverj.scoutingapp.ui.theme.ScoutingAppTheme

@Composable
fun MainScreen(
    onAboutClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row {
        Text(text = "Main Screen")
        IconButton(onClick = onAboutClicked) {
            Icon(
                imageVector = Icons.Outlined.Info,
                contentDescription = stringResource(R.string.about)
            )
        }
    }
}

@Preview
@Composable
fun MainScreenPreview() {
    ScoutingAppTheme {
        MainScreen(
            onAboutClicked = {}
        )
    }
}