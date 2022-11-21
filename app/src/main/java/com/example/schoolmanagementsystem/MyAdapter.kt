package com.example.schoolmanagementsystem

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.schoolmanagementsystem.databinding.ActivityAttendanceBinding.inflate

class MyAdapter(val mCtx:Context, val list:ArrayList<NotificationData>) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView=LayoutInflater.from(mCtx).inflate(R.layout.notification, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val curr=list[position]
        holder.title.text= curr.titile.toString()
        Log.v("RecyclerView", curr.titile.toString())
        holder.body.text =curr.content.toString()
        holder.date.text =curr.date.toString()
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class MyViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView){
        val title=itemView.findViewById<TextView>(R.id.titleNoti)
        val body=itemView.findViewById<TextView>(R.id.contextNoti)
        val date=itemView.findViewById<TextView>(R.id.date)



    }
}