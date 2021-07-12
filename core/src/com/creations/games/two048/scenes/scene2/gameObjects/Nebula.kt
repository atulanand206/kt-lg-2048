package com.creations.games.two048.scenes.scene2.gameObjects

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.creations.games.engine.dependency.DI
import com.creations.games.engine.gameObject.GameObject
import com.creations.games.engine.values.Values
import com.creations.games.two048.utils.assets

class Nebula (private val di: DI): GameObject() {
    private val myTex = di.assets.nebula

    private var scrollSpeed = -0.2f

    init{
        color = Color(1f,1f,1f,0.6f)

        val scale = Values.VIRTUAL_WIDTH / myTex.width
        setSize(myTex.width.toFloat() * scale, myTex.height.toFloat() * scale)
    }

    override fun act(dt: Float) {
        //since only moving in x, the y value of moveBy is 0
        this.moveBy(scrollSpeed, 0f)
        if(x < 0){
            x+=width
        }
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        batch.color = color
        batch.draw(myTex,x,y,width, height)
        batch.draw(myTex,x-width,y,width, height)
        batch.color = Color.WHITE
    }
}