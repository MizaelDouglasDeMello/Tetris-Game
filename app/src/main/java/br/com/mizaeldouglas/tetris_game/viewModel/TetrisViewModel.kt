package br.com.mizaeldouglas.tetris_game.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.mizaeldouglas.tetris_game.model.TetrisGame
import java.util.*

class TetrisViewModel : ViewModel() {

    private val _game = MutableLiveData(TetrisGame())
    val game: LiveData<TetrisGame> = _game

    private var timer: Timer? = null
    private var spawnDelayTicks: Int = 0
    private val spawnDelayThreshold = 2

    fun startGame() {
        _game.value = TetrisGame()
        spawnDelayTicks = 0
        timer?.cancel()
        timer = Timer()
        timer?.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                _game.value?.let { currentGame ->
                    // Se o jogo acabou, interrompe as atualizações
                    if (currentGame.gameOver) {
                        stopGame()
                        return
                    }
                    if (currentGame.activePiece == null) {
                        if (spawnDelayTicks >= spawnDelayThreshold) {
                            currentGame.spawnPiece()
                            spawnDelayTicks = 0
                        } else {
                            spawnDelayTicks++
                        }
                    } else {
                        currentGame.moveDown()
                    }
                    _game.postValue(currentGame)
                }
            }
        }, 0, 500)
    }

    fun stopGame() {
        timer?.cancel()
        timer = null
    }

    fun moveLeft() {
        _game.value?.let { currentGame ->
            if (!currentGame.gameOver) {
                currentGame.moveLeft()
                _game.postValue(currentGame)
            }
        }
    }

    fun moveRight() {
        _game.value?.let { currentGame ->
            if (!currentGame.gameOver) {
                currentGame.moveRight()
                _game.postValue(currentGame)
            }
        }
    }

    fun moveDown() {
        _game.value?.let { currentGame ->
            if (!currentGame.gameOver) {
                currentGame.moveDown()
                _game.postValue(currentGame)
            }
        }
    }

    fun rotatePiece() {
        _game.value?.let { currentGame ->
            if (!currentGame.gameOver) {
                currentGame.rotateActivePiece()
                _game.postValue(currentGame)
            }
        }
    }

    fun restartGame() {
        stopGame()
        _game.value = TetrisGame()
        startGame()
    }

    override fun onCleared() {
        super.onCleared()
        timer?.cancel()
    }
}