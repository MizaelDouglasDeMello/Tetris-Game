package br.com.mizaeldouglas.tetris_game.view.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import br.com.mizaeldouglas.tetris_game.R
import br.com.mizaeldouglas.tetris_game.view.TetrisGameView
import br.com.mizaeldouglas.tetris_game.viewModel.TetrisViewModel

class MainActivity : AppCompatActivity() {

    private val viewModel: TetrisViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tetrisView = findViewById<TetrisGameView>(R.id.tetrisView)
        val startButton = findViewById<Button>(R.id.startButton)
        val stopButton = findViewById<Button>(R.id.stopButton)
        val leftButton = findViewById<Button>(R.id.leftButton)
        val rightButton = findViewById<Button>(R.id.rightButton)
        val downButton = findViewById<Button>(R.id.downButton)
        val rotateButton = findViewById<Button>(R.id.rotateButton)
        val restartButton = findViewById<Button>(R.id.restartButton)

        // Observa as alterações do jogo e atualiza a view e visibilidade dos botões
        viewModel.game.observe(this, Observer { game ->
            tetrisView.game = game
            tetrisView.invalidate()

            // Se game over, mostra o botão de restart, senão oculta
            if (game.gameOver) {
                restartButton.visibility = View.VISIBLE
            } else {
                restartButton.visibility = View.GONE
            }
        })

        fun showRestartButton() {
            val restartButton = findViewById<Button>(R.id.restartButton)
            restartButton.visibility = View.VISIBLE
        }

        startButton.setOnClickListener {
            viewModel.startGame()
        }

        stopButton.setOnClickListener {
            viewModel.stopGame()
        }

        leftButton.setOnClickListener {
            viewModel.moveLeft()
        }

        rightButton.setOnClickListener {
            viewModel.moveRight()
        }

        downButton.setOnClickListener {
            viewModel.moveDown()
        }

        rotateButton.setOnClickListener {
            viewModel.rotatePiece()
        }

        restartButton.setOnClickListener {
            viewModel.restartGame()
        }

    }
}