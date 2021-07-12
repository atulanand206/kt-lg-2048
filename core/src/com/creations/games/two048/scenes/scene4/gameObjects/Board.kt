package com.creations.games.two048.scenes.scene4.gameObjects

import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Align
import com.creations.games.engine.dependency.DI
import com.creations.games.engine.gameObject.GameObject
import com.creations.games.engine.gameObject.addInputListener
import com.creations.games.engine.values.Values
import com.creations.games.engine.widgets.TextLabel
import com.creations.games.two048.asset.FontSize
import com.creations.games.two048.utils.assets
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.max

class Board(private val di: DI): GameObject() {

    private val rectTexture = di.assets.rect

    private val screenCenter = Vector2(Values.VIRTUAL_WIDTH/2f, Values.VIRTUAL_HEIGHT/2f)
    private val boardColor = Color.ORANGE
    private val cellSide = 24f
    private val rows = 20
    private val cols = 12
    private val nextItems = 3

    private val sxBoard = screenCenter.x  - cols * cellSide / 2
    private val syBoard = screenCenter.y  - rows * cellSide / 2
    private val sxHold = screenCenter.x - cols * cellSide / 2 - 140f
    private val syHold = screenCenter.y + (rows - nextItems * 6) * cellSide
    private val sxNext = screenCenter.x + cols * cellSide / 2 + 30f
    private val syNext = screenCenter.y + (rows - nextItems * 8) * cellSide

    private var posX = cols / 2
    private var posY = rows - 2
    private var fastFall = false
    private var elapsedTime = 0f
    private var slowElapsedTime = 0f
    private val frequency = 0.1f
    private val slowFrequency = 1f
    private var tapDownTime = Date()
    private val tapThreshold = 1000
    private var score = 0
    private var activePieceSpec = randomColoredPiece()
    private var isFinished = false

    private val blocks = ArrayList<Cell>()
    private val blockedCells = Array(rows) { Array(cols) { Block(Color.RED, false) } }
    private val maxRows = Array(cols) { -1 }
    private val nextPieceTypes = ArrayList<ColoredPiece>()
    private val nextPieces = ArrayList<Piece>()

    private var pieceHeldInTurn = false
    private var pieceHeld = false
    private lateinit var heldPieceSpec: ColoredPiece
    private lateinit var heldPiece: Piece

    private lateinit var activePiece: Piece
    private lateinit var projection: Piece
    private lateinit var scoreBoard: TextLabel

    init {
        this.addInputListener()
        renderTitle()
        renderBlocks()
        renderActivePiece()
        generateNextPieces()
        renderNextPieces()
        renderScore()
    }

    private fun renderTitle() {
        val title = di.assets.createLabel("Tetris", size = FontSize.F14, color = Color.BLACK)
        title.setPosition(Values.VIRTUAL_WIDTH/2, Values.VIRTUAL_HEIGHT - 20f, Align.top)
        addActor(title)
    }

    private fun renderBlocks() {
        for (i in blockedCells.indices) {
            for (j in blockedCells[i].indices) {
                if (blockedCells[i][j].checked) {
                    blocks.add(renderCell(blockedCells[i][j].color, sxBoard + j * cellSide, syBoard + i * cellSide))
                }
            }
        }
    }

    private fun renderCell(color: Color, x: Float, y: Float): Cell {
        val cell = Cell(di)
        cell.color = color
        cell.setPosition(x, y)
        addActor(cell)
        return cell
    }

    private fun renderActivePiece() {
        activePiece = renderPiece(activePieceSpec)
        for (i in 1..activePiece.top()) activePiece.moveDown()
        renderProjection(activePieceSpec.type)
    }

    private fun renderPiece(coloredPiece: ColoredPiece): Piece {
        return renderPiece(coloredPiece.type, coloredPiece.color, 0f, sxBoard + cols * cellSide / 2, syBoard + rows * cellSide, 1f, true)
    }

    private fun renderPiece(type: PieceType, color: Color, rotation: Float, x: Float, y: Float, scale: Float = 1f, main: Boolean = false): Piece {
        val piece = pieceFromType(di, type)
        piece.cellSide *= scale
        piece.color = color
        piece.initPiece()
        piece.setPosition(x, y)
        val r = (rotation / 90f).toInt()
        for (i in 1..r) piece.rotate()
        if (main) {
            if (!isConfigValid(piece.currentConfig())) {
                isFinished = true
                renderFinish()
                return piece
            }
        }
        addActor(piece)
        return piece
    }

    private fun renderProjection(type: PieceType) {
        projection = renderPiece(type, Color.WHITE, 0f, sxBoard + cols * cellSide / 2, syBoard + (activePiece.bottom() + 1) * cellSide)
        offsetProjection()
    }

    private fun offsetProjection() {
        var row: Int = maxRows[posX]
        for (config in activePiece.currentConfig()) {
            row = if (config[1] < 0) max(row, maxRows[posX + config[0]] - config[1])
            else max(row, maxRows[posX + config[0]])
        }
        projection.offset = row * cellSide
        projection.refresh()
    }

    private fun generateNextPieces() {
        for (i in 1..nextItems) nextPieceTypes.add(randomColoredPiece())
    }

    private fun generateNextPiece() {
        activePiece.remove()
        projection.remove()
        if (isFinished) return
        activePieceSpec = nextPieceTypes[0]
        nextPieceTypes.removeAt(0)
        nextPieceTypes.add(randomColoredPiece())
        renderActivePiece()
        renderNextPieces()
    }

    private fun renderNextPieces() {
        nextPieces.forEach{
            it.remove()
        }
        for (i in nextPieceTypes.indices) {
            nextPieces.add(renderPiece(nextPieceTypes[i].type, nextPieceTypes[i].color,0f,
                    sxNext + 2.5f * cellSide,syNext + (2 + 4 * i) * cellSide))
        }
    }

    private fun generateHeldPiece() {
        if (pieceHeldInTurn) return
        if (pieceHeld) {
            heldPiece.remove()
            val spec = heldPieceSpec
            heldPieceSpec = activePieceSpec
            activePieceSpec = spec
            heldPiece = renderHeldPiece()
            activePiece.remove()
            projection.remove()
            renderActivePiece()
        } else {
            heldPieceSpec = activePieceSpec
            heldPiece = renderHeldPiece()
            pieceHeld = true
            generateNextPiece()
        }
        pieceHeldInTurn = true
    }

    private fun renderHeldPiece() = renderPiece(heldPieceSpec.type, heldPieceSpec.color, 0f,
            sxHold + 2.5f * cellSide, syHold + (2) * cellSide)

    private fun renderScore() {
        scoreBoard = di.assets.createLabel("0", size = FontSize.F14, color = Color.BLACK)
        scoreBoard.setPosition(Values.VIRTUAL_WIDTH-40f, Values.VIRTUAL_HEIGHT - 20f, Align.top)
        addActor(scoreBoard)
    }

    private fun renderFinish() {
        val finish = di.assets.createLabel("Game over. Score is $score", size = FontSize.F20, color = Color.BLACK)
        finish.zIndex = 2
        finish.setPosition(Values.VIRTUAL_WIDTH / 2, Values.VIRTUAL_HEIGHT - 50f, Align.center)
        addActor(finish)
    }

    override fun keyDown(keycode: Int): Boolean {
        when (keycode) {
            Input.Keys.UP -> {
                rotate()
            }
            Input.Keys.DOWN -> {
                fastFall = true
                tapDownTime = Date()
            }
            Input.Keys.LEFT -> {
                moveLeft()
            }
            Input.Keys.RIGHT -> {
                moveRight()
            }
            Input.Keys.SPACE -> {
                generateHeldPiece()
            }
        }
        return super.keyDown(keycode)
    }

    override fun keyUp(keycode: Int): Boolean {
        when (keycode) {
            Input.Keys.DOWN -> {
                if (Date().time - tapDownTime.time < tapThreshold) {
                    moveToBottom()
                }
                fastFall = false
            }
        }
        return super.keyUp(keycode)
    }

    private fun canRotate(): Boolean {
        return isConfigValid(activePiece.nextConfig())
    }

    private fun isConfigValid(nextConfig: Array<IntArray>): Boolean {
        if (blockedCells[posY][posX].checked) {
            return false
        }
        for (config in nextConfig) {
            if (!isValid(posY + config[1], posX + config[0])) {
                return false
            }
            if (blockedCells[posY + config[1]][posX + config[0]].checked) {
                return false
            }
        }
        return true
    }

    private fun rotate() {
        if (!canRotate()) return
        activePiece.rotate()
        projection.rotate()
        checkLeftOnRotate()
        checkRightOnRotate()
        offsetProjection()
    }

    private fun checkRightOnRotate() {
        if (activePiece.right() + posX == cols) {
            posX--
            activePiece.moveLeft()
            projection.moveLeft()
        }
    }

    private fun checkLeftOnRotate() {
        if (activePiece.left() == posX + 1) {
            posX++
            activePiece.moveRight()
            projection.moveRight()
        }
    }

    private fun canMove(x: Int, y: Int): Boolean {
        val config = activePiece.currentConfig()
        if (posY - activePiece.bottom() == 0) {
            return false
        }
        if (!isValid(posY + y,  posX + x)) return false;
        for (conf in config) {
            if (!isValid(posY + y + conf[1], posX + x + conf[0]))
                return false
            if (blockedCells[posY + y + conf[1]][posX + x + conf[0]].checked)
                return false
        }
        return true
    }

    private fun moveLeft() {
        if (canMove(-1, 0)) {
            posX--
            activePiece.moveLeft()
            projection.moveLeft()
            offsetProjection()
        }
    }

    private fun moveRight() {
        if (canMove(1, 0)) {
            posX++
            activePiece.moveRight()
            projection.moveRight()
            offsetProjection()
        }
    }

    private fun moveToBottom() {
        while (canMove(0, -1))
            moveDown()
    }

    private fun moveDown() {
        if (canMove(0, -1)) {
            posY--
            activePiece.moveDown()
        } else {
            placePiece()
            renderBlocks()
            generateNextPiece()
            removeLineIfComplete()
        }
    }

    private fun placePiece() {
        blockedCells[posY][posX].checked = true
        blockedCells[posY][posX].color =  activePiece.color
        val config = activePiece.currentConfig()
        for (conf in config)
            if (isValid(posY + conf[1], posX + conf[0])) {
                blockedCells[posY + conf[1]][posX + conf[0]].checked = true
                blockedCells[posY + conf[1]][posX + conf[0]].color =  activePiece.color
            }
        if (blockedCells.isNotEmpty())
            for (i in blockedCells[0].indices) {
                for (j in blockedCells.size - 1 downTo 0) {
                    if (blockedCells[j][i].checked) {
                        maxRows[i] = j
                        break
                    }
                }
            }
        posX = cols / 2
        posY = rows - 2
        pieceHeldInTurn = false
    }

    private fun isValid(x: Int, y: Int): Boolean {
        if (x < 0 || y < 0) return false
        if (x >= blockedCells.size || y >= blockedCells[x].size) return false
        return true
    }

    private fun removeLineIfComplete() {
        removeCurrentBlocks()
        val removals = rowsToRemove()
        removeRows(removals)
        updateScore(removals)
        renderBlocks()
    }

    private fun removeCurrentBlocks() {
        blocks.forEach {
            it.remove()
        }
    }

    private fun rowsToRemove(): ArrayList<Int> {
        val removals = ArrayList<Int>()
        for (i in blockedCells.indices) {
            val toRemove = canRemove(i)
            if (toRemove) {
                removals.add(i)
            }
        }
        removals.reverse()
        return removals
    }

    private fun canRemove(i: Int): Boolean {
        var toRemove = true
        for (j in blockedCells[i].indices) {
            if (!blockedCells[i][j].checked) {
                toRemove = false
                break
            }
        }
        return toRemove
    }

    private fun removeRows(removals: ArrayList<Int>) {
        for (i in removals) {
            for (j in i until blockedCells.size - 1) {
                blockedCells[j] = blockedCells[j + 1]
            }
            blockedCells[blockedCells.size - 1] = Array(cols) { Block(Color.RED, false) }
        }
    }

    private fun updateScore(removals: ArrayList<Int>) {
        score +=  removals.size * (removals.size + 1) / 2
        scoreBoard.setText(score)
    }

    override fun act(dt: Float) {
        elapsedTime += dt
        if (elapsedTime > frequency) {
            if (fastFall) {
                moveDown()
            }
            elapsedTime -= frequency
        }

        slowElapsedTime += dt
        if (slowElapsedTime > slowFrequency) {
            moveDown()
            slowElapsedTime -= slowFrequency
        }
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        val padding = 4f
        batch.color = boardColor
        batch.draw(rectTexture,
                sxBoard,
                syBoard,
                cols * cellSide + 2 * padding, rows *  cellSide + 2 * padding)
        batch.draw(rectTexture,
                sxNext,
                syNext,
                cellSide * 5 + 2 * padding, nextItems * cellSide * 4 + 2 * padding)
        batch.draw(rectTexture,
                sxHold,
                syHold,
                cellSide * 5, cellSide * 5)
        batch.color = Color.WHITE
        super.draw(batch, parentAlpha)
    }
}