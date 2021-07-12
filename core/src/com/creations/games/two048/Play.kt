package com.creations.games.two048

import com.badlogic.gdx.assets.loaders.FileHandleResolver
import com.badlogic.gdx.graphics.g2d.Batch
import com.creations.games.engine.EngineGame
import com.creations.games.engine.dependency.assetManager
import com.creations.games.engine.scenes.Scene
import com.creations.games.engine.values.Values
import com.creations.games.two048.asset.Assets
import com.creations.games.two048.scenes.scene4.Scene4

class Play(
    resolver: FileHandleResolver
) : EngineGame(resolver, defaultAspectRatio = Pair(16f, 16f)) {
    private lateinit var scene:Scene

    init{
        //todo.note - Change screen scale below
        Values.screenFactor = 40f;
    }

    //This is called at the beginning. We load the assets first
    override fun onCreate() {
        //add assets to DI(Dependency Injector)
        di.add(Assets::class.java, Assets(di.assetManager))
    }

    //At this point assets are loaded. You should create your game here
    override fun onLoaded() {
        //todo.note - Change the scene below
//        scene = Scene1(di)
//        scene = Scene2(di)
//        scene = Scene3(di)
        scene = Scene4(di)

        //add scene to DI
        di.add(Scene::class.java,scene)
    }

    //update is called once every frame
    override fun update(dt: Float) {
        scene.act(dt)
    }

    override fun draw(batch: Batch) {

    }
}