package com.example.homeconsumption

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main_dicas.*


class MainDicas : AppCompatActivity() {
    // Database reference pointing to root of database
    val rootRef: DatabaseReference = FirebaseDatabase.getInstance().reference

    // Database reference pointing to demo node
    val dicasRef: DatabaseReference = rootRef.child("Sensors/Dicas")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_dicas)

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            startActivity(Intent(this, MenuActivity::class.java))
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }
        // Read from the database
        dicasRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val value = dataSnapshot.getValue(Int::class.java)
                Dica1.text = value.toString()

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        findViewById<FloatingActionButton>(R.id.fabemail).setOnClickListener {
            startActivity(packageManager.getLaunchIntentForPackage("com.google.android.gm"));
            }


        }
        }



