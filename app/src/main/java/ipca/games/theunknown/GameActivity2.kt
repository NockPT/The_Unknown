package ipca.games.theunknown

import android.content.pm.ActivityInfo
import android.graphics.Point
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Display
import android.view.View

class GameActivity2 : AppCompatActivity() {

    var gameView2 : GameView2? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation =  (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_game2)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                    // Set the content to appear under the system bars so that the
                    // content doesn't resize when the system bars hide and show.
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    // Hide the nav bar and status bar
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN)
        }

        val display : Display = windowManager.defaultDisplay
        var size : Point = Point()
        display.getSize(size)

        gameView2 = GameView2(this, size.x, size.y)
        gameView2!!.score = getIntent().getIntExtra("SCORE",0)
        setContentView(gameView2)

    }

    override fun onPause() {
        super.onPause()
        gameView2!!.pause()
    }

    override fun onResume() {
        super.onResume()
        gameView2!!.resume()
    }

}
