package com.example.homeconsumption


import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_menu.*


class MenuActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        auth = FirebaseAuth.getInstance();
        Logout.setOnClickListener {
            auth.signOut();
            val intent = Intent(this, LoginActivity::class.java);
            startActivity(intent);
        }
        btnEletricidade.setOnClickListener {
            startActivity(Intent(this, MainEletricidade::class.java))
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
        btnAgua.setOnClickListener {
            startActivity(Intent(this, MainAgua::class.java))
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
        btnResiduos.setOnClickListener {
            startActivity(Intent(this, MainResiduos::class.java))
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
        btnDicas.setOnClickListener {
            startActivity(Intent(this, MainDicas::class.java))
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
        }
    }
