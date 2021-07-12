package com.creations.games.two048.scenes.scene4.gameObjects

import com.badlogic.gdx.graphics.Color
import com.creations.games.engine.dependency.DI
import com.creations.games.engine.gameObject.GameObject
import kotlin.math.max
import kotlin.math.min

abstract class Piece(private val di: DI): GameObject() {

    private var rotationX = 0f
    var cellSide = 24f
    var offset = 0f
    private lateinit var pivot: Cell
    private val neighbours = ArrayList<Cell>()

    abstract fun getPieces(): Array<IntArray>

    abstract fun getPieces90deg(): Array<IntArray>

    abstract fun getPieces180deg(): Array<IntArray>

    abstract fun getPieces270deg(): Array<IntArray>

    fun top(): Int {
        val config = currentConfig()
        var x = 0
        for (conf in config)
            x = max(x, conf[1])
        return x
    }

    fun bottom(): Int {
        val config = currentConfig()
        var x = Int.MAX_VALUE
        for (conf in config)
            x = min(x, conf[1])
        return x * -1
    }

    fun left(): Int {
        val config = currentConfig()
        var x = Int.MAX_VALUE
        for (conf in config)
            x = min(x, conf[0])
        return x * -1
    }

    fun right(): Int {
        val config = currentConfig()
        var x = 0
        for (conf in config)
            x = max(x, conf[0])
        return x
    }

    open fun canRotate(): Boolean {
        return true;
    }

    fun initPiece() {
        addPiece()
    }

    fun moveDown() {
        this.moveBy(0f, -cellSide)
    }

    fun moveLeft() {
        this.moveBy(-cellSide, 0f)
    }

    fun moveRight() {
        this.moveBy(cellSide, 0f)
    }

    fun defaultDown() {
        for (i in 1..top())
            moveDown()
    }

    fun currentConfig(): Array<IntArray> {
        return when(rotationX) {
            0f -> getPieces()
            90f -> getPieces90deg()
            180f -> getPieces180deg()
            270f -> getPieces270deg()
            else -> getPieces()
        }
    }

    fun nextConfig(): Array<IntArray> {
        return when(rotationX) {
            270f -> getPieces()
            0f -> getPieces90deg()
            90f -> getPieces180deg()
            180f -> getPieces270deg()
            else -> getPieces90deg()
        }
    }

    fun rotate() {
        rotationX = (rotationX + 90f) % 360f
        val config = currentConfig()
        for (i in 0 until 3) {
            val conf = config[i]
            neighbours[i].setPosition(cellSide * conf[0], cellSide * conf[1])
        }
    }

    private fun addPiece() {
        pivot = addCell(color, x, y)
        val piecesConfig = currentConfig()
        for (pieceConfig in piecesConfig)
            neighbours.add(addCell(color, x + cellSide * pieceConfig[0], y + cellSide * pieceConfig[1]))
    }

    fun refresh() {
        pivot.y = offset
        for (it in neighbours.indices) {
            neighbours[it].y = pivot.y + cellSide * currentConfig()[it][1]
        }
    }

    private fun addCell(color: Color, x: Float, y: Float): Cell {
        val cell = Cell(di)
        cell.color = color
        cell.setPosition(x, y)
        addActor(cell)
        return cell
    }
}