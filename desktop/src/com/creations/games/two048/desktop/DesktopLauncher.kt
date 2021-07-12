package com.creations.games.two048.desktop

import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver
import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.creations.games.engine.values.Values
import com.creations.games.two048.Play

object DesktopLauncher {
    private val cfg by lazy {
        LwjglApplicationConfiguration().apply {
            width = Values.VIRTUAL_WIDTH.toInt()
            height = Values.VIRTUAL_HEIGHT.toInt()

            useHDPI = true

            samples = 2
        }
    }

    @JvmStatic
    fun main(args: Array<String>) {
        LwjglApplication(Play(InternalFileHandleResolver()), cfg)
    }
}