package com.creations.games.two048.scenes.scene3.gameObjects

import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.utils.Align
import com.creations.games.engine.dependency.DI
import com.creations.games.engine.gameObject.GameObject
import com.creations.games.engine.gameObject.addInputListener
import com.creations.games.engine.widgets.TextLabel
import com.creations.games.two048.asset.FontSize
import com.creations.games.two048.scenes.scene3.Scene3
import com.creations.games.two048.utils.assets
import com.creations.games.two048.utils.scene

class Cell(private val di: DI, i:Int, j:Int): GameObject() {
    var nearbyMinesLabel:TextLabel = di.assets.createLabel("", FontSize.F12, Color.BLACK)

    var hasMine = false
    var isRevealed = false
    var isEmpty = false
    var isFlagged = false
    var indices = Pair(i,j)

    init {
        addActor(nearbyMinesLabel)
        this.addInputListener()
    }

    //adds a mine to this cell
    fun addMine() {
        hasMine = true
        nearbyMinesLabel.setText("")
    }

    //adds number specifying surrounding mines
    fun addNumber(num: Int) {
        nearbyMinesLabel.setText(num.toString())
        nearbyMinesLabel.setSize(nearbyMinesLabel.reqWidth, nearbyMinesLabel.reqHeight)
        nearbyMinesLabel.setPosition(width/2, height/2, Align.center)
    }

    fun empty() {
        isEmpty = true
    }

    fun reveal() {
        isRevealed = true
    }

    fun flag() {
        isFlagged = true
    }

    //touchDown is used to detect touch screen input or left mouse button
    override fun touchDown(x: Float, y: Float, pointer: Int, button: Int): Boolean {
        if (button != Input.Buttons.LEFT) return false
        val scene = di.scene
        if(scene is Scene3){
            scene.board.cellClicked(indices.first, indices.second)
        }
        return super.touchDown(x, y, pointer, button)
    }

    override fun rightClicked(x: Float, y: Float) {
        val scene = di.scene
        if (scene is Scene3) {
            scene.board.cellFlagged(indices.first, indices.second)
        }
        return super.rightClicked(x, y)
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        val cellMargins = width * 0.05f

        //draw cell BG
        if(isRevealed) {
            batch.color = Color(0.95f, 0.95f, 0.95f, 1f)
        }
        batch.draw(di.assets.rect,x+cellMargins,y+cellMargins,width-2*cellMargins,height-2*cellMargins)
        batch.color = Color.WHITE

        //show flag when cell flagged
        if (!isRevealed && isFlagged) {
            batch.draw(di.assets.flag, x + cellMargins, y + cellMargins, width - 2 * cellMargins, height - 2 * cellMargins)
        }

        //draw cell contents if revealed
        if(isRevealed) {
            if (hasMine) {
                batch.draw(di.assets.mine, x + cellMargins, y + cellMargins, width - 2 * cellMargins, height - 2 * cellMargins)
            }

            //super.draw will draw the label for number
            super.draw(batch, parentAlpha)
        }
    }
}