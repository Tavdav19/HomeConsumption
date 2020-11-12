package com.example.homeconsumption

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView

class ExpandableListViewAdapter internal constructor(private val context: Context, private val tipoDicas: List<String>, private val listaDicas:HashMap<String, List<String>>): BaseExpandableListAdapter() {
    override fun getGroupCount(): Int {
        return tipoDicas.size
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return this.listaDicas[this.tipoDicas[groupPosition]]!!.size
    }

    override fun getGroup(groupPosition: Int): Any {
        return tipoDicas[groupPosition]
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        return this.listaDicas[this.tipoDicas[groupPosition]]!![childPosition]
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun getGroupView(groupPosition: Int, childPosition: Boolean, convertView: View?, parent: ViewGroup?): View {
        var convertView = convertView
        val tipoDicatitulo = getGroup(groupPosition) as String
        if (convertView == null){
            val inflater= context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = inflater.inflate(R.layout.tipodicas, null)
        }
        val tipodicaTv = convertView!!.findViewById<TextView>(R.id.tipodica_tv)
        tipodicaTv.setText(tipoDicatitulo)

        return convertView
    }

    override fun getChildView(groupPosition: Int, childPosition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup?): View {
        var convertView = convertView
        val listaDicatitulo = getChild(groupPosition, childPosition) as String

        if (convertView == null){
            val inflater= context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = inflater.inflate(R.layout.listadicas, null)
        }
        val listadicaTv = convertView!!.findViewById<TextView>(R.id.listadicas_tv)
        listadicaTv.setText(listaDicatitulo)

        return convertView
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }
}