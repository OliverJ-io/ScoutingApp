package io.oliverj.scoutingapp.ui.components

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import io.oliverj.scoutingapp.ScoutingAppScreen
import io.oliverj.scoutingapp.ui.ScoutingViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

@Composable
fun FileSaving(
    fileName: String,
    fileData: String,
    isCsv: Boolean = false
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val data = result.data
        val uri = data?.data
        uri?.let {
            context.contentResolver.openOutputStream(it)?.let { outputStream ->
                scope.launch {
                    withContext(Dispatchers.IO) {
                        outputStream.write(fileData.toByteArray())
                        outputStream.flush()
                        outputStream.close()
                    }
                }
            }
        }
    }

    Button(onClick = {
        val intent = createNewDocumentIntent(fileName, isCsv)
        launcher.launch(intent)
    }, modifier = Modifier.padding(5.dp)) {
        if (!isCsv) {
            Text(text = "Add to Files")
        } else {
            Text(text = "Save")
        }
    }
}

@Composable
fun SheetSaving(data: String) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, data)
        type = "text/plain"
    }

    val shareIntent = Intent.createChooser(sendIntent, null)

    Box(
        contentAlignment = Alignment.BottomEnd,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Button(
            onClick = {
                context.startActivity(shareIntent)
            }
        ) {
            Text("Save as CSV")
        }
    }
}

fun createNewDocumentIntent(fileName: String, isCsv: Boolean): Intent {
    if (!isCsv) {
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/json"
            putExtra(Intent.EXTRA_TITLE, "$fileName.json")
        }
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.setFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
        intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        return intent
    } else {
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "text/csv"
            putExtra(Intent.EXTRA_TITLE, "$fileName.csv")
        }
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.setFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
        intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        return intent
    }
}

@Composable
fun SimpleOutlinedTextField(initialContent: String, label: String = "Field", onChange: (String) -> Unit = {}) {
    var text by remember { mutableStateOf(initialContent) }

    OutlinedTextField(
        value = text,
        onValueChange = { text = it; onChange(it) },
        label = { Text(label) },
        singleLine = true,
        modifier = Modifier.padding(5.dp)
    )
}

@Composable
fun SimpleTextArea(initialContent: String, label: String = "Field", onChange: (String) -> Unit = {}) {
    var text by remember { mutableStateOf(initialContent) }

    OutlinedTextField(
        value = text,
        onValueChange = { text = it; onChange(it) },
        label = { Text(label) },
        modifier = Modifier.padding(5.dp)
    )
}

@Composable
fun SimpleCheckBox(initialState: Boolean, label: String = "Check", modifier: Modifier = Modifier, horizontalArrangement: Arrangement.Horizontal = Arrangement.Start, onChange: (Boolean) -> Unit = {}) {
    var checked by remember { mutableStateOf(initialState) }

    Row(
        horizontalArrangement = horizontalArrangement,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Text(text = label, style = MaterialTheme.typography.labelLarge)
        Checkbox(
            checked = checked,
            onCheckedChange = {checked = it; onChange(it) }
        )
    }
}

@Composable
fun SimpleAlertDialog(title: String, content: String, shouldShowDialog: MutableState<Boolean>, onConfirm: () -> Unit, onCancel: () -> Unit) {
    if (shouldShowDialog.value) {
        AlertDialog(
            onDismissRequest = {
                shouldShowDialog.value = false
                onCancel()
            },
            title = { Text(title) },
            text = { Text(content) },
            confirmButton = {
                Button(
                    onClick = {
                        shouldShowDialog.value = false
                        onConfirm()
                    }
                ) {
                    Text(text = "Confirm")
                }
            }
        )
    }
}

@Composable
fun ComplexDialog(title: String, shouldShowDialog: MutableState<Boolean>, onDismiss: () -> Unit = {}, content: @Composable () -> Unit = {}) {
    if (shouldShowDialog.value) {
        Dialog(onDismissRequest = onDismiss) {
            Card(
                shape = RoundedCornerShape(16.dp)
            ) {
                Column {
                    Text(title, style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(5.dp))
                    content()
                }
            }
        }
    }
}

@Composable
fun AllianceButtonGroup(
    label: String,
    options: List<String>,
    viewModel: ScoutingViewModel
) {
    var selectedString by remember { mutableStateOf(viewModel.uiState.value.alliance) }
    Column( modifier = Modifier.padding(5.dp) ) {
        Text(text = label, style = MaterialTheme.typography.labelLarge)
        SingleChoiceSegmentedButtonRow {
            options.forEachIndexed { index, label ->
                SegmentedButton(
                    shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size),
                    onClick = { selectedString = label.lowercase(); viewModel.setAlliance(label.lowercase()) },
                    selected = label.lowercase() == selectedString
                ) {
                    Text(label)
                }
            }
        }
    }
}

@Composable
fun RobotButtonGroup(
    label: String,
    options: List<String>,
    viewModel: ScoutingViewModel
) {
    var selectedString by remember { mutableStateOf(viewModel.uiState.value.robotId) }
    Column( modifier = Modifier.padding(5.dp) ) {
        Text(text = label, style = MaterialTheme.typography.labelLarge)
        SingleChoiceSegmentedButtonRow {
            options.forEachIndexed { index, label ->
                SegmentedButton(
                    shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size),
                    onClick = { selectedString = label.lowercase(); viewModel.setRobotId(label.lowercase()) },
                    selected = label.lowercase() == selectedString
                ) {
                    Text(label)
                }
            }
        }
    }
}

@Composable
fun NextButton(
    nextPage: String,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = { navController.navigate(nextPage) },
        modifier = modifier
    ) {
        Text("Next")
    }
}

@Composable
fun MatchScreen(
    netPoints: Int,
    basketPoints: Int,
    onNetPointClicked: () -> Unit,
    onBasketPointClicked: () -> Unit,
    onUndo: () -> Unit,
    stackView: @Composable () -> Unit = {},
    debug_mode: Boolean,
    navController: NavHostController,
    modifier: Modifier = Modifier,
    isTeleop: Boolean = false,
    additionalContent: @Composable () -> Unit = {}
) {

    var netPoint = netPoints
    var basketPoint = basketPoints

    var scroll by remember { mutableStateOf(ScrollState(0)) }

    Column {

        Column(
            modifier = Modifier.verticalScroll(scroll, reverseScrolling = true).fillMaxSize()
        ) {
            Row(
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(5.dp).fillMaxWidth()
            ) {
                if (isTeleop) {
                    NextButton(
                        nextPage = ScoutingAppScreen.Save.name,
                        navController = navController
                    )
                } else {
                    NextButton(
                        nextPage = ScoutingAppScreen.TeleOp.name,
                        navController = navController
                    )
                }
            }
            if (debug_mode) {
                Text("[DEBUG]")
                Text(netPoint.toString())
                Text(basketPoint.toString())
                stackView()
                Text("[END DEBUG]")
            }

            Text("Net Balls: $netPoint", style = MaterialTheme.typography.displayMedium)
            Text("Basket Balls: $basketPoint", style = MaterialTheme.typography.displayMedium)

        }

        Column(
            verticalArrangement = Arrangement.Bottom,
            modifier = Modifier.fillMaxHeight()
        ) {
            additionalContent()
            Row {
                Button(
                    onClick = {
                        netPoint += 1
                        onNetPointClicked()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .weight(0.5f)
                        .padding(5.dp),
                    shape = RoundedCornerShape(5.dp)
                ) {
                    Text("Net")
                }
                Button(
                    onClick = {
                        basketPoint += 1
                        onBasketPointClicked()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .weight(0.5f)
                        .padding(5.dp),
                    shape = RoundedCornerShape(5.dp)
                ) {
                    Text("Basket")
                }
            }
            Button(
                onClick = onUndo,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .padding(5.dp),
                shape = RoundedCornerShape(5.dp)
            ) {
                Text("Undo")
            }
        }
    }
}