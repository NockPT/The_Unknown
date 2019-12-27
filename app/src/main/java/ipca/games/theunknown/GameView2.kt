package ipca.games.theunknown

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView


class GameView2 : SurfaceView, Runnable {

    var playing : Boolean = false
    var gameThread : Thread? = null
    var score: Int = 0
    var viewWidth = 0
    var viewHeight = 0

    var spacePlayer : SpacePlayer
    var playerBulletsSpace : MutableList<PlayerBulletSpace> = ArrayList<PlayerBulletSpace>()


    var enemies : MutableList<Enemy> = ArrayList<Enemy>()
    var enemyBullets : MutableList<EnemyBullet> = ArrayList<EnemyBullet>()

    var stars : MutableList<Star> = ArrayList<Star>()

    var paint : Paint
    var canvas :Canvas
    var surfaceHolder : SurfaceHolder

    var touched : Boolean = false
    var bulletTime : Float
    var dead : Boolean = false

    constructor(context: Context? , viewWidth : Int, viewHeight : Int) : super(context){
        spacePlayer = SpacePlayer(context!!, viewWidth, viewHeight)

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

        for(x in 0 until 100){
            stars.add(Star(viewWidth , viewHeight))

        }

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

        for (s in stars) {
            s.Update()
        }

        spacePlayer.update()
        //playerBullet.update(player)
        for (e in enemies) {
            e.update()
            for (b in playerBulletsSpace){
                if (e.collissionDetection.intersect(b.collissionDetection)) {
                    e.y = viewHeight + 100
                    score += 100
                }
            }

            if (e.collissionDetection.intersect(spacePlayer.collissionDetection)) {
                e.y = viewHeight + 100
                spacePlayer.x = 1000
                dead = true
            }
        }

        for (b in playerBulletsSpace) {
            b.update()
        }

        for (eb in enemyBullets) {
            eb.update()
            if (eb.collissionDetection.intersect(spacePlayer.collissionDetection)) {
                spacePlayer.x = 1000
                dead = true
            }

        }


        if(bulletTime <= 0.0f){
            playerBulletsSpace.add(PlayerBulletSpace(context!!, viewWidth, viewHeight, spacePlayer))

            for (e in enemies) {
                enemyBullets.add(EnemyBullet(context!!, viewWidth, viewHeight, e))
            }

            bulletTime = 3.0f
        }

        if(dead) {
            for (b in playerBulletsSpace) {
                b.x = -1000
            }
            val intent = Intent().setClass(context, GameOverActivity::class.java)
            (context as Activity).startActivity(intent)

        }

        if(score == 1000){
            val intent = Intent().setClass(context, MainActivity::class.java)
            intent.putExtra("SCORE", score)
            (context as Activity).startActivity(intent)
        }

    }
    fun draw() {
        if (surfaceHolder.surface.isValid) {
            canvas = surfaceHolder.lockCanvas()
            canvas.drawColor(Color.BLACK)
            canvas.drawBitmap(spacePlayer.bitmap!!, spacePlayer.x.toFloat(), spacePlayer.y.toFloat(), Paint())

            for ( s in stars){
                paint.strokeWidth = s.getStarWidth()
                canvas.drawPoint(s.x.toFloat(), s.y.toFloat(), paint)
            }

            for ( e in enemies){
                canvas.drawBitmap(e.bitmap!!, e.x.toFloat(),e.y.toFloat(), Paint())
            }

            for (b in playerBulletsSpace) {
                canvas.drawBitmap(b.bitmap!!, b.x.toFloat(), b.y.toFloat(), Paint())
            }

            for ( eb in enemyBullets){
                canvas.drawBitmap(eb.bitmap!!,eb.x.toFloat(),eb.y.toFloat(), Paint())
            }

            paint.color = Color.WHITE
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
                    if (event.x.toInt() >= spacePlayer.x && event.x.toInt() < (spacePlayer.x + spacePlayer.bitmap.width)
                        && event.y.toInt() >= spacePlayer.y && event.y.toInt() < (spacePlayer.y + spacePlayer.bitmap.height)) {
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
                            spacePlayer.x = event.x.toInt() - (spacePlayer.bitmap.width / 2)
                        }
                    }
                }
        }

        return true
    }
}