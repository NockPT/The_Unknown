package ipca.games.theunknown

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.system.exitProcess

class GameOverActivity : AppCompatActivity() {

    private lateinit var boss1Animation: AnimationDrawable
    private lateinit var enemyAnimation: AnimationDrawable
    private lateinit var spaceshipAnimation: AnimationDrawable
    private lateinit var shipAnimation: AnimationDrawable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_over)

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

        findViewById<ImageView>(R.id.boss_1_anim).apply {
            setBackgroundResource(R.drawable.boss_1_anim)
            boss1Animation = background as AnimationDrawable
        }

        findViewById<ImageView>(R.id.enemy_anim).apply {
            setBackgroundResource(R.drawable.enemy_anim)
            enemyAnimation = background as AnimationDrawable
        }

        findViewById<ImageView>(R.id.spaceship_anim).apply {
            setBackgroundResource(R.drawable.spaceship_anim)
            spaceshipAnimation = background as AnimationDrawable
        }

        findViewById<ImageView>(R.id.ship_anim).apply {
            setBackgroundResource(R.drawable.ship_anim)
            shipAnimation = background as AnimationDrawable
        }

        imageExit.setOnClickListener {
            exitProcess(0)
        }

        imageRestart.setOnClickListener {
            val intent = Intent(this@GameOverActivity, GameActivity1::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        boss1Animation.start()
        enemyAnimation.start()
        spaceshipAnimation.start()
        shipAnimation.start()
    }

}
