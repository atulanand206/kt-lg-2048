package com.creations.games.two048.scenes.scene3

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Align
import com.creations.games.engine.dependency.DI
import com.creations.games.engine.scenes.Scene
import com.creations.games.engine.values.Values
import com.creations.games.two048.asset.FontSize
import com.creations.games.two048.scenes.scene3.gameObjects.GameBoard
import com.creations.games.two048.utils.assets

class Scene3(private val di: DI) : Scene(di){
    //Vector2 is a pair of floats
    private val screenCenter = Vector2(Values.VIRTUAL_WIDTH/2f, Values.VIRTUAL_HEIGHT/2f)

    private val assets = di.assets

    lateinit var board:GameBoard

    //init is the constructor
    init {
        addTitle()

        addBoard()
    }

    private fun addTitle() {
        val title = assets.createLabel("Minesweeper", size = FontSize.F14, color = Color.BLACK)
        title.setPosition(Values.VIRTUAL_WIDTH/2f, Values.VIRTUAL_HEIGHT, Align.top)
        addObjToScene(title)
    }

    private fun addBoard() {
        board = GameBoard(di)
        val boardSize = Values.VIRTUAL_HEIGHT * 0.8f
        board.setSize(boardSize, boardSize)
        board.setRowSize(10)
        board.setNumMines(10)

        board.setPosition(screenCenter.x, screenCenter.y, Align.center)

        board.generate()

        addObjToScene(board)
        addObjToScene(board.message)

        Gdx.input.inputProcessor = stage
    }

    override fun act(dt: Float) {

    }
}

/**
 * Complete below tasks. you can make your own decisions and choices wherever required
 * The aim of this scene is to make a minesweeper game. It's a very famous game, you can read the rules on internet
 *
 * - Wrong square is being revealed on clicking a square, fix this bug
 * - Currently, on clicking a square only a single square is revealed. Make this behavior similar to minesweeper where an entire mine-free region is revealed.
 * - The number assigned to a square represents the number of mines in the surrounding eight cells. Fix the logic of generating this number
 * - Add the functionality of flagging a square with right-click by adding code to rightClicked function of cell class. You can find a flag asset through di.assets
 * - Add logic to detect game win or loss and show the following text on screen:
 *   ~ Game Over - if player clicked a mine
 *   ~ Game Complete - If player reveals all squares except the mines. In this case, also reveal all the mines as well
 */