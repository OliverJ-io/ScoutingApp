package io.oliverj.scoutingapp.data

data class ScoutingUiState(
    val compId: String = "",
    val autonNetPoints: Int = 0,
    val autonBasketPoints: Int = 0,
    val autonPoints: Int = 0,
    val teleopNetPoints: Int = 0,
    val teleopBasketPoints: Int = 0,
    val teleopPoints: Int = 0,
    val points: Int = 0,
    val didClimb: Boolean = false,
    val scouter: String = "",
    val matchId: String = ""
)