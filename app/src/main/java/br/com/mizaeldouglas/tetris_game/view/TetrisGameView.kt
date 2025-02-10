package br.com.mizaeldouglas.tetris_game.view


import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import br.com.mizaeldouglas.tetris_game.model.TetrisGame

class TetrisGameView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {

    var game: TetrisGame? = null

    // Pintura para o grid
    private val gridPaint = Paint().apply {
        color = Color.DKGRAY
        style = Paint.Style.STROKE
        strokeWidth = 2f
    }

    // Mapeia id do Tetromino para sua cor (para células travadas)
    private val colorMap = mapOf(
        1 to Color.parseColor("#00BCD4"), // I - ciano
        2 to Color.parseColor("#FFEB3B"), // O - amarelo
        3 to Color.parseColor("#9C27B0"), // T - roxo
        4 to Color.parseColor("#4CAF50"), // S - verde
        5 to Color.parseColor("#F44336"), // Z - vermelho
        6 to Color.parseColor("#2196F3"), // J - azul
        7 to Color.parseColor("#FF9800")  // L - laranja
    )

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        game?.let { game ->
            val numRows = game.board.size
            val numCols = game.board[0].size
            val cellWidth = width / numCols.toFloat()
            val cellHeight = height / numRows.toFloat()

            // Desenha o board
            for (row in 0 until numRows) {
                for (col in 0 until numCols) {
                    val cellValue = game.board[row][col]
                    val paint = Paint().apply {
                        color = if (cellValue != 0) colorMap[cellValue] ?: Color.GRAY
                        else Color.LTGRAY
                    }
                    val left = col * cellWidth
                    val top = row * cellHeight
                    val right = left + cellWidth
                    val bottom = top + cellHeight
                    canvas.drawRect(left, top, right, bottom, paint)
                    canvas.drawRect(left, top, right, bottom, gridPaint)
                }
            }

            // Desenha a peça ativa
            game.activePiece?.let { active ->
                val activeCells = game.getActivePieceCells()
                val activeColor = active.shape.color
                activeCells.forEach { (r, c) ->
                    if (r in 0 until numRows && c in 0 until numCols) {
                        val left = c * cellWidth
                        val top = r * cellHeight
                        val right = left + cellWidth
                        val bottom = top + cellHeight
                        canvas.drawRect(left, top, right, bottom, Paint().apply { color = activeColor })
                        canvas.drawRect(left, top, right, bottom, gridPaint)
                    }
                }
            }

            // Se o jogo acabou, exibe Game Over no centro
            if (game.gameOver) {
                val textPaint = Paint().apply {
                    color = Color.BLACK
                    textSize = 64f
                    isFakeBoldText = true
                    textAlign = Paint.Align.CENTER
                }
                canvas.drawText(
                    "Game Over ☠️",
                    (width / 2).toFloat(),
                    (height / 2).toFloat(),
                    textPaint
                )
            }
        }
    }
}