package com.example.schoolmanagementsystem

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.attendanceview.view.*

class StuAttenAdapter: RecyclerView.Adapter<StuAttenAdapter.myViewAttendance>(){

    inner class myViewAttendance(itemView: View):RecyclerView.ViewHolder(itemView){
        var subname=itemView.subname
        var per=itemView.attpercentage
        var facn=itemView.facName
        var attf=itemView.attfrac

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewAttendance {
        var view=LayoutInflater.from(parent.context).inflate(R.layout.attendanceview,parent,false)
        return myViewAttendance(view)
    }

    override fun onBindViewHolder(holder: myViewAttendance, position: Int) {
    }

    override fun getItemCount(): Int {
        return 0
    }
}