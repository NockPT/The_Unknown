package ipca.games.theunknown


import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.drawable.AnimationDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.system.exitProcess


class MainActivity : AppCompatActivity() {

    private lateinit var boss1Animation: AnimationDrawable
    private lateinit var enemyAnimation: AnimationDrawable
    private lateinit var spaceshipAnimation: AnimationDrawable
    private lateinit var shipAnimation: AnimationDrawable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation =  (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main)

        textHighscore.text = getString(R.string.highscore) + "0"
        textHighscore.isVisible = false


        var highscore: Int? = 0
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("users").child(FirebaseAuth.getInstance().currentUser!!.uid).child("highscore")

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val value = dataSnapshot.getValue(Long::class.java)
                highscore = value?.toInt()
                if (highscore == null) {
                    highscore = 0
                    textHighscore.text = getString(R.string.highscore) + highscore.toString()
                    textHighscore.isVisible = true
                }else{
                    textHighscore.text = getString(R.string.highscore) + highscore.toString()
                    textHighscore.isVisible = true
                }
            }
            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                highscore = 0
            }

        })


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
            moveTaskToBack(true)
            android.os.Process.killProcess(android.os.Process.myPid())
            exitProcess(1)
        }

        imageRestart.setOnClickListener {
            val intent = Intent(this@MainActivity, GameActivity1::class.java)
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
