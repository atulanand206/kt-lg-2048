package com.creations.games.two048.scenes.scene4

import com.creations.games.engine.dependency.DI
import com.creations.games.engine.scenes.Scene
import com.creations.games.two048.scenes.scene4.gameObjects.Board

class Scene4(private val di: DI): Scene(di) {

    lateinit var board: Board

    init {
        addBoard()
    }

    private fun addBoard() {
        board = Board(di)
        addObjToScene(board)
        stage.keyboardFocus = board
    }

    override fun act(dt: Float) {
    }
}

/**
 *  1. Done - Multiple shapes of pieces.
 *  2. Done - Rotate 90deg clockwise - up arrow.
 *  3. Done - Pull down / swipe down puts the piece where it goes. - Down arrow hold
 *      a. Done - Few steps - faster move - hold down
 *      b. Done - All the way - instant - tap down
 *  4. Done - Left/Right arrow for move the piece.
 *  5. Done - Piece will stay at a level for x seconds.
 *  6. Done - Scoring
 *      a. Remove 1 line - 1pt
 *      b. Remove x line - x(x+1)/2 pt
 *  7. Done - When piece touches the floor, it can be moved till x seconds, then it flashes
 *      and becomes fixed.
 *  8. Done - Show 3 next pieces.
 *  9. Done - Pieces have different colors.
 *  10. Done - You get a chance to hold a piece only once per turn. - Swap using space bar.
 *  11. Done - Based on pivot point of shape, upon rotation if conflicting with already
 *      present blocks, stop rotation.*
 *  12. Done - Game over logic
 *  13. Done - Projection of where the piece will land.
 */