package com.cayzerok.world

import com.cayzerok.core.*
import com.cayzerok.render.*
import com.google.gson.Gson
import org.joml.Matrix4f
import org.joml.Vector3f
import java.io.File
import java.io.FileNotFoundException

val Land1 = Layer("main1")
val Land2 = Layer("main2")
val gson = Gson()
val layerList = listOf(Land1, Land2)

object World{
    val width = 64
    val height = 64
    var scale = 71f
    var projection = Matrix4f().setTranslation(Vector3f(0f)).scale(World.scale)
    var wayMap:Array<Boolean?> =  Array(World.width*World.height,{false})

    fun loadWays() {
        try {
            wayMap = gson.fromJson(File(path + "levels/wayMap.lvl").readText(), Array<Boolean?>::class.java)
        }catch (e:FileNotFoundException) {}
    }
    fun setWays(way:Boolean, x: Int, y: Int) {
        wayMap[x+y*World.width] = way
    }

    fun getWay(x: Int, y: Int): Boolean? {
        return wayMap[x+y*World.width]
    }

    fun saveWays() {
        val wayMapString = gson.toJson(wayMap)
        File(path+"levels/wayMap.lvl").writeText(wayMapString)
    }

    fun correctCamera() {
        val w = -World.width*World.scale*2
        val h = World.height*World.scale*2

        if(mainCamera.camPosition.x> -(mainWindow.width/2)+ World.scale)
            mainCamera.camPosition.x = -(mainWindow.width/2)+ World.scale
        if(mainCamera.camPosition.x < w+(mainWindow.width/2)+ World.scale)
            mainCamera.camPosition.x = w+(mainWindow.width/2)+ World.scale

        if(mainCamera.camPosition.y < mainWindow.height/2-World.scale)
            mainCamera.camPosition.y = mainWindow.height/2-World.scale
        if(mainCamera.camPosition.y > h-mainWindow.height/2-World.scale)
            mainCamera.camPosition.y = h-mainWindow.height/2-World.scale

        if(player.position.x> World.scale+0.5f)
            player.position.x = World.scale+0.5f
        if(player.position.x < w+World.scale-0.5f)
            player.position.x = w+World.scale-0.5f

        if(player.position.y < -World.scale+0.5f)
            player.position.y = -World.scale+0.5f
        if(player.position.y > h-World.scale-0.5f)
            player.position.y = h-World.scale-0.5f
    }
}

class Layer(val name:String){

    private var sheet:Array<Int?> = Array(World.width*World.height,{null})
    private var angleSheet:Array<Double?> =  Array(World.width*World.height,{0.0})

    fun renderIt() {
        for (y in 0 until World.height)
            for (x in 0 until World.width) {
                if (sheet[x+y*World.width] != null) {
                    render.renderTile(sheet[x+y*World.width]!!,x.toFloat(),-y.toFloat(), angle = angleSheet[x+y*World.width]!!)
                }
            }
    }

    fun setTile(tile: Int?, x:Int, y:Int, angle:Double = 0.0) {
        sheet[x+y*World.width] = tile
        angleSheet[x+y*World.width] = angle
    }

    fun getTile(x:Int,y:Int): Tile? {
        try {
            return tiles[sheet[x+y*World.width]!!]
        } catch (e:ArrayIndexOutOfBoundsException) {return null}
    }




    fun loadWorld() {
        try {
            sheet = gson.fromJson(File(path + "levels/" + name + ".lvl").readText(), Array<Int?>::class.java)
            angleSheet = gson.fromJson(File(path+"levels/"+name+".ang").readText(),Array<Double?>::class.java)
        } catch (e:FileNotFoundException){}

    }
    fun saveWorld() {
        val lvlString = gson.toJson(sheet)
        val angleString = gson.toJson(angleSheet)
        File(path+"levels/"+name+".lvl").writeText(lvlString)
        File(path+"levels/"+name+".ang").writeText(angleString)
    }
}