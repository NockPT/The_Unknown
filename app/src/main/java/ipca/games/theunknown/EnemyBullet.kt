package ipca.games.theunknown

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Rect
import java.util.*

class EnemyBullet {

    var x: Int = 0
    var y: Int = 0
    var speed: Int = 0

    private var maxY = 0
    private var minY = 0
    private var maxX = 0
    private var minX = 0

    val collissionDetection : Rect
    var bitmap : Bitmap

    var generator = Random()


    constructor(context: Context,
                borderWidth: Int,
                borderHeight: Int, enemy: Enemy
    ) {

        maxX = borderWidth
        maxY = borderHeight
        minX = 0
        minY = 100
        speed = 8


        bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.enemy_bullet)

        x = enemy.x + (enemy.bitmap.width / 2) - (bitmap.width / 2)
        y = enemy.y + enemy.bitmap.height

        collissionDetection = Rect(x, y, bitmap.width, bitmap.height)

    }

    fun update(enemy: Enemy) {

        if(enemy.color == Color.WHITE){
            y += speed
        }

        if(enemy.color == Color.RED){
            y += speed * 2
        }

        if(enemy.color == Color.GREEN){
            y += speed / 2
        }


        collissionDetection.set(x,y, x + bitmap.width, y + bitmap.height)
    }
}