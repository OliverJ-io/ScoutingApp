package io.oliverj.scoutingapp.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import io.oliverj.scoutingapp.ScoutingAppScreen
import io.oliverj.scoutingapp.ui.components.MatchScreen
import io.oliverj.scoutingapp.ui.components.NextButton

@Composable
fun TeleOpScreen(
    basketPoints: Int,
    netPoints: Int,
    onNetPointClicked: () -> Unit,
    onBasketPointClicked: () -> Unit,
    onUndo: () -> Unit,
    stackView: @Composable () -> Unit = {},
    debug_mode: Boolean,
    navController: NavHostController,
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
        modifier,
        true
    )
}