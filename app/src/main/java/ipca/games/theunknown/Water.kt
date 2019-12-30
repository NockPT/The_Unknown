package ipca.games.theunknown

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.util.*


class Water{

    var bitmap : Bitmap

    var x : Int = 0
    var y : Int = 0
    var speed : Int = 0

    private var maxY = 0
    private var minY = 0
    private var maxX = 0
    private var minX = 0

    constructor(context: Context, borderWidth : Int, borderHeight : Int){

        bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.water)



        maxX = borderWidth
        maxY = borderHeight

        minX = 0
        minY = 0

        var generator = Random()
        speed = generator.nextInt(10) + 10
        x = generator.nextInt(maxX)
        y = generator.nextInt(maxY)

    }

    fun Update(){
        y += speed

        if(y > maxY){
         y = 0
         var generator = Random()
         speed = generator.nextInt(10) + 10
         x = generator.nextInt(maxX)
        }
    }
}