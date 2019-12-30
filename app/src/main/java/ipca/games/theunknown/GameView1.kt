package ipca.games.theunknown

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat


class GameView1 : SurfaceView, Runnable {

    var playing : Boolean = false
    var gameThread : Thread? = null
    var score: Int = 0
    var viewWidth = 0
    var viewHeight = 0

    var player : Player
    var playerBullets : MutableList<PlayerBullet> = ArrayList<PlayerBullet>()


    var enemies : MutableList<Enemy> = ArrayList<Enemy>()
    var enemyBullets : MutableList<EnemyBullet> = ArrayList<EnemyBullet>()

    var paint : Paint
    var canvas :Canvas
    var surfaceHolder : SurfaceHolder

    var touched : Boolean = false
    var bulletTime : Float
    var dead : Boolean = false

    constructor(context: Context? , viewWidth : Int, viewHeight : Int) : super(context){
        player = Player(context!!, viewWidth, viewHeight)

        paint = Paint()
        paint.textSize = 50.0f
        canvas = Canvas()
        surfaceHolder = holder
        bulletTime = 3.0f

        this.viewWidth = viewWidth
        this.viewHeight = viewHeight

        for(x in 0 until 3){
            enemies.add(Enemy(context,viewWidth , viewHeight))
        }
        val tf = ResourcesCompat.getFont(context, R.font.agencyfb);
        paint.setTypeface(tf)

        val colorOrange = ContextCompat.getColor(context, R.color.orange)
        paint.color = colorOrange

    }

    override fun run() {
        while(playing) {
            update()
            draw()
            control()
        }
    }

    fun update() {
        bulletTime -= 0.2f
        player.update()

        for (e in enemies) {
            e.update()
            for (b in playerBullets){
                if (e.collissionDetection.intersect(b.collissionDetection)) {
                    e.y = viewHeight + 100
                    score += 100
                }
            }

            if (e.collissionDetection.intersect(player.collissionDetection)) {
                e.y = viewHeight + 100
                player.x = 1000
                dead = true
            }
        }

        for (b in playerBullets) {
            b.update()
        }

        for (eb in enemyBullets) {
            eb.update()
            if (eb.collissionDetection.intersect(player.collissionDetection)) {
                player.x = 1000
                dead = true
            }
        }


        if(bulletTime <= 0.0f){
            playerBullets.add(PlayerBullet(context!!, viewWidth, viewHeight, player))

            for (e in enemies) {
                enemyBullets.add(EnemyBullet(context!!, viewWidth, viewHeight, e))
            }

            bulletTime = 3.0f
        }

        if(dead) {
            for (b in playerBullets) {
                b.x = -1000
            }
            val intent = Intent().setClass(context, GameOverActivity::class.java)
            intent.putExtra("SCORE", score)
            (context as Activity).startActivity(intent)
        }

        if(score >= 100){
            val intent = Intent().setClass(context, GameActivity2::class.java)
            intent.putExtra("SCORE", score)
            (context as Activity).startActivity(intent)
        }
    }

    fun draw() {
        if (surfaceHolder.surface.isValid) {
            canvas = surfaceHolder.lockCanvas()
            canvas.drawColor(getResources().getColor(R.color.water))

            canvas.drawBitmap(player.bitmap!!, player.x.toFloat(), player.y.toFloat(), Paint())

            for ( e in enemies){
                canvas.drawBitmap(e.bitmap!!, e.x.toFloat(),e.y.toFloat(), Paint())
            }

            for (b in playerBullets) {
                canvas.drawBitmap(b.bitmap!!, b.x.toFloat(), b.y.toFloat(), Paint())
            }

            for ( eb in enemyBullets){
                canvas.drawBitmap(eb.bitmap!!,eb.x.toFloat(),eb.y.toFloat(), Paint())
            }

            canvas.drawText("Score: " + score, 50.0f, 100.0f, paint)

            surfaceHolder.unlockCanvasAndPost(canvas)
        }
    }

    fun control(){
        Thread.sleep(17)
    }

    fun pause(){
        playing = false
        gameThread!!.join()
    }
    fun resume(){
        playing = true
        gameThread = Thread(this)
        gameThread!!.start()

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let { ev ->
            when(ev.action.and(MotionEvent.ACTION_MASK)){
                MotionEvent.ACTION_DOWN->{
                    if (event.x.toInt() >= player.x && event.x.toInt() < (player.x + player.bitmap.width)
                        && event.y.toInt() >= player.y && event.y.toInt() < (player.y + player.bitmap.height)) {
                        //tada, if this is true, you've started your click inside your bitmap
                        touched = true
                    }
                }

                MotionEvent.ACTION_UP->{
                        touched = false
                }
            }
        }

        if(touched && !dead){
            event?.let { ev ->
                when(ev.action.and(MotionEvent.ACTION_MASK)){
                    MotionEvent.ACTION_MOVE->{
                        player.x = event.x.toInt() - (player.bitmap.width / 2)
                    }
                }
            }
        }
        return true
    }
}