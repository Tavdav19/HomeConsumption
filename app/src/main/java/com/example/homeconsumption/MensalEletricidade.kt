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
import kotlinx.android.synthetic.main.activity_main_agua.*
import kotlinx.android.synthetic.main.activity_main_eletricidade.*
import kotlinx.android.synthetic.main.activity_mensal_agua.*
import kotlinx.android.synthetic.main.activity_mensal_eletricidade.*
import java.text.SimpleDateFormat
import java.util.*

class MensalEletricidade : AppCompatActivity() {

    val rootRef: DatabaseReference = FirebaseDatabase.getInstance().reference
    val entries = ArrayList<BarEntry>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mensal_eletricidade)



        findViewById<FloatingActionButton>(R.id.fabMensal).setOnClickListener {
            startActivity(Intent(this, MainEletricidade::class.java))
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
            val luzMesRef: DatabaseReference =
                rootRef.child("Consumption/Power/Day/" + ano + "/" + mes + "/" + y)
            luzMesRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    var somahoras = dataSnapshot.getValue(Float::class.java)

                    if (somahoras != null) {
                        entries.add(BarEntry(y.toFloat(), somahoras.toString().toFloat()))
                        maximo = maxOf(maximo, somahoras)
                    } else (entries.add(BarEntry(y.toFloat(), 0f)))

                    val barDataSet = BarDataSet(entries, "")

                    barDataSet.color = (Color.WHITE)


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
                    xAxis.setLabelCount(31, false)

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
                        if (somahoras != null) {
                            eletricidadeAcumulado.text = "%.3f".format(somahoras)
                            precoElemes = (somahoras * 0.1481).toFloat()
                            textViewPrecoEleMensal.text = "%.2f".format(precoElemes)
                        }
                    }

                }   override fun onCancelled(error: DatabaseError) {
                }
        })
        }
    }
}