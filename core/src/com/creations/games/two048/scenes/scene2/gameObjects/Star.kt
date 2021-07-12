package com.creations.games.two048.scenes.scene2.gameObjects

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.MathUtils
import com.creations.games.engine.dependency.DI
import com.creations.games.engine.gameObject.GameObject
import com.creations.games.two048.utils.assets

class Star (private val di: DI): GameObject() {
    private val myTex = di.assets.stars[MathUtils.random(0, di.assets.stars.size - 1)]
    private val r = MathUtils.random(255f)
    private val g = MathUtils.random(255f)
    private val b = MathUtils.random(255f)
    private val a = MathUtils.random(1f)
    private val syncMultiplier = MathUtils.random(0.5f)
    private val meanScale = MathUtils.random( 0.05f)
    private var sizeMultiplier = meanScale

    private var elapsed = 0f

    init{
        setSize(myTex.width.toFloat(), myTex.height.toFloat())

        //give a random rotation to each star
        rotation = MathUtils.random(360f)
    }

    override fun act(dt: Float) {
        elapsed += dt * 360 * 0.5f * syncMultiplier
        sizeMultiplier = meanScale * (1 + MathUtils.sinDeg(elapsed))
        color.set(r, g, b, a)
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        val scaledWidth = width * sizeMultiplier
        val scaledHeight = height * sizeMultiplier

        batch.color = color
        batch.draw(myTex,x - scaledWidth/2,y - scaledHeight/2, scaledWidth, scaledHeight)
        batch.color = Color.WHITE
    }
}