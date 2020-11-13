package com.example.homeconsumption

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main_eletricidade.*
import kotlinx.android.synthetic.main.activity_main_residuos.*

class MainResiduos : AppCompatActivity() {
    // Database reference pointing to root of database
    val rootRef: DatabaseReference = FirebaseDatabase.getInstance().reference

    // Database reference pointing to demo node
    val vidroRef: DatabaseReference = rootRef.child("Sensors/Waste/GlassInst")
    val plasticoRef: DatabaseReference = rootRef.child("Sensors/Waste/PlasticInst")
    val papelRef: DatabaseReference = rootRef.child("Sensors/Waste/PaperInst")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_residuos)

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            startActivity(Intent(this, MenuActivity::class.java))
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }
        // Read from the database
        vidroRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val value = dataSnapshot.getValue(Int::class.java)
                VidroAtual.text = value.toString()

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
        plasticoRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val value = dataSnapshot.getValue(Int::class.java)
                plasticoAtual.text = value.toString()

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
        papelRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val value = dataSnapshot.getValue(Int::class.java)
                papelAtual.text = value.toString()

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}