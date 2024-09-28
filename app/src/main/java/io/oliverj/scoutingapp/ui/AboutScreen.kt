package io.oliverj.scoutingapp.ui

import android.graphics.drawable.Icon
import android.widget.ListView
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import io.oliverj.scoutingapp.R
import io.oliverj.scoutingapp.ui.theme.ScoutingAppTheme
import kotlinx.coroutines.launch

@Composable
fun AboutScreen(
    snackbarHostState: SnackbarHostState,
    onEasterEgg: () -> Unit,
    easterEgg: Boolean,
    modifier: Modifier = Modifier
) {

    var easter_count: Int = 0
    var debug_state by remember { mutableStateOf(easterEgg) }

    val info: List<Pair<String, String>> = listOf(
        Pair("App Name", stringResource(R.string.app_name)),
        Pair("App Version", stringResource(R.string.app_version)),
        Pair("Author", stringResource(R.string.author)),
        Pair("Build Number", stringResource(R.string.build_number)),
        Pair("Debug Mode", debug_state.toString().replaceFirstChar { it.uppercaseChar() })
    )

    val scope = rememberCoroutineScope()

    Column {
        info.forEach { item ->
            ListItem(
                headlineContent = { Text(text = item.first, style = MaterialTheme.typography.labelSmall, color = Color.Gray) },
                supportingContent = { Text(text = item.second, style = MaterialTheme.typography.bodyMedium, color = Color.Black) },
                modifier = Modifier.clickable {
                    if (item.first == "Build Number") {
                        if (easter_count == 6) {
                            scope.launch {
                                if (!debug_state) {
                                    snackbarHostState.showSnackbar("Disabled Debug Mode")
                                } else {
                                    snackbarHostState.showSnackbar("Enabled Debug Mode")
                                }
                            }
                            easter_count = 0
                            onEasterEgg()
                            debug_state = !debug_state
                        }
                        easter_count += 1
                    }
                }
            )
        }
    }
}

@Preview
@Composable
fun AboutScreenPreview() {
    ScoutingAppTheme {
        AboutScreen(SnackbarHostState(), {}, false)
    }
}