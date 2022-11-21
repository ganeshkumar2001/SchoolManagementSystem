package com.example.schoolmanagementsystem

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckedTextView
import android.widget.TextView

class MarkAttAdapter(val mCtx: Context, val r:Int, val items:MutableList<String?>): ArrayAdapter<String?>(mCtx,
R.layout.checkedtestviewlayout,items){
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater= LayoutInflater.from(mCtx)
        val view=inflater.inflate(R.layout.checkedtestviewlayout, null)
        val text1=view.findViewById<CheckedTextView>(R.id.checkboxt)
        text1.text=items[position].toString()
        return view
    }

}