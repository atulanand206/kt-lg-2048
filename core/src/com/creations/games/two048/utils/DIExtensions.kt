package com.creations.games.two048.utils

import com.creations.games.engine.dependency.DI
import com.creations.games.engine.scenes.Scene
import com.creations.games.two048.asset.Assets

//todo.note - Add shared objects to dependency injector so that a reference can be passed to different gameObjects
val DI.scene get() = this[Scene::class.java]
val DI.assets get() = this[Assets::class.java]
