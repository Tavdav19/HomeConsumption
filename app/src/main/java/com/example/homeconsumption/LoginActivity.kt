package com.example.homeconsumption

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import kotlinx.android.synthetic.main.activity_main.*

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance();

        Login.setOnClickListener {
            if (username.text.trim().toString().isNotEmpty() && password.text.trim().toString().isNotEmpty()) {
                SignIn(username.text.trim().toString(), password.text.trim().toString());
            } else {
                Toast.makeText(this, "Invalid Login", Toast.LENGTH_SHORT).show();
            }

        }

    }
        fun SignIn(email:String, password: String){
            auth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this) {task ->
                    if (task.isSuccessful){
                    val intent = Intent(this, MenuActivity::class.java);
                        startActivity(intent);
                    }else{
                        Toast.makeText(this,""+task.exception, Toast.LENGTH_SHORT).show();
                    }
                }
        }

    override fun onStart() {
        super.onStart()
    }
}
