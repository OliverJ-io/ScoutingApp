package io.oliverj.scoutingapp.ui

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import io.oliverj.scoutingapp.ScoutingAppScreen
import io.oliverj.scoutingapp.ui.components.MatchScreen
import io.oliverj.scoutingapp.ui.components.NextButton
import io.oliverj.scoutingapp.ui.components.SimpleCheckBox

@Composable
fun AutonScreen(
    netPoints: Int,
    basketPoints: Int,
    onNetPointClicked: () -> Unit,
    onBasketPointClicked: () -> Unit,
    onUndo: () -> Unit,
    stackView: @Composable () -> Unit = {},
    debug_mode: Boolean,
    navController: NavHostController,
    viewModel: ScoutingViewModel,
    modifier: Modifier = Modifier
) {
    MatchScreen(
        netPoints,
        basketPoints,
        onNetPointClicked,
        onBasketPointClicked,
        onUndo,
        stackView,
        debug_mode,
        navController,
        modifier
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            SimpleCheckBox(
                viewModel.uiState.collectAsState().value.crossedLine,
                "Crossed Line",
                modifier = Modifier.padding(5.dp).scale(1.3f)
            ) { crossedLine ->
                viewModel.crossedLine(crossedLine)
            }
        }
    }
}