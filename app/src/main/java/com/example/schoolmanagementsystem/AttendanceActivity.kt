package com.example.schoolmanagementsystem

import android.os.BugreportManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class AttendanceActivity : AppCompatActivity() {
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var adapter: RecyclerView.Adapter<RecyclerAdapter.ViewHolder>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_attendance)

        layoutManager=LinearLayoutManager(this)
        val recyclelayout=findViewById<RecyclerView>(R.id.recyclelayout)
        recyclelayout.layoutManager=layoutManager
        adapter=RecyclerAdapter()
        recyclelayout.adapter=adapter

    }
}