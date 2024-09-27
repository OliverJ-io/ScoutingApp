package io.oliverj.scoutingapp

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Environment
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.core.app.ActivityCompat
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import io.oliverj.scoutingapp.ui.AboutScreen
import io.oliverj.scoutingapp.ui.AutonScreen
import io.oliverj.scoutingapp.ui.MainScreen
import io.oliverj.scoutingapp.ui.SaveScreen
import io.oliverj.scoutingapp.ui.ScoutingViewModel
import io.oliverj.scoutingapp.ui.TeleOpScreen
import java.io.File
import java.io.FileOutputStream
import kotlin.io.path.Path

enum class ScoutingAppScreen(@StringRes val title: Int) {
    Main(title = R.string.app_name),
    Auton(title = R.string.auton),
    TeleOp(title = R.string.teleop),
    About(title = R.string.about),
    Save(title = R.string.save)
}

var easter_egg: Boolean = false

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScoutingAppAppBar(
    currentScreen: ScoutingAppScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {Text(stringResource(currentScreen.title))},
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        }
    )
}

@Composable
fun ScoutingAppApp(
    viewModel: ScoutingViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = ScoutingAppScreen.valueOf(
        backStackEntry?.destination?.route ?: ScoutingAppScreen.Main.name
    )

    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(
        topBar = {
            ScoutingAppAppBar(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() })
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = currentScreen == ScoutingAppScreen.Main,
                    onClick = { navController.navigate(ScoutingAppScreen.Main.name) },
                    icon = { Icon(imageVector = Icons.Outlined.Home, contentDescription = stringResource(
                        R.string.app_name
                    )) }
                )
                NavigationBarItem(
                    selected = currentScreen == ScoutingAppScreen.Auton,
                    onClick = { navController.navigate(ScoutingAppScreen.Auton.name) },
                    icon = { Icon(imageVector =  ImageVector.vectorResource(R.drawable.auton), contentDescription = stringResource(
                        R.string.auton
                    )) }
                )
                NavigationBarItem(
                    selected = currentScreen == ScoutingAppScreen.TeleOp,
                    onClick = { navController.navigate(ScoutingAppScreen.TeleOp.name) },
                    icon = { Icon(imageVector = ImageVector.vectorResource(R.drawable.teleop), contentDescription = stringResource(
                        R.string.teleop
                    )) }
                )
                NavigationBarItem(
                    selected = currentScreen == ScoutingAppScreen.Save,
                    onClick = { navController.navigate(ScoutingAppScreen.Save.name) },
                    icon = { Icon(imageVector = Icons.Filled.Share, contentDescription = stringResource(
                        id = R.string.save
                    )) }
                )
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { innerPadding ->
        val uiState by viewModel.uiState.collectAsState()

        NavHost(
            navController = navController,
            startDestination = ScoutingAppScreen.Main.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = ScoutingAppScreen.Main.name) {
                MainScreen(
                    onAboutClicked = { navController.navigate(ScoutingAppScreen.About.name) }
                )
            }

            composable(route = ScoutingAppScreen.Auton.name) {
                AutonScreen(
                    onNetPointClicked = { viewModel.incrementAutonNetPoints() },
                    onBasketPointClicked = { viewModel.incrementAutonBasketPoints() },
                    onUndo = { viewModel.undoAction() },
                    stackView = { Text(viewModel.undoStack.toString()) },
                    basketPoints = uiState.autonBasketPoints,
                    netPoints = uiState.autonNetPoints,
                    debug_mode = easter_egg
                )
            }

            composable(route = ScoutingAppScreen.TeleOp.name) {
                TeleOpScreen(
                    onNetPointClicked = { viewModel.incrementTeleOpNetPoints() },
                    onBasketPointClicked = { viewModel.incrementTeleOpBasketPoints() },
                    onUndo = { viewModel.undoAction() },
                    basketPoints = uiState.teleopBasketPoints,
                    netPoints = uiState.teleopNetPoints,
                    stackView = { Text(viewModel.undoStack.toString()) },
                    debug_mode = easter_egg
                )
            }

            composable(route = ScoutingAppScreen.About.name) {
                AboutScreen(snackbarHostState = snackbarHostState, onEasterEgg = { easter_egg = !easter_egg }, easter_egg)
            }

            composable(route = ScoutingAppScreen.Save.name) {
                SaveScreen(snackbarHostState = snackbarHostState, onSave = {
                    createFile(Path(Environment.getExternalStorageDirectory().toString(), "scouting").toString().toUri(), "${uiState.compId}-${uiState.matchId}-${uiState.scouter}", uiState.toString())
                })
            }
        }
    }
}

const val CREATE_FILE = 1

private fun createFile(pickerInitialUri: Uri, fileName: String, content: String) {
    val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
        addCategory(Intent.CATEGORY_OPENABLE)
        type = "text/plain"
        putExtra(Intent.EXTRA_TITLE, "$fileName.txt")
        putExtra(Intent.EXTRA_TEXT, content)
    }
    ActivityCompat.startActivityForResult(Activity(), intent, CREATE_FILE, null)
}