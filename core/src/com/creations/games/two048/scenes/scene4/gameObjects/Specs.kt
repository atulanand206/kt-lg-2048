package com.creations.games.two048.scenes.scene4.gameObjects

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.MathUtils
import com.creations.games.engine.dependency.DI

enum class PieceType {
    LARGE, SQUARE, ANGLE, ANGLE_REVERSE, ZIGZAG, ZIGZAG_REVERSE
}

fun pieceFromType(di: DI, type: PieceType) = when(type) {
    PieceType.SQUARE -> PieceSquare(di)
    PieceType.ANGLE -> PieceAngle(di)
    PieceType.ANGLE_REVERSE -> PieceAngleReverse(di)
    PieceType.LARGE -> PieceLarge(di)
    PieceType.ZIGZAG -> PieceZigzag(di)
    PieceType.ZIGZAG_REVERSE -> PieceZigzagReverse(di)
}

val Colors = arrayOf(Color.BLACK, Color.RED, Color.BLUE, Color.MAROON, Color.FOREST)

data class Block(var color: Color, var checked: Boolean)

data class ColoredPiece(var color: Color, var type: PieceType)

fun randomColoredPiece() = ColoredPiece(randomColor(), randomPieceType())

private fun randomPieceType() = PieceType.values()[MathUtils.random(PieceType.values().size - 1)]

private fun randomColor() = Colors[MathUtils.random(Colors.size - 1)]

class PieceLarge(private val di: DI): Piece(di) {
    override fun getPieces(): Array<IntArray> {
        return arrayOf(intArrayOf(1, 0), intArrayOf(-1, 0), intArrayOf(-2, 0))
    }

    override fun getPieces90deg(): Array<IntArray> {
        return arrayOf(intArrayOf(0, 1), intArrayOf(0, -1), intArrayOf(0, 2))
    }

    override fun getPieces180deg(): Array<IntArray> {
        return arrayOf(intArrayOf(1, 0), intArrayOf(-1, 0), intArrayOf(2, 0))
    }

    override fun getPieces270deg(): Array<IntArray> {
        return arrayOf(intArrayOf(0, 1), intArrayOf(0, -1), intArrayOf(0, -2))
    }
}

class PieceSquare(private val di: DI): Piece(di) {
    override fun canRotate(): Boolean {
        return false
    }

    override fun getPieces(): Array<IntArray> {
        return arrayOf(intArrayOf(1, 0), intArrayOf(0, 1), intArrayOf(1, 1))
    }

    override fun getPieces90deg(): Array<IntArray> {
        return arrayOf(intArrayOf(1, 0), intArrayOf(0, 1), intArrayOf(1, 1))
    }

    override fun getPieces180deg(): Array<IntArray> {
        return arrayOf(intArrayOf(1, 0), intArrayOf(0, 1), intArrayOf(1, 1))
    }

    override fun getPieces270deg(): Array<IntArray> {
        return arrayOf(intArrayOf(1, 0), intArrayOf(0, 1), intArrayOf(1, 1))
    }
}

class PieceAngle(private val di: DI): Piece(di) {
    override fun getPieces(): Array<IntArray> {
        return arrayOf(intArrayOf(1, 0), intArrayOf(-1, 0), intArrayOf(1, 1))
    }

    override fun getPieces90deg(): Array<IntArray> {
        return arrayOf(intArrayOf(0, 1), intArrayOf(0, -1), intArrayOf(1, -1))
    }

    override fun getPieces180deg(): Array<IntArray> {
        return arrayOf(intArrayOf(1, 0), intArrayOf(-1, 0), intArrayOf(-1, -1))
    }

    override fun getPieces270deg(): Array<IntArray> {
        return arrayOf(intArrayOf(0, 1), intArrayOf(0, -1), intArrayOf(-1, 1))
    }
}

class PieceAngleReverse(private val di: DI): Piece(di) {
    override fun getPieces(): Array<IntArray> {
        return arrayOf(intArrayOf(1, 0), intArrayOf(-1, 0), intArrayOf(-1, 1))
    }

    override fun getPieces90deg(): Array<IntArray> {
        return arrayOf(intArrayOf(0, 1), intArrayOf(0, -1), intArrayOf(1, 1))
    }

    override fun getPieces180deg(): Array<IntArray> {
        return arrayOf(intArrayOf(1, 0), intArrayOf(-1, 0), intArrayOf(1, -1))
    }

    override fun getPieces270deg(): Array<IntArray> {
        return arrayOf(intArrayOf(0, 1), intArrayOf(0, -1), intArrayOf(-1, -1))
    }
}

class PieceZigzag(private val di: DI): Piece(di) {
    override fun getPieces(): Array<IntArray> {
        return arrayOf(intArrayOf(-1, 0), intArrayOf(0, 1), intArrayOf(1, 1))
    }

    override fun getPieces90deg(): Array<IntArray> {
        return arrayOf(intArrayOf(0, 1), intArrayOf(1, 0), intArrayOf(1, -1))
    }

    override fun getPieces180deg(): Array<IntArray> {
        return arrayOf(intArrayOf(0, -1), intArrayOf(1, 0), intArrayOf(-1, -1))
    }

    override fun getPieces270deg(): Array<IntArray> {
        return arrayOf(intArrayOf(-1, 0), intArrayOf(-1, 1), intArrayOf(0, -1))
    }
}

class PieceZigzagReverse(private val di: DI): Piece(di) {
    override fun getPieces(): Array<IntArray> {
        return arrayOf(intArrayOf(-1, 1), intArrayOf(0, 1), intArrayOf(1, 0))
    }

    override fun getPieces90deg(): Array<IntArray> {
        return arrayOf(intArrayOf(1, 1), intArrayOf(1, 0), intArrayOf(0, -1))
    }

    override fun getPieces180deg(): Array<IntArray> {
        return arrayOf(intArrayOf(1, -1), intArrayOf(0, -1), intArrayOf(-1, 0))
    }

    override fun getPieces270deg(): Array<IntArray> {
        return arrayOf(intArrayOf(-1, -1), intArrayOf(-1, 0), intArrayOf(0, 1))
    }
}