package com.example.homeconsumption

import android.content.Intent
import android.graphics.Color
import android.graphics.Color.*
import android.os.Bundle
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main_agua.*
import kotlinx.android.synthetic.main.activity_main_eletricidade.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList



class MainEletricidade : AppCompatActivity() {
    // Database reference pointing to root of database
    val rootRef: DatabaseReference = FirebaseDatabase.getInstance().reference

    // Database reference pointing to demo node
    val powerRef: DatabaseReference = rootRef.child("Sensors/PowerInst")
    val entries = ArrayList<BarEntry>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_eletricidade)

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            startActivity(Intent(this, MenuActivity::class.java))
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }
        // Read from the database
        powerRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val value = dataSnapshot.getValue(Int::class.java)
                EletricidadeAtual.text = value.toString()
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val dia: String = sdf.format(Date())
        val hour = SimpleDateFormat("HH")
        val horaString: String = hour.format(Date())
        val horaInt = horaString.toInt()
        var maximo = 0f
        var precoluz =0f
        for (y in 0..23) {
            val luzRef: DatabaseReference = rootRef.child("Sensors/Power/" + dia + "/" + y)
            luzRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    var mediaHora = dataSnapshot.getValue(Float::class.java)
                    if (mediaHora != null) {
                        entries.add(BarEntry(y.toFloat(), mediaHora.toString().toFloat()))
                        maximo = maxOf(maximo, mediaHora)
                    } else (entries.add(BarEntry(y.toFloat(), 0f)))
                    val barDataSet = BarDataSet(entries, "")
                    barDataSet.color = (WHITE)
                    val data = BarData(barDataSet)
                    barChart.data = data
                    barChart.setTouchEnabled(false)
                    barChart.getLegend().setEnabled(false)
                    val description: Description = barChart.getDescription()
                    description.setEnabled(false)
                    val xAxis: XAxis = barChart.getXAxis()
                    xAxis.position = XAxisPosition.BOTTOM
                    xAxis.textSize = 10f
                    xAxis.textColor = Color.WHITE
                    xAxis.setDrawAxisLine(true)
                    xAxis.setDrawGridLines(false)
                    xAxis.setLabelCount(24, false)
                    val leftAxis: YAxis = barChart.getAxisLeft()
                    barChart.getAxisRight().setEnabled(false);
                    leftAxis.setTextSize(10f)
                    leftAxis.setAxisMinimum(0f)
                    leftAxis.setAxisMaximum(maximo)
                    leftAxis.setTextColor(Color.WHITE)
                    leftAxis.setGranularity(1f)
                    leftAxis.setLabelCount(11, true)
                    barDataSet.setDrawValues(false)
                    kWh.setVisibility(VISIBLE)
                    horatext.setVisibility(VISIBLE)
                    barChart.animateY(10)

                    if (horaInt == y) {
                        if (mediaHora != null) {
                            acumuladoEleHora.text = "%.3f".format(mediaHora)
                            textViewAcuHoraEle.text= "Acumulado "+y+"H"
                            precoluz = (mediaHora * 0.1481).toFloat()
                            textViewPrecoLuz.text = "%.2f".format(precoluz)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
        }
            btnHistoricoEle.setOnClickListener {
                startActivity(Intent(this, MensalEletricidade::class.java))
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
    }}
