package br.com.mizaeldouglas.tetris_game.model

data class TetrominoShape(
    val rotations: List<List<Pair<Int, Int>>>, // Cada rotação define offsets (linha, coluna) a partir do pivot
    val color: Int, // Cor da peça
    val id: Int     // Identificador único (usado no board)
)
