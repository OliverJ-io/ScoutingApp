package io.oliverj.scoutingapp.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.oliverj.scoutingapp.data.ScoutingUiState
import io.oliverj.scoutingapp.ui.components.ComplexDialog
import io.oliverj.scoutingapp.ui.components.FileSaving
import io.oliverj.scoutingapp.ui.components.SheetSaving
import io.oliverj.scoutingapp.ui.components.SimpleAlertDialog
import io.oliverj.scoutingapp.ui.components.SimpleCheckBox
import io.oliverj.scoutingapp.ui.components.SimpleTextArea
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.csv.Csv
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalSerializationApi::class)
@Composable
fun SaveScreen(
    snackbarHostState: SnackbarHostState,
    fileName: String,
    fileData: String,
    viewModel: ScoutingViewModel,
    onComplete: () -> Unit = {},
    modifier: Modifier = Modifier
) {

    var showAlert: MutableState<Boolean> = mutableStateOf(false)

    Column {
        Column {
            SimpleCheckBox(viewModel.uiState.collectAsState().value.didClimb, "Did Climb") {
                viewModel.didClimb(it)
            }

            SimpleTextArea(viewModel.uiState.collectAsState().value.comment, "Comment") {
                viewModel.setComment(it)
            }
        }
        ComplexDialog(
            "Complete Match",
            showAlert,
            { showAlert.value = false }
        ) {
            Column (
                modifier = Modifier.padding(5.dp)
            ) {
                Text("Be sure to save before continuing", modifier = Modifier.padding(5.dp))
                Row (
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(5.dp)
                ) {
                    FileSaving(
                        fileName,
                        Csv {
                            hasHeaderRecord = true
                        }.encodeToString(
                            ScoutingUiState.serializer(),
                            Json.decodeFromString(ScoutingUiState.serializer(), fileData)
                        ),
                        isCsv = true
                    )
                    Button(
                        onClick = onComplete,
                        modifier = Modifier.padding(5.dp)
                    ) {
                        Text("Continue")
                    }
                }
            }
        }
        Column(
            verticalArrangement = Arrangement.Bottom,
            modifier = Modifier.fillMaxHeight()
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = { showAlert.value = true },
                    modifier = Modifier.padding(5.dp)
                ) {
                    Text("Complete")
                }
            }
        }
    }
}