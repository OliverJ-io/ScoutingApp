package io.oliverj.scoutingapp.data

import kotlinx.serialization.Serializable

@Serializable
data class ScoutingUiState(
    val compId: String = "",
    val matchId: String = "1",
    val teamId: String = "",
    val scouter: String = "",
    val alliance: String = "blue",
    val robotId: String = "left",
    val autonNetScores: Int = 0,
    val autonNetPoints: Int = 0,
    var autonBasketScores: Int = 0,
    val autonBasketPoints: Int = 0,
    val autonScores: Int = 0,
    val autonPoints: Int = 0,
    val teleopNetScores: Int = 0,
    val teleopNetPoints: Int = 0,
    val teleopBasketScores: Int = 0,
    val teleopBasketPoints: Int = 0,
    val teleopScores: Int = 0,
    val teleopPoints: Int = 0,
    val scores: Int = 0,
    val points: Int = 0,
    val didClimb: Boolean = false,
    val crossedLine: Boolean = false,
    val comment: String = ""
)