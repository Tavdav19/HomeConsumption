package com.example.homeconsumption

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_mensal_eletricidade.barChartLuzMensal
import kotlinx.android.synthetic.main.activity_mensal_eletricidade.diatextMensal
import kotlinx.android.synthetic.main.activity_mensal_eletricidade.kWhMensal
import kotlinx.android.synthetic.main.activity_mensal_eletricidade.vidroAcumulado
import kotlinx.android.synthetic.main.activity_mensal_residuos.*
import java.text.SimpleDateFormat
import java.util.*

class MensalResiduos : AppCompatActivity() {

    val rootRef: DatabaseReference = FirebaseDatabase.getInstance().reference
    val entries = ArrayList<BarEntry>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mensal_residuos)



        findViewById<FloatingActionButton>(R.id.fabMensal).setOnClickListener {
            startActivity(Intent(this, MainResiduos::class.java))
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }

        val year = SimpleDateFormat("yyyy")
        val month = SimpleDateFormat("MM")
        val day = SimpleDateFormat("dd")

        val ano: String = year.format(Date())
        val mes: String = month.format(Date())
        val dia: String = day.format(Date())
        val diaInt= dia.toInt();
        var precoElemes =0f
        var maximo = 0f
        for (y in 1..31) {
            var papelsomahoras = 0f
            val papelMesRef: DatabaseReference=
                rootRef.child("Consumption/Waste/Paper/Day/" + ano + "/" + mes + "/" + y)
            papelMesRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.getValue(Float::class.java)== null){
                        papelsomahoras=0f
                    }else{
                        papelsomahoras = dataSnapshot.getValue(Float::class.java)!!
                    if (diaInt == y) {
                            cartaoAcumulado.text = "%.1f".format(papelsomahoras)
                    }}
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })
            var plasticosomahoras = 0f
            val plasticoMesRef: DatabaseReference=
                rootRef.child("Consumption/Waste/Plastic/Day/" + ano + "/" + mes + "/" + y)
            plasticoMesRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.getValue(Float::class.java)== null){
                        plasticosomahoras=0f
                    }else{
                        plasticosomahoras = dataSnapshot.getValue(Float::class.java)!!
                    }
                    if (diaInt == y) {
                        if (plasticosomahoras != null) {
                            plasticoAcumulado.text = "%.1f".format(plasticosomahoras)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })
            val vidroMesRef: DatabaseReference =
                rootRef.child("Consumption/Waste/Glass/Day/" + ano + "/" + mes + "/" + y)
            vidroMesRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    var vidrosomahoras = dataSnapshot.getValue(Float::class.java)

                    if (vidrosomahoras != null) {
                        entries.add(BarEntry(y.toFloat(), floatArrayOf(vidrosomahoras,plasticosomahoras,papelsomahoras)))

                        maximo = maxOf(maximo, vidrosomahoras+plasticosomahoras+papelsomahoras)
                    } else (entries.add(BarEntry(y.toFloat(), 0f)))

                    val barDataSet = BarDataSet(entries, "")

                    barDataSet.setColors(Color.GREEN,Color.YELLOW,Color.BLUE)


                    val data = BarData(barDataSet)
                    barChartLuzMensal.data = data
                    barChartLuzMensal.setTouchEnabled(false)
                    barChartLuzMensal.getLegend().setEnabled(false)


                    val description: Description = barChartLuzMensal.getDescription()
                    description.setEnabled(false)

                    val xAxis: XAxis = barChartLuzMensal.getXAxis()
                    xAxis.position = XAxis.XAxisPosition.BOTTOM
                    xAxis.textSize = 10f
                    xAxis.textColor = Color.WHITE
                    xAxis.setDrawAxisLine(true)
                    xAxis.setDrawGridLines(false)
                    xAxis.setLabelCount(16, false)

                    val leftAxis: YAxis = barChartLuzMensal.getAxisLeft()
                    barChartLuzMensal.getAxisRight().setEnabled(false);

                    leftAxis.setTextSize(10f); // set the text size
                    leftAxis.setAxisMinimum(0f); // start at zero

                    leftAxis.setAxisMaximum(maximo); // the axis maximum is 100
                    leftAxis.setTextColor(Color.WHITE);
                    leftAxis.setGranularity(1f); // interval 1
                    leftAxis.setLabelCount(11, true); // force 6 labels


                    barDataSet.setDrawValues(false)
                    kWhMensal.setVisibility(View.VISIBLE)
                    diatextMensal.setVisibility(View.VISIBLE)
                    barChartLuzMensal.animateY(10)

                    if (diaInt == y) {
                        if (vidrosomahoras != null) {
                            vidroAcumulado.text = "%.1f".format(vidrosomahoras)

                        }
                    }

                }   override fun onCancelled(error: DatabaseError) {
                }
            })
        }

    }
}