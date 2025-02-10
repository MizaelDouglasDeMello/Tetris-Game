package br.com.mizaeldouglas.tetris_game.model

data class TetrominoInstance(
    val shape: TetrominoShape,
    var rotation: Int = 0,
    var centerRow: Int,
    var centerCol: Int
)