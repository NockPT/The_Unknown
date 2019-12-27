package ipca.games.theunknown

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect

class SpacePlayer {
    var bitmap : Bitmap
    var x : Int = 0
    var y : Int = 0
    var speed : Int = 0

    private val gravity : Int = -10
    private var min_speed = 1
    private var max_speed = 20
    private var maxY = 0
    private var maxX = 0
    private var minX = 0
    private var minY = 0
    val collissionDetection : Rect

    constructor(context: Context, borderWidth : Int, borderHeight : Int){

        bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.spaceship_0)


        maxY = borderHeight - bitmap.height
        maxX = borderWidth - bitmap.width

        y = maxY
        x = (borderWidth / 2)

        collissionDetection = Rect(x, y, bitmap.width, bitmap.height)
    }

    fun update(){
        if(x < minX && x < 500) x = minX
        if (x > maxX && x < 500)  x = maxX

        collissionDetection.set(x,y, x + bitmap.width, y + bitmap.height)
    }
}