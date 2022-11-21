package com.example.schoolmanagementsystem.classR

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.schoolmanagementsystem.R

class classAdapter(val mCtx:Context, val r:Int,val items:MutableList<String?>):ArrayAdapter<String?>(mCtx,
    R.layout.classlayout,items){
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater=LayoutInflater.from(mCtx)
        val view=inflater.inflate(R.layout.classlayout, null)
        val text1=view.findViewById<TextView>(R.id.classText)
        text1.text=items[position].toString()
        return view
    }

}