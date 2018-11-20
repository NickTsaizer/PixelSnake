package com.cayzerok.core

import com.cayzerok.render.player
import com.cayzerok.render.playerAngle
import com.cayzerok.world.TileList
import com.cayzerok.world.World
import com.cayzerok.world.layerList
import com.cayzerok.world.tiles
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.BufferUtils
import org.lwjgl.glfw.GLFWScrollCallback
import org.lwjgl.glfw.GLFWScrollCallbackI
import java.awt.SystemColor.window
import org.lwjgl.glfw.GLFW.glfwSetScrollCallback




val xBuffer = BufferUtils.createDoubleBuffer(1)
val yBuffer = BufferUtils.createDoubleBuffer(1)

private var layer:Int = 0

fun getInput(window:Long) {

    glfwGetCursorPos(input.window, xBuffer, yBuffer)
    cursorPos.x = (xBuffer.get(0).toFloat()-mainCamera.camPosition.x-mainWindow.width/2)
    cursorPos.y = (-yBuffer.get(0).toFloat()-mainCamera.camPosition.y+mainWindow.height/2)


    if( input.isKeyPressed(GLFW_KEY_E)) {
        if (player.invTileAngle == 0.0) {
            player.invTileAngle = 270.0
        } else player.invTileAngle -= 90.0
    }
    if( input.isKeyPressed(GLFW_KEY_Q)) {
        if (player.invTileAngle == 270.0) {
            player.invTileAngle = 0.0
        } else player.invTileAngle += 90.0
    }
    if( input.isKeyPressed(GLFW_KEY_ESCAPE)) {glfwSetWindowShouldClose(window,true)}
    if( input.isKeyDown(GLFW_KEY_W)) {
        player.move(0f,-15f,0f)
        playerAngle = 0f }
    if( input.isKeyDown(GLFW_KEY_A)) {
        player.move(15f,0f,0f)
        playerAngle = Math.toRadians(90.0).toFloat() }
    if( input.isKeyDown(GLFW_KEY_S)) {
        player.move(0f,15f,0f)
        playerAngle=Math.toRadians(180.0).toFloat() }
    if( input.isKeyDown(GLFW_KEY_D)) {
        player.move(-15f,0f,0f)
        playerAngle = Math.toRadians(270.0).toFloat() }

    when{
        input.isKeyDown(GLFW_KEY_W)&& input.isKeyDown(GLFW_KEY_A) -> playerAngle = Math.toRadians(45.0).toFloat()
        input.isKeyDown(GLFW_KEY_A)&& input.isKeyDown(GLFW_KEY_S) -> playerAngle = Math.toRadians(135.0).toFloat()
        input.isKeyDown(GLFW_KEY_S)&& input.isKeyDown(GLFW_KEY_D) -> playerAngle = Math.toRadians(225.0).toFloat()
        input.isKeyDown(GLFW_KEY_D)&& input.isKeyDown(GLFW_KEY_W) -> playerAngle = Math.toRadians(315.0).toFloat()
    }

    when {
        input.isKeyPressed(GLFW_KEY_UP) -> if (layer + 1 in 0..layerList.lastIndex) layer +=1
        input.isKeyPressed(GLFW_KEY_DOWN) -> if (layer - 1 in 0..layerList.lastIndex) layer -=1
    }

    if (input.isMouseButtonDown(0)) {
        layerList[layer].setTile(player.invTile, (cursorPos.x/100+0.5).toInt(),(-cursorPos.y/100+0.5).toInt(),player.invTileAngle)}
    if (input.isMouseButtonDown(1)) {layerList[layer].setTile(null, (cursorPos.x/100+0.5).toInt(),(-cursorPos.y/100+0.5).toInt())}
    if (input.scroll != 0.0) {
        if (tiles[player.invTile+input.scroll.toInt()] != null)
            player.invTile += input.scroll.toInt()
        input.scroll = 0.0
    }
}

class Input(val window: Long) {
    var keys = BooleanArray(GLFW_KEY_LAST,{false})
    var mouse = BooleanArray(GLFW_MOUSE_BUTTON_LAST,{false})

    var scroll = 0.0

    fun updateScroll() {

        glfwSetScrollCallback(window, GLFWScrollCallback.create { window, xoffset, yoffset ->
            scroll = yoffset
        })
    }

    fun isKeyDown(key:Int) : Boolean {
        return glfwGetKey(window,key) == 1
    }

    fun isKeyPressed(key:Int) : Boolean {
        return (isKeyDown(key) && !keys[key])
    }

    fun isKeyReleased(key:Int) : Boolean {
        return (!isKeyDown(key) && keys[key])
    }

    fun isMouseButtonDown(key:Int) : Boolean {
        return glfwGetMouseButton(window,key) == 1
    }

    fun isMouseButtonPressed(key:Int) : Boolean {
        return (isMouseButtonDown(key) && !mouse[key])
    }

    fun isMouseButtonReleased(key:Int) : Boolean {
        return (!isMouseButtonDown(key) && mouse[key])
    }

    fun update() {
        keys.forEachIndexed { index,it ->
            keys[index] = isKeyDown(index)
        }
        mouse.forEachIndexed{ index,it ->
            mouse[index] = isMouseButtonDown(index)
        }
        glfwPollEvents()
    }

}