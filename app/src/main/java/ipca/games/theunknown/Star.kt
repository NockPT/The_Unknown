package ipca.games.theunknown

import java.util.*


class Star{
    var x : Int = 0
    var y : Int = 0
    var speed : Int = 0

    private var maxY = 0
    private var minY = 0
    private var maxX = 0
    private var minX = 0

    constructor(borderWidth : Int, borderHeight : Int){

        maxX = borderWidth
        maxY = borderHeight

        minX = 0
        minY = 0

        var generator = Random()
        speed = generator.nextInt(10)
        x = generator.nextInt(maxX)
        y = generator.nextInt(maxY)

    }

    fun Update(){
        y += speed

        if(y > maxY){
         y = 0
         var generator = Random()
         speed = generator.nextInt(10)
         x = generator.nextInt(maxX)
        }
    }

    fun getStarWidth () : Float {
    var generator = Random()
    return generator.nextFloat() * 3.0f
    }
}