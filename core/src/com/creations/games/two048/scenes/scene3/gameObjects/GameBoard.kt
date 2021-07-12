package com.creations.games.two048.scenes.scene3.gameObjects

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.utils.Align
import com.creations.games.engine.dependency.DI
import com.creations.games.engine.gameObject.GameObject
import com.creations.games.engine.values.Values
import com.creations.games.two048.asset.FontSize
import com.creations.games.two048.utils.assets

class GameBoard(private val di: DI) : GameObject() {

    val message = di.assets.createLabel("", size = FontSize.F14, color = Color.BLACK)

    private var cellsRevealed = 0
    private var cellsToReveal: Int = 0
    private var mineRevealed = false
    private var rowSize = 0
    private var numMines = 0
    private var directions = arrayOf(intArrayOf(-1, -1), intArrayOf(-1, 0), intArrayOf(-1, 1),
            intArrayOf(0, -1), intArrayOf(0, 1),
            intArrayOf(1, -1), intArrayOf(1, 0), intArrayOf(1, 1)
    )

    private var cells = ArrayList<ArrayList<Cell>>()

    init {
        message.setPosition(Values.VIRTUAL_WIDTH/2f, 5f, Align.bottom)
        addActor(message)
    }

    fun setRowSize(sz: Int) {
        rowSize = sz
    }

    fun setNumMines(num: Int) {
        numMines = num
    }

    fun generate() {
        val cellSize = width / rowSize
        cellsToReveal = rowSize * rowSize - numMines
        for (i in 0 until rowSize) {
            cells.add(ArrayList())

            for (j in 0 until rowSize) {
                val c = Cell(di, i, j)
                c.setSize(cellSize, cellSize)
                c.setPosition(i * cellSize, j * cellSize)
                addActor(c)

                cells[i].add(c)
            }
        }

        addMines()
        addNumbers()
    }

    private fun addMines() {
        var minesLeft = numMines
        while (minesLeft > 0) {
            val r = MathUtils.random(rowSize - 1)
            val c = MathUtils.random(rowSize - 1)

            if (!cells[r][c].hasMine) {
                cells[r][c].addMine()
                minesLeft -= 1
            }
        }
    }

    private fun addNumbers() {
        for (i in 0 until rowSize) {
            for (j in 0 until rowSize) {
                if (cells[i][j].hasMine) continue
                var x = 0
                for (direction in directions) {
                    val nx = i + direction[0]
                    val ny = j + direction[1]
                    if (isValid(nx, ny) && cells[nx][ny].hasMine) x++
                }
                // only add number to the cell if a positive value.
                if (x != 0) cells[i][j].addNumber(x)
                else cells[i][j].empty()
            }
        }
    }

    private fun isValid(x: Int, y: Int): Boolean {
        return x >= 0 && y >= 0 && x < rowSize && y < rowSize
    }

    fun cellFlagged(i: Int, j: Int) {
        // no change when the mine is revealed, viz., game is lost
        if (mineRevealed) {
            return
        }

        // flag the cell only when not revealed
        if (!cells[i][j].isRevealed) cells[i][j].flag()
    }

    fun cellClicked(i: Int, j: Int) {
        // no change when the mine is revealed, viz., game is lost
        if (mineRevealed) {
            return
        }
        // declare the game as loss on clicking a mine
        if (cells[i][j].hasMine) {
            gameOver()
            cells[i][j].reveal()
            return
        }

        // reveal the clicked cell if is not empty and without a mine.
        // if a cell is empty, open the entire mine-free region and the neighbouring valued cells.
        if (cells[i][j].isEmpty) reveal(i, j)
        else if (!cells[i][j].isRevealed) {
            cellsRevealed++
            cells[i][j].reveal()
        }

        // declare the game as win when all cells other than mines are revealed
        if (!mineRevealed && cellsRevealed == cellsToReveal) {
            gameWin()
        }
    }

    private fun reveal(i: Int, j: Int) {
        // no change when a revealed cell is tried to be revealed.
        if (cells[i][j].isRevealed || !cells[i][j].isEmpty) return

        // reveal the cell if empty.
        if (cells[i][j].isEmpty && !cells[i][j].isRevealed) {
            cellsRevealed++
            cells[i][j].reveal()
        }

        // depth-first search recursion on all the neighbouring cells.
        // auxiliary visited matrix was not required because isRevealed flag has taken that role.
        for (direction in directions) {
            val nx = i + direction[0]
            val ny = j + direction[1]
            if (isValid(nx, ny))
                if (cells[nx][ny].isEmpty) reveal(nx, ny)
                else if (!cells[nx][ny].hasMine && !cells[nx][ny].isRevealed) {
                    cellsRevealed++
                    cells[nx][ny].reveal()
                }
        }
    }

    private fun revealAll() {
        for (i in 0 until rowSize) for (j in 0 until rowSize) cells[i][j].reveal()
    }

    private fun gameOver() {
        mineRevealed = true
        message.setText("Game Over")
    }

    private fun gameWin() {
        message.setText("Game Complete")
        revealAll()
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        batch.color = Color(0.9f, 0.9f, 0.9f, 1f)
        batch.draw(di.assets.rect, x, y, width, height)
        batch.color = Color.WHITE

        super.draw(batch, parentAlpha)
    }
}