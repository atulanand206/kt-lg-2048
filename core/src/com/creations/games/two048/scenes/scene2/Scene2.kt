package com.creations.games.two048.scenes.scene2

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Align
import com.creations.games.engine.dependency.DI
import com.creations.games.engine.scenes.Scene
import com.creations.games.engine.values.Values
import com.creations.games.two048.scenes.scene2.gameObjects.GalaxyCluster
import com.creations.games.two048.scenes.scene2.gameObjects.Rocket
import com.creations.games.two048.scenes.scene2.gameObjects.StarryBG

class Scene2(private val di: DI) : Scene(di){
    //todo.note - Scene follows a 2D cartesian coordinate system with origin at lower left
    //Vector2 is a pair of floats
    private val screenCenter = Vector2(Values.VIRTUAL_WIDTH/2f, Values.VIRTUAL_HEIGHT/2f)

    init {
        addSpaceBG()
        addRocket()
        addGalaxy()
    }

    private fun addSpaceBG() {
        //add the background
        val bg = StarryBG(di)
        addObjToScene(bg)
    }

    private fun addRocket() {
        val r = Rocket(di)
        r.setPosition(screenCenter.x, screenCenter.y, Align.center)
        addObjToScene(r)
        stage.keyboardFocus = r
    }

    private fun addGalaxy() {
        val g = GalaxyCluster(di)
        addObjToScene(g)
    }

    override fun act(dt: Float) {
        ///nothing to do yet
    }
}

/**
 * Complete below tasks. you can make your own decisions and choices wherever required
 *
 * - The background Nebula is going to the right way too fast.
 *   ~ Make it go to the left
 *   ~ Slow it down to a more natural looking speed
 *   ~ Ensure that it wraps around the screen just like it did when it was moving towards right.
 *     The part going out of the left side should come back in from right side
 *
 * - The stars are way too big and twinkle all together.
 *   ~ Make the average star size smaller
 *   ~ Make them twinkle out of sync with each other but keep the frequency same, i.e. each star should take same amount of time to complete the twinkling cycle
 *   ~ Since smaller stars appear way too sparse now. increase the star density
 *   ~ All stars are white, make them colorful (shades of reds, blues, yellows whatever you want)
 *
 * - There are some galaxy assets present in di.assets.galaxies array
 *   ~ Just like stars are sprinkled randomly, sprinkle some galaxies over the background and make them move slowly in the choice of your direction.
 *     Try to make it look appealing if possible
 *     Note :- Follow object oriented design principles and make appropriate classes etc
 *
 * - Spaceship direction can be controlled with left and right arrow keys and thrust can be applied by up arrow key.
 *   In order to slow down the ship, you need to turn around and apply thrust in opposite direction otherwise it will keep moving. It's in space after all!
 *   Spaceship currently goes out of screen
 *   ~ Make the spaceship come back into the screen from the opposite side.
 *     If part of the ship is protruding out of one edge of screen, that part should be coming in from the other edge (Just like Nebula)
 *   ~ Make the ship slowly rotate anticlockwise and clockwise on pressing left and right arrow keys respectively.
 *     The thrust applied on pressing up arrow key should be in the direction the ship is pointing in.
 *     This will allow player to move all over the screen instead of just left and right
 *     Note :- all 4 corners of the screen should wrap around. Top with bottom and left with right
 */