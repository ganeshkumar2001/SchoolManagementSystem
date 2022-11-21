package com.example.schoolmanagementsystem

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecyclerAdapter: RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {
    private var rollNum= arrayOf(1,2,3,4,5,6,7,8,9,10,11,12)
    private var names= arrayOf("Talanki Ganesh Kumar","T Gowtham","Venkata Pavan","Balaji","Sairam","Ramesh Kumar","ganesh","Vivek","Sagala","Ashok","Abhinav","Teja")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter.ViewHolder {
        val v= LayoutInflater.from(parent.context).inflate(R.layout.student_card_layout,parent,false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: RecyclerAdapter.ViewHolder, position: Int) {
        holder.stud_roll.text= rollNum[position].toString()
        holder.stud_name.text=names[position]
    }

    override fun getItemCount(): Int {
        return names.size
    }

    inner class ViewHolder(itemView:View): RecyclerView.ViewHolder(itemView){
        var stud_roll:TextView
        var stud_name:TextView
        var isPresent:CheckBox
        init {
            stud_roll=itemView.findViewById(R.id.roll)
            stud_name=itemView.findViewById(R.id.name)
            isPresent=itemView.findViewById(R.id.attendCheck)
        }
    }
}