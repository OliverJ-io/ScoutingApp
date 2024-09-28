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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import io.oliverj.scoutingapp.ScoutingAppScreen
import io.oliverj.scoutingapp.ui.components.NextButton

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
    modifier: Modifier = Modifier
) {

    var netPoint = netPoints
    var basketPoint = basketPoints

    Column {
        Row(
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(5.dp).fillMaxWidth()
        ) {
            NextButton(
                nextPage = ScoutingAppScreen.TeleOp.name,
                navController = navController
            )
        }
        if (debug_mode) {
            Text("[DEBUG]")
            Text(netPoint.toString())
            Text(basketPoint.toString())
            stackView()
            Text("[END DEBUG]")
        }

        Text("Net Points: $netPoint")
        Text("Basket Points: $basketPoint")

        Column(
            verticalArrangement = Arrangement.Bottom,
            modifier = Modifier.fillMaxHeight()
        ) {
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