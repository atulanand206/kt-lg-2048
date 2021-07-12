package com.creations.games.two048.scenes.scene2.gameObjects

import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.creations.games.engine.dependency.DI
import com.creations.games.engine.gameObject.GameObject
import com.creations.games.engine.gameObject.addInputListener
import com.creations.games.engine.values.Values
import com.creations.games.two048.utils.assets
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class Rocket (private val di: DI): GameObject() {
    private var firingTex = di.assets.rocketFiring
    private var normalTex = di.assets.rocket

    private var currentTex = normalTex
    private var rotatingAngle = 0
    private var isRotating = false
    private var isFiring = false

    //physics data
    private val maxVel = 10f
    private val velocity = Vector2()

    init{
        val scl = 0.5f
        setSize(currentTex.width * scl, currentTex.height*scl)

        //todo.note - to make a GameObject receive user input, below function needs to be called
        this.addInputListener()
    }

    //called when a key is pressed
    override fun keyDown(keycode: Int): Boolean {
        when(keycode){
            Input.Keys.UP -> {
                isFiring = true
                currentTex = firingTex
            }
            Input.Keys.LEFT -> {
                isRotating = true;
                rotatingAngle = 1
            }
            Input.Keys.RIGHT -> {
                isRotating = true;
                rotatingAngle = -1
            }
        }
        return super.keyDown(keycode)
    }

    //called when a key is released
    override fun keyUp(keycode: Int): Boolean {
        when(keycode){
            Input.Keys.UP -> {
                isFiring = false
                currentTex = normalTex
            }
            Input.Keys.LEFT -> {
                isRotating = false
                rotatingAngle = 0
            }
            Input.Keys.RIGHT -> {
                isRotating = false
                rotatingAngle = 0
            }
        }
        return super.keyUp(keycode)
    }

    override fun act(dt: Float) {
        //accelerate if player is firing the rocket
        //since currently player can only move on x axis, calculations are simplified and only x component needs to be handled
        if(isFiring){
            velocity.add((dt * cos(rotation * PI / 180)).toFloat(), (dt * sin(rotation * PI / 180)).toFloat())
        }
        if (isRotating) {
            rotation = (rotation + rotatingAngle) % 360f
        }

        //limit velocity magnitude
        if (velocity.x > maxVel) velocity.x = maxVel
        if(velocity.y > maxVel) velocity.y = maxVel

        moveBy(velocity.x, velocity.y)

        //offset the rockets grid when going beyond the screen
        if (x < 0) x += Values.VIRTUAL_WIDTH
        if (x > Values.VIRTUAL_WIDTH) x -= Values.VIRTUAL_WIDTH
        if (y < 0) y += Values.VIRTUAL_HEIGHT
        if (y > Values.VIRTUAL_HEIGHT) y-= Values.VIRTUAL_HEIGHT
    }

    //relative position of the rockets in the grid
    private var directions = arrayOf(
            intArrayOf(-1, -1), intArrayOf(-1, 0),
            intArrayOf(0, -1), intArrayOf(0, 0),
    )

    override fun draw(batch: Batch, parentAlpha: Float) {
        //todo.note - draw function has many overrides. Below override can be used to draw a rotate and scaled texture
        //set the origin of rotation to center, scale is 1 because it is handled explicitly in size while calling setSize in init
        //renders the rocket grid
        for (direction in directions)
            batch.draw(TextureRegion(currentTex),
                    x + Values.VIRTUAL_WIDTH * direction[0],
                    y + Values.VIRTUAL_HEIGHT * direction[1],
                    width/2, height/2, width, height,
                    1f,1f,rotation)
    }
}
