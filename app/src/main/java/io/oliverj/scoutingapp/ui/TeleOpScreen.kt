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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TeleOpScreen(
    basketPoints: Int,
    netPoints: Int,
    onNetPointClicked: () -> Unit,
    onBasketPointClicked: () -> Unit,
    onUndo: () -> Unit,
    stackView: @Composable () -> Unit = {},
    debug_mode: Boolean,
    modifier: Modifier = Modifier
) {
    var netPoint = netPoints
    var basketPoint = basketPoints

    Column {
        if (debug_mode) {
            Text("[DEBUG]")
            Text(netPoint.toString())
            Text(basketPoint.toString())
            stackView()
            Text("[END DEBUG]")
        }

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