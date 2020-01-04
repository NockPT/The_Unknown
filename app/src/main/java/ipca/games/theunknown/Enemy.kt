package ipca.games.theunknown

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Rect
import java.security.AccessControlContext
import java.util.*
import kotlin.math.max

class Enemy {
    var bitmap : Bitmap
    var x : Int = 0
    var y : Int = 0
    var speed : Int = 0
    var generator = Random()

    private val gravity : Int = -10
    private var min_speed = 1
    private var max_speed = 20
    private var maxY = 0
    private var maxX = 0
    private var minX = 0
    private var minY = 0
    val collissionDetection : Rect
    var color: Int

    constructor(context: Context, borderWidth : Int, borderHeight : Int){

        bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.enemy_0)

        maxY = borderHeight - bitmap.height
        maxX = borderWidth - bitmap.width

        speed = generator.nextInt(10) + 10
        x = generator.nextInt(maxX)
        y = 0

        color = Color.WHITE

        collissionDetection = Rect(x, y, bitmap.width, bitmap.height)
    }

    fun update(){

        if(color == Color.WHITE){
            y += speed
        }

        if(color == Color.RED){
            y += speed * 2
        }

        if(color == Color.GREEN){
            y += speed / 2
        }


        if (y - bitmap.height * 2 > maxY) {
            x = generator.nextInt(maxX)
            speed = generator.nextInt(10) + 10
            y = 0
            color = Color.WHITE
        }

        collissionDetection.set(x,y, x + bitmap.width, y + bitmap.height)
    }
}