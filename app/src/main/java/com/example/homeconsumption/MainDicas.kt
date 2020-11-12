package com.example.homeconsumption

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main_dicas.*
import kotlinx.android.synthetic.main.activity_main_eletricidade.*


class MainDicas : AppCompatActivity() {
    // Database reference pointing to root of database
    val rootRef: DatabaseReference = FirebaseDatabase.getInstance().reference

    // Database reference pointing to demo node


    private lateinit var  listViewAdapter: ExpandableListViewAdapter
    private  lateinit var  tipoDicas : List<String>
    private lateinit var listaDicas : HashMap<String, List<String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_dicas)

        showList()

        listViewAdapter = ExpandableListViewAdapter(this, tipoDicas, listaDicas)
        eListView.setAdapter(listViewAdapter)



        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            startActivity(Intent(this, MenuActivity::class.java))
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }
        // Read from the database


        findViewById<FloatingActionButton>(R.id.fabemail).setOnClickListener {
            startActivity(packageManager.getLaunchIntentForPackage("com.google.android.gm"));
            }


        }





private fun showList(){
    tipoDicas = ArrayList()
    listaDicas = HashMap()


    (tipoDicas as ArrayList<String>).add("Eletricidade")
    (tipoDicas as ArrayList<String>).add("Agua")
    (tipoDicas as ArrayList<String>).add("Residuos")
    val tipo1 : MutableList<String> = ArrayList()
    val tipo2 : MutableList<String> = ArrayList()
    val tipo3 : MutableList<String> = ArrayList()

    for (dica in 1..3) {
        for (y in 2..6) {
            val listaRef: DatabaseReference = rootRef.child("Dicas/"+dica+"/"+y)
            listaRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val value = dataSnapshot.getValue(String::class.java)
                    if (dica == 1) {
                        tipo1.add(value.toString())
                    } else if (dica == 2) {
                        tipo2.add(value.toString())
                    } else {
                        tipo3.add(value.toString())
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
        }
    }

//    listaDicas[tipoDicas[0]] = tipo1
//    listaDicas[tipoDicas[1]] = tipo2
//    listaDicas[tipoDicas[2]] = tipo3


//    val tipo1 : MutableList<String> = ArrayList()
//    tipo1.add("1 na lista Ele")
//    tipo1.add("2 na lista Ele")
//    tipo1.add("3 na lista Ele")
//
//    val tipo2 : MutableList<String> = ArrayList()
//    tipo2.add("1 na lista Agua")
//    tipo2.add("2 na lista Agua")
//    tipo2.add("3 na lista Agua")
//
//    val tipo3 : MutableList<String> = ArrayList()
//    tipo3.add("1 na lista Res")
//    tipo3.add("2 na lista Res")
//    tipo3.add("3 na lista Res")

    listaDicas[tipoDicas[0]] = tipo1
    listaDicas[tipoDicas[1]] = tipo2
    listaDicas[tipoDicas[2]] = tipo3
}
        }



