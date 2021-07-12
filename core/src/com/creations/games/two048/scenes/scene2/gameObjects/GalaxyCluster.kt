package com.creations.games.two048.scenes.scene2.gameObjects

import com.badlogic.gdx.math.MathUtils
import com.creations.games.engine.dependency.DI
import com.creations.games.engine.gameObject.GameObject
import com.creations.games.engine.values.Values
import com.creations.games.two048.utils.assets

class GalaxyCluster(private val di: DI): GameObject() {

    private val textures = di.assets.galaxies

    init{
        setSize(Values.VIRTUAL_WIDTH, Values.VIRTUAL_HEIGHT)
        addGalaxies()
    }

    private fun addGalaxies() {
        for (i in 0 until 6) {
            val g = Galaxy(di, textures[i % textures.size])
            g.setPosition(MathUtils.random(width), MathUtils.random(height))
            addActor(g)
        }
    }
}