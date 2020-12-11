package com.example.homeconsumption

import android.app.Notification.DEFAULT_SOUND
import android.app.Notification.DEFAULT_VIBRATE
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Color.*
import android.os.Build
import android.os.Bundle
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.google.android.material.floatingactionbutton.FloatingActionButton
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

    private val CHANNEL_ID = "channel_id"
    private val notificationId = 101
    private var sent=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_eletricidade)
        createNotificationChannel()
        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            startActivity(Intent(this, MenuActivity::class.java))
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }

        // Read from the database
        powerRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val value = dataSnapshot.getValue(Int::class.java)
                EletricidadeAtual.text = value.toString()
                if (value!! >= 500 && sent == 0) {
                    sendNotification()
                    sent = 1
                }

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
                    xAxis.setLabelCount(12, false)
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
                            textViewAcuHoraEle.text = "Acumulado " + y + "H"

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


    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val id = "example"
            val name = "Home Consumption"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(id, name, importance).apply {
                description ="Consumo excessivo de eletricidade." +
                        " Veja algumas recomendações na nossa zona de dicas da aplicação."
            }
            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
    private fun sendNotification(){
        val notifyIntent = Intent(this, MainDicas::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val notifyPendingIntent = PendingIntent.getActivity(
            this, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT

        )

        val builder = NotificationCompat.Builder(this, "example")
            .setSmallIcon(R.drawable.notif)
            .setContentTitle("Consumo Excessivo de Eletricidade")
            .setStyle(NotificationCompat.BigTextStyle())
            .setContentText("Veja algumas recomendações na nossa zona de dicas.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(notifyPendingIntent)
            .setAutoCancel(true);

        with(NotificationManagerCompat.from(this)){
            notify(101, builder.build())

        }
    }

}
