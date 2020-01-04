package ipca.games.theunknown

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat


class GameView3 : SurfaceView, Runnable {

    var playing : Boolean = false
    var gameThread : Thread? = null
    var score: Int = 0
    var viewWidth = 0
    var viewHeight = 0

    var spacePlayer : SpacePlayer
    var boss2 : Boss2
    var playerBulletsSpace : MutableList<PlayerBulletSpace> = ArrayList<PlayerBulletSpace>()


    var bossEnemies : MutableList<EnemyBoss> = ArrayList<EnemyBoss>()
    var enemyBulletsBoss : MutableList<EnemyBulletBoss> = ArrayList<EnemyBulletBoss>()

    var stars : MutableList<Star> = ArrayList<Star>()

    var paint : Paint
    var canvas :Canvas
    var surfaceHolder : SurfaceHolder

    var touched : Boolean = false
    var bulletTime : Float
    var bulletTimeBoss : Float

    var dead : Boolean = false

    val colorOrange : Int

    constructor(context: Context? , viewWidth : Int, viewHeight : Int) : super(context){
        spacePlayer = SpacePlayer(context!!, viewWidth, viewHeight)
        boss2 = Boss2(context!!, viewWidth, viewHeight)

        paint = Paint()
        paint.textSize = 50.0f
        canvas = Canvas()
        surfaceHolder = holder
        bulletTime = 3.0f
        bulletTimeBoss = 0.0f


        this.viewWidth = viewWidth
        this.viewHeight = viewHeight


        for(x in 0 until 6){
            bossEnemies.add(EnemyBoss(context,viewWidth , viewHeight,boss2))
        }

        for(x in 0 until 100){
            stars.add(Star(viewWidth , viewHeight))
        }
        val tf = ResourcesCompat.getFont(context, R.font.agencyfb)
        paint.setTypeface(tf)

        colorOrange = ContextCompat.getColor(context, R.color.orange)
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

        for (s in stars) {
            s.Update()
        }

        spacePlayer.update()
        boss2.update()

        for (e in bossEnemies) {
            e.update(boss2)
            for (b in playerBulletsSpace){
                if (e.collissionDetection.intersect(b.collissionDetection)) {
                    b.y = 0 - 100
                    if(e.color == Color.RED){
                        score += 100
                        e.y = viewHeight + 100
                    }
                    if(e.color == Color.GREEN){
                        score += 25
                        e.y = viewHeight + 100
                    }
                    e.color = b.color
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
                if (b.collissionDetection.intersect(boss2.collissionDetection)) {
                    val intent = Intent().setClass(context, YouWinActivity::class.java)
                    intent.putExtra("SCORE", score)
                    (context as Activity).startActivity(intent)
                }
        }

        for (eb in enemyBulletsBoss) {
            eb.update()
            if (eb.collissionDetection.intersect(spacePlayer.collissionDetection)) {
                spacePlayer.x = 1000
                dead = true
            }
        }

        if(dead) {
            for (b in playerBulletsSpace) {
                b.x = -1000
            }
            val intent = Intent().setClass(context, GameOverActivity::class.java)
            intent.putExtra("SCORE", score)
            (context as Activity).startActivity(intent)

        }


        if(score == 3000){
            val intent = Intent().setClass(context, YouWinActivity::class.java)
            intent.putExtra("SCORE", score)
            (context as Activity).startActivity(intent)
        }

        if(bulletTime <= 0.0f){
            playerBulletsSpace.add(PlayerBulletSpace(context!!, viewWidth, viewHeight, spacePlayer))
            for (e in bossEnemies) {
                enemyBulletsBoss.add(EnemyBulletBoss(context!!, viewWidth, viewHeight, e))
            }
            bulletTime = 3.0f
        }

    }
    fun draw() {
        if (surfaceHolder.surface.isValid) {
            canvas = surfaceHolder.lockCanvas()
            canvas.drawColor(Color.BLACK)

            canvas.drawBitmap(spacePlayer.bitmap!!, spacePlayer.x.toFloat(), spacePlayer.y.toFloat(), Paint())

            canvas.drawBitmap(boss2.resized!!, boss2.x.toFloat(), boss2.y.toFloat(), Paint())


            for ( s in stars){
                paint.strokeWidth = s.getStarWidth()
                paint.color = Color.WHITE
                canvas.drawPoint(s.x.toFloat(), s.y.toFloat(), paint)
            }

            for ( e in bossEnemies){
                paint.colorFilter = PorterDuffColorFilter(e.color, PorterDuff.Mode.MULTIPLY)
                canvas.drawBitmap(e.bitmap!!, e.x.toFloat(),e.y.toFloat(), paint)
            }

            for (b in playerBulletsSpace) {
                paint.colorFilter = PorterDuffColorFilter(b.color, PorterDuff.Mode.MULTIPLY)
                canvas.drawBitmap(b.bitmap!!, b.x.toFloat(), b.y.toFloat(), paint)
            }

            for ( eb in enemyBulletsBoss){
                canvas.drawBitmap(eb.bitmap!!,eb.x.toFloat(),eb.y.toFloat(), Paint())
            }

            paint.colorFilter = null
            paint.color = colorOrange
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