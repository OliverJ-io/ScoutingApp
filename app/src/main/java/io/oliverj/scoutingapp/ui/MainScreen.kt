package io.oliverj.scoutingapp.ui

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.util.Log
import android.webkit.MimeTypeMap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import io.oliverj.scoutingapp.R
import io.oliverj.scoutingapp.ScoutingAppScreen
import io.oliverj.scoutingapp.ui.components.AllianceButtonGroup
import io.oliverj.scoutingapp.ui.components.FileChooser
import io.oliverj.scoutingapp.ui.components.FileDialog
import io.oliverj.scoutingapp.ui.components.NextButton
import io.oliverj.scoutingapp.ui.components.RobotButtonGroup
import io.oliverj.scoutingapp.ui.components.SimpleOutlinedTextField
import io.oliverj.scoutingapp.ui.theme.ScoutingAppTheme
import java.io.File

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    context: Context,
    onAboutClicked: () -> Unit,
    onFileLoad: (String?) -> Unit = {},
    onFinish: () -> Unit,
    viewModel: ScoutingViewModel,
    navController: NavHostController
) {

    val filePicker = rememberLauncherForActivityResult(
        contract = FileDialog(),
        onResult = { uris ->
            uris.forEach { uri ->
                val data = getUri(uri, context)
                Log.d("MainScreen", "Uri Data: $data")
                onFileLoad(getUri(uri, context))
                onFinish()
            }
        }
    )

    Column {
        Row {
            Text(text = "Main Screen")
            IconButton(onClick = onAboutClicked) {
                Icon(
                    imageVector = Icons.Outlined.Info,
                    contentDescription = stringResource(R.string.about)
                )
            }
        }

        SimpleOutlinedTextField(
            initialContent = viewModel.uiState.collectAsState().value.compId,
            label = "Competition",
            onChange = { viewModel.setCompId(it) }
        )

        SimpleOutlinedTextField(
            initialContent = viewModel.uiState.collectAsState().value.matchId,
            label = "Match",
            onChange = { viewModel.setMatchId(it) }
        )

        SimpleOutlinedTextField(
            initialContent = viewModel.uiState.collectAsState().value.scouter,
            label = "Scouter",
            onChange = { viewModel.setScouter(it) }
        )

        AllianceButtonGroup(
            label = "Alliance",
            options = listOf("Blue", "Red"),
            viewModel = viewModel
        )

        RobotButtonGroup(
            label = "Robot",
            options = listOf("Left", "Right"),
            viewModel = viewModel
        )

        NextButton(
            nextPage = ScoutingAppScreen.Auton.name,
            navController = navController,
            modifier = Modifier.padding(5.dp)
        )

        Box(
        modifier = modifier
            .fillMaxSize(),
        contentAlignment = Alignment.BottomEnd
        ) {
            FileChooser {
                filePicker.launch("application/json")
            }
        }
    }
}

@Preview
@Composable
fun MainScreenPreview() {
    ScoutingAppTheme {
        MainScreen(
            onAboutClicked = {},
            context = LocalContext.current,
            viewModel = ScoutingViewModel(),
            onFinish = {},
            navController = rememberNavController()
        )
    }
}

private fun getUri(uri: Uri, context: Context): String? {
    var out: String? = null
    if (ContentResolver.SCHEME_CONTENT == uri.scheme) {
        val cr: ContentResolver = context.contentResolver
        val input = cr.openInputStream(uri)
        out = input?.bufferedReader().use { it?.readText() }
    }
    return out
}