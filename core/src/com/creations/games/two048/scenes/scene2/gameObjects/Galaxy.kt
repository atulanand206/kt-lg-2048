package com.creations.games.two048.scenes.scene2.gameObjects;

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.MathUtils
import com.creations.games.engine.dependency.DI
import com.creations.games.engine.gameObject.GameObject
import com.creations.games.engine.values.Values


class Galaxy(private val di: DI, private val texture: Texture) : GameObject() {

    private val sizeMultiplier = MathUtils.random(0.7f)
    private var scrollSpeed = -0.1f

    init {
        setSize(texture.width.toFloat(), texture.height.toFloat())
        rotation = MathUtils.random(360f)
    }

    override fun act(dt: Float) {
        this.moveBy(scrollSpeed, 0f)
        if (x < 0) {
            x += Values.VIRTUAL_WIDTH
        }
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        val scaledWidth = width * sizeMultiplier
        val scaledHeight = height * sizeMultiplier

        batch.color = color
        batch.draw(texture,x - scaledWidth/2,y - scaledHeight/2, scaledWidth, scaledHeight)
        batch.color = Color.WHITE
    }
}
