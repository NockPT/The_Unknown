package ipca.games.theunknown

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory

class Boom {

    var bitmap : Bitmap
    var resized : Bitmap
    var x : Int = 0
    var y : Int = 0

    constructor(context: Context, borderWidth : Int, borderHeight : Int){

        bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.boom)

        resized = Bitmap.createScaledBitmap(bitmap,(bitmap.getWidth() * 0.3f).toInt(), (bitmap.getHeight() * 0.3f).toInt(), true)


        x = -300
        y = -300

    }
}