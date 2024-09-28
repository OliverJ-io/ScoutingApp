package io.oliverj.scoutingapp.data

import kotlinx.serialization.Serializable

@Serializable
data class ScoutingUiState(
    val compId: String = "",
    val matchId: String = "1",
    val scouter: String = "",
    val alliance: String = "blue",
    val robotId: String = "left",
    val autonNetPoints: Int = 0,
    val autonBasketPoints: Int = 0,
    val autonPoints: Int = 0,
    val teleopNetPoints: Int = 0,
    val teleopBasketPoints: Int = 0,
    val teleopPoints: Int = 0,
    val points: Int = 0,
    val didClimb: Boolean = false
)