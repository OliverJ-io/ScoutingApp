package io.oliverj.scoutingapp.ui

import androidx.lifecycle.ViewModel
import io.oliverj.scoutingapp.data.ScoutingUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.Stack

enum class ScoutingActions {
    INC_AUTON_BASKET_POINTS,
    INC_AUTON_NET_POINTS,
    INC_TELEOP_BASKET_POINTS,
    INC_TELEOP_NET_POINTS,
    DEC_AUTON_BASKET_POINTS,
    DEC_AUTON_NET_POINTS,
    DEC_TELEOP_BASKET_POINTS,
    DEC_TELEOP_NET_POINTS
}

class ScoutingViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ScoutingUiState())
    val uiState: StateFlow<ScoutingUiState> = _uiState.asStateFlow()

    var undoStack: Stack<ScoutingActions> = Stack()

    fun incrementAutonBasketPoints(is_undo: Boolean = false) {
        _uiState.update { currentState ->
            currentState.copy(
                autonBasketPoints = currentState.autonBasketPoints + 1,
                autonPoints = currentState.autonPoints + 1,
                points = currentState.points + 1
            )
        }
        if (is_undo) return
        undoStack.push(ScoutingActions.INC_AUTON_BASKET_POINTS)
    }

    fun incrementAutonNetPoints(is_undo: Boolean = false) {
        _uiState.update { currentState ->
            currentState.copy(
                autonNetPoints = currentState.autonNetPoints + 1,
                autonPoints = currentState.autonPoints + 1,
                points = currentState.points + 1
            )
        }
        if (is_undo) return
        undoStack.push(ScoutingActions.INC_AUTON_NET_POINTS)
    }

    fun incrementTeleOpBasketPoints(is_undo: Boolean = false) {
        _uiState.update { currentState ->
            currentState.copy(
                teleopBasketPoints = currentState.teleopBasketPoints + 1,
                teleopPoints = currentState.teleopPoints + 1,
                points = currentState.points + 1
            )
        }
        if (is_undo) return
        undoStack.push(ScoutingActions.INC_TELEOP_BASKET_POINTS)
    }

    fun incrementTeleOpNetPoints(is_undo: Boolean = false) {
        _uiState.update { currentState ->
            currentState.copy(
                teleopNetPoints = currentState.teleopNetPoints + 1,
                teleopPoints = currentState.teleopPoints + 1,
                points = currentState.points + 1
            )
        }
        if (is_undo) return
        undoStack.push(ScoutingActions.INC_TELEOP_NET_POINTS)
    }

    fun decrementAutonBasketPoints(is_undo: Boolean = false) {
        _uiState.update { currentState ->
            currentState.copy(
                autonBasketPoints = currentState.autonBasketPoints - 1,
                autonPoints = currentState.autonPoints - 1,
                points = currentState.points - 1
            )
        }
        if (is_undo) return
        undoStack.push(ScoutingActions.DEC_AUTON_BASKET_POINTS)
    }

    fun decrementAutonNetPoints(is_undo: Boolean = false) {
        _uiState.update { currentState ->
            currentState.copy(
                autonNetPoints = currentState.autonNetPoints - 1,
                autonPoints = currentState.autonPoints - 1,
                points = currentState.points - 1
            )
        }
        if (is_undo) return
        undoStack.push(ScoutingActions.DEC_AUTON_NET_POINTS)
    }

    fun decrementTeleOpBasketPoints(is_undo: Boolean = false) {
        _uiState.update { currentState ->
            currentState.copy(
                teleopBasketPoints = currentState.teleopBasketPoints - 1,
                teleopPoints = currentState.teleopPoints - 1,
                points = currentState.points - 1
            )
        }
        if (is_undo) return
        undoStack.push(ScoutingActions.DEC_TELEOP_BASKET_POINTS)
    }

    fun decrementTeleOpNetPoints(is_undo: Boolean = false) {
        _uiState.update { currentState ->
            currentState.copy(
                teleopNetPoints = currentState.teleopNetPoints - 1,
                teleopPoints = currentState.teleopPoints - 1,
                points = currentState.points - 1
            )
        }
        if (is_undo) return
        undoStack.push(ScoutingActions.DEC_TELEOP_NET_POINTS)
    }

    fun undoAction() {
        if (undoStack.empty()) return
        val last = undoStack.pop()
        when(last) {
            ScoutingActions.INC_AUTON_BASKET_POINTS -> decrementAutonBasketPoints(is_undo = true)
            ScoutingActions.INC_AUTON_NET_POINTS -> decrementAutonNetPoints(is_undo = true)
            ScoutingActions.INC_TELEOP_BASKET_POINTS -> decrementTeleOpBasketPoints(is_undo = true)
            ScoutingActions.INC_TELEOP_NET_POINTS -> decrementTeleOpNetPoints(is_undo = true)
            ScoutingActions.DEC_AUTON_BASKET_POINTS -> incrementAutonBasketPoints(is_undo = true)
            ScoutingActions.DEC_AUTON_NET_POINTS -> incrementAutonNetPoints(is_undo = true)
            ScoutingActions.DEC_TELEOP_BASKET_POINTS -> incrementTeleOpBasketPoints(is_undo = true)
            ScoutingActions.DEC_TELEOP_NET_POINTS -> incrementTeleOpNetPoints(is_undo = true)
            else -> {}
        }

    }
}