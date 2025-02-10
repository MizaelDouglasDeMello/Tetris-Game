package br.com.mizaeldouglas.tetris_game.model
import kotlin.random.Random

class TetrisGame(
    // Board de 20 linhas x 10 colunas; 0: célula vazia, valores > 0: peça travada
    val board: Array<IntArray> = Array(20) { IntArray(10) { 0 } },
    var score: Int = 0,
    var gameOver: Boolean = false
) {
    var activePiece: TetrominoInstance? = null

    companion object {
        // Definição dos Tetrominoes com rotações e cores individuais
        val tetrominoes = listOf(
            TetrominoShape(
                rotations = listOf(
                    listOf(0 to -1, 0 to 0, 0 to 1, 0 to 2),
                    listOf(-1 to 1, 0 to 1, 1 to 1, 2 to 1)
                ),
                color = 0xFF00BCD4.toInt(), // I - ciano
                id = 1
            ),
            TetrominoShape(
                rotations = listOf(
                    listOf(0 to 0, 0 to 1, 1 to 0, 1 to 1)
                ),
                color = 0xFFFFEB3B.toInt(), // O - amarelo
                id = 2
            ),
            TetrominoShape(
                rotations = listOf(
                    listOf(0 to -1, 0 to 0, 0 to 1, 1 to 0),
                    listOf(-1 to 0, 0 to 0, 1 to 0, 0 to 1),
                    listOf(0 to -1, -1 to 0, 0 to 0, 0 to 1),
                    listOf(0 to -1, -1 to 0, 0 to 0, 1 to 0)
                ),
                color = 0xFF9C27B0.toInt(), // T - roxo
                id = 3
            ),
            TetrominoShape(
                rotations = listOf(
                    listOf(0 to 0, 0 to 1, 1 to -1, 1 to 0),
                    listOf(-1 to 0, 0 to 0, 0 to 1, 1 to 1)
                ),
                color = 0xFF4CAF50.toInt(), // S - verde
                id = 4
            ),
            TetrominoShape(
                rotations = listOf(
                    listOf(0 to -1, 0 to 0, 1 to 0, 1 to 1),
                    listOf(-1 to 1, 0 to 0, 0 to 1, 1 to 0)
                ),
                color = 0xFFF44336.toInt(), // Z - vermelho
                id = 5
            ),
            TetrominoShape(
                rotations = listOf(
                    listOf(0 to -1, 0 to 0, 0 to 1, 1 to -1),
                    listOf(-1 to 0, 0 to 0, 1 to 0, -1 to -1),
                    listOf(0 to -1, 0 to 0, 0 to 1, -1 to 1),
                    listOf(-1 to 0, 0 to 0, 1 to 0, 1 to 1)
                ),
                color = 0xFF2196F3.toInt(), // J - azul
                id = 6
            ),
            TetrominoShape(
                rotations = listOf(
                    listOf(0 to -1, 0 to 0, 0 to 1, 1 to 1),
                    listOf(-1 to 0, 0 to 0, 1 to 0, -1 to 1),
                    listOf(0 to -1, 0 to 0, 0 to 1, -1 to -1),
                    listOf(-1 to 0, 0 to 0, 1 to 0, 1 to -1)
                ),
                color = 0xFFFF9800.toInt(), // L - laranja
                id = 7
            )
        )
    }

    // Retorna a lista de coordenadas absolutas da peça ativa
    fun getActivePieceCells(): List<Pair<Int, Int>> {
        val instance = activePiece ?: return emptyList()
        val offsets = instance.shape.rotations[instance.rotation]
        return offsets.map { (dr, dc) ->
            instance.centerRow + dr to instance.centerCol + dc
        }
    }

    // Verifica se um conjunto de células é válido (dentro do board e sem colisão)
    private fun isValidPosition(cells: List<Pair<Int, Int>>): Boolean {
        for ((r, c) in cells) {
            if (r !in board.indices || c !in board[0].indices) return false
            if (board[r][c] != 0) return false
        }
        return true
    }

    // Gera uma nova peça se não houver peça ativa.
    // Se a posição inicial não for válida, o jogo está acabado.
    fun spawnPiece() {
        if (activePiece != null || gameOver) return
        val shape = tetrominoes.random(Random(System.currentTimeMillis()))
        val centerCol = board[0].size / 2
        activePiece = TetrominoInstance(shape, rotation = 0, centerRow = 0, centerCol = centerCol)
        if (!isValidPosition(getActivePieceCells())) {
            activePiece = null
            gameOver = true
        }
    }

    // Move a peça ativa para baixo. Se não for possível, trava a peça, limpa linhas e incrementa a pontuação.
    fun moveDown() {
        activePiece?.let {
            val newInstance = it.copy(centerRow = it.centerRow + 1)
            val newCells = newInstance.shape.rotations[newInstance.rotation].map { (dr, dc) ->
                newInstance.centerRow + dr to newInstance.centerCol + dc
            }
            if (isValidPosition(newCells)) {
                it.centerRow++
            } else {
                // Travar a peça no board
                getActivePieceCells().forEach { (r, c) ->
                    if (r in board.indices && c in board[0].indices) {
                        board[r][c] = it.shape.id
                    }
                }
                activePiece = null
                score += 10  // Pontos por peça travada
                clearFullLines()
            }
        }
    }

    // Move a peça ativa para a esquerda
    fun moveLeft() {
        activePiece?.let {
            val newInstance = it.copy(centerCol = it.centerCol - 1)
            val newCells = newInstance.shape.rotations[newInstance.rotation].map { (dr, dc) ->
                newInstance.centerRow + dr to newInstance.centerCol + dc
            }
            if (isValidPosition(newCells)) {
                it.centerCol--
            }
        }
    }

    // Move a peça ativa para a direita
    fun moveRight() {
        activePiece?.let {
            val newInstance = it.copy(centerCol = it.centerCol + 1)
            val newCells = newInstance.shape.rotations[newInstance.rotation].map { (dr, dc) ->
                newInstance.centerRow + dr to newInstance.centerCol + dc
            }
            if (isValidPosition(newCells)) {
                it.centerCol++
            }
        }
    }

    // Roda a peça ativa, verificando se a nova rotação é válida
    fun rotateActivePiece() {
        activePiece?.let {
            val newRotation = (it.rotation + 1) % it.shape.rotations.size
            val rotatedOffsets = it.shape.rotations[newRotation]
            val newCells = rotatedOffsets.map { (dr, dc) ->
                it.centerRow + dr to it.centerCol + dc
            }
            if (isValidPosition(newCells)) {
                it.rotation = newRotation
            }
        }
    }

    // Verifica e elimina as linhas completas, deslocando o board para baixo.
    fun clearFullLines() {
        var fullLines = 0
        // Verifica cada linha
        for (i in board.indices) {
            if (board[i].all { it != 0 }) {
                fullLines++
                // Remove a linha completa
                for (r in i downTo 1) {
                    board[r] = board[r - 1].copyOf()
                }
                board[0] = IntArray(board[0].size) { 0 }
            }
        }
        // Incrementa a pontuação por linha eliminada (ex: 100 pontos por linha)
        score += fullLines * 100
    }
}