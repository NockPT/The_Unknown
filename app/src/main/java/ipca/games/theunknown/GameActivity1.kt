package ipca.games.theunknown

import android.content.pm.ActivityInfo
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.view.Display
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class GameActivity1 : AppCompatActivity {

    var gameView1 : GameView1? = null

    companion object{
        var highscore : Int? = 0
    }

    constructor() : super()
    {
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("users").child(FirebaseAuth.getInstance().currentUser!!.uid).child("highscore")

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val value = dataSnapshot.getValue(Long::class.java)
                highscore = value?.toInt()
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                highscore = 0
            }
        })

        if(highscore == null)
            highscore = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation =  (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_game)

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

        if(highscore == null)
            highscore = 0

        gameView1 = GameView1(this, size.x, size.y)
        setContentView(gameView1)

    }

    override fun onPause() {
        super.onPause()
        gameView1!!.pause()
    }

    override fun onResume() {
        super.onResume()
        gameView1!!.resume()
    }
}
