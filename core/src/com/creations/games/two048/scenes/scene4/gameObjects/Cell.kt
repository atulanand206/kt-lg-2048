package com.creations.games.two048.scenes.scene4.gameObjects

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.creations.games.engine.dependency.DI
import com.creations.games.engine.gameObject.GameObject
import com.creations.games.two048.utils.assets

class Cell(private val di: DI): GameObject() {
    private val rectTexture = di.assets.margin
    private val cellSize = 20f
    override fun draw(batch: Batch, parentAlpha: Float) {
        batch.color = color
        batch.draw(rectTexture, x, y, cellSize, cellSize)
        batch.color = Color.WHITE
    }
}