package com.example.homeconsumption

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main_agua.*
import java.text.SimpleDateFormat
import java.util.*


class MainAgua : AppCompatActivity() {
    // Database reference pointing to root of database
    val rootRef: DatabaseReference = FirebaseDatabase.getInstance().reference

    // Database reference pointing to demo node
  val waterRef: DatabaseReference = rootRef.child("Sensors/WaterInst")

    val entries = ArrayList<BarEntry>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_agua)

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            startActivity(Intent(this, MenuActivity::class.java))
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }
        // Read from the database
            waterRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val value = dataSnapshot.getValue(Int::class.java)
                    VidroAtual.text = value.toString()
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
        var precoagua = 0f
        for (y in 0..23) {
            val aguaRef: DatabaseReference = rootRef.child("Sensors/Water/" + dia + "/" + y)
            aguaRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    var valorHora = dataSnapshot.getValue(Float::class.java)

                    if (valorHora != null) {
                        entries.add(BarEntry(y.toFloat(), valorHora.toString().toFloat()))
                        maximo = maxOf(maximo, valorHora)
                    } else (entries.add(BarEntry(y.toFloat(), 0f)))

                    val barDataSet = BarDataSet(entries, "")

                    barDataSet.color = (Color.WHITE)


                    val data = BarData(barDataSet)
                    barChartlixo.data = data
                    barChartlixo.setTouchEnabled(false)
                    barChartlixo.getLegend().setEnabled(false)


                    val description: Description = barChartlixo.getDescription()
                    description.setEnabled(false)

                    val xAxis: XAxis = barChartlixo.getXAxis()
                    xAxis.position = XAxis.XAxisPosition.BOTTOM
                    xAxis.textSize = 10f
                    xAxis.textColor = Color.WHITE
                    xAxis.setDrawAxisLine(true)
                    xAxis.setDrawGridLines(false)
                    xAxis.setLabelCount(12, false)

                    val leftAxis: YAxis = barChartlixo.getAxisLeft()
                    barChartlixo.getAxisRight().setEnabled(false);

                    leftAxis.setTextSize(10f); // set the text size
                    leftAxis.setAxisMinimum(0f); // start at zero

                    leftAxis.setAxisMaximum(maximo); // the axis maximum is 100
                    leftAxis.setTextColor(Color.WHITE);
                    leftAxis.setGranularity(1f); // interval 1
                    leftAxis.setLabelCount(11, true); // force 6 labels


                    barDataSet.setDrawValues(false)
                    barChartlixo.animateY(10)


                    horatextView.setVisibility(View.VISIBLE)
                    textViewLitro.setVisibility(View.VISIBLE)

                    if (horaInt == y) {
                        if (valorHora != null) {
                            AcumuladoAguaHora.text = "%.3f".format(valorHora)
                            textViewAcuHora.text= "Acumulado "+y+"H"
                            precoagua = (valorHora * 0.001).toFloat()
                            textViewPrecoAgua.text = "%.2f".format(precoagua)
                        }
                    }

//                if(valorHora >10){
//                    AguaAtual.text = "%.1f".format(valorHora)
//                }else{
//                    AguaAtual.text = "%.3f".format(valorHora)
//                }
//
//                if(valorHora <1){
//                    textViewLitroInst.text="ml"
//                    AguaAtual.text = "%.0f".format(valorHora * 1000)
//                }else{
//                    textViewLitroInst.text="Litro"
//                }


                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
            btnMensalResiduos.setOnClickListener {
                startActivity(Intent(this, MensalAgua::class.java))
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
        }
    }}