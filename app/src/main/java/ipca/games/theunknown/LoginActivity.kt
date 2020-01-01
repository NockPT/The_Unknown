package ipca.games.theunknown

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    val TAG = "LoginActivity"

    private lateinit var auth: FirebaseAuth

    lateinit var usernameEditText : EditText
    lateinit var passwordEditText : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        //username = test@test.com
        //password = 123123

        //for debugging purposes
        username.setText("test@test.com")
        password.setText("123123")

        auth = FirebaseAuth.getInstance()

        usernameEditText = findViewById<EditText>(R.id.username)
        passwordEditText = findViewById<EditText>(R.id.password)
        val loginButton = findViewById<Button>(R.id.login)
        val registerButton = findViewById<Button>(R.id.register)

        loginButton.setOnClickListener {
            login(usernameEditText.text.toString(),
                passwordEditText.text.toString())
        }

        registerButton.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }

        val prefs = PreferenceManager.getDefaultSharedPreferences(this@LoginActivity)
        val usernameStored = prefs.getString("username","")
        val passwordStored = prefs.getString("password","")
        val remember = prefs.getBoolean("remember",false)

        if (remember){
            usernameEditText.setText(usernameStored)
            passwordEditText.setText(passwordStored)
            checkBoxRemember.isChecked = true
        }


    }

    fun login(email:String, password:String ){
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information

                    if (checkBoxRemember.isChecked){
                        val prefs = PreferenceManager.getDefaultSharedPreferences(this@LoginActivity)
                        prefs.edit().putString("username", usernameEditText.text.toString()).apply()
                        prefs.edit().putString("password", passwordEditText.text.toString()).apply()
                        prefs.edit().putBoolean("remember", true).apply()
                        prefs.edit().commit()

                    }else {
                        val prefs = PreferenceManager.getDefaultSharedPreferences(this@LoginActivity)
                        prefs.edit().putString("username", "").apply()
                        prefs.edit().putString("password", "").apply()
                        prefs.edit().putBoolean("remember", false).apply()
                        prefs.edit().commit()
                    }

                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)


                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, getString(R.string.authentication_failed),
                        Toast.LENGTH_SHORT).show()

                }

                // ...
            }
    }
}
