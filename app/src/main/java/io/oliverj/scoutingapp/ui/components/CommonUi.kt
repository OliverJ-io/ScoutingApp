package io.oliverj.scoutingapp.ui.components

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.JsonObject
import java.nio.file.Files

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
    }) {
        if (!isCsv) {
            Text(text = "Add to Files")
        } else {
            Text(text = "Save as CSV")
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
            type = "text/plain"
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
        label = { Text(label) }
    )
}

@Composable
fun SimpleCheckBox(initialState: Boolean, label: String = "Check", onChange: (Boolean) -> Unit = {}) {
    var checked by remember { mutableStateOf(initialState) }

    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
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