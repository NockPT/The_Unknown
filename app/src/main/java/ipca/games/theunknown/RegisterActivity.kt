package ipca.games.theunknown

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class RegisterActivity : AppCompatActivity() {

    val TAG = "RegisterActivity"

    private lateinit var auth: FirebaseAuth

    lateinit var usernameEditText : EditText
    lateinit var passwordEditText : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()

        usernameEditText = findViewById<EditText>(R.id.username)
        passwordEditText = findViewById<EditText>(R.id.password)
        val registerButton = findViewById<Button>(R.id.end_register)


        registerButton.setOnClickListener {
            register(usernameEditText.text.toString(),
                passwordEditText.text.toString())
        }
    }

    fun register(email:String, password:String ) {

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this
            ) { task ->
                if (task.isSuccessful) { // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    val user: FirebaseUser = auth.getCurrentUser()!!

                    Toast.makeText(
                        this@RegisterActivity, "User created.",
                        Toast.LENGTH_SHORT
                    ).show()

                    val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                    startActivity(intent)

                } else { // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        this@RegisterActivity, "Email already used or password too short (must be >6 characters)",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                // ...
            }

    }
}
