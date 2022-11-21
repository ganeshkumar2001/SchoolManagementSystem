package com.example.schoolmanagementsystem

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import com.example.schoolmanagementsystem.databinding.ActivityStuAttendanceBinding
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_attendance.*
import kotlinx.android.synthetic.main.activity_home_student.*
import kotlinx.android.synthetic.main.activity_stu_attendance.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlin.math.floor

class StuAttendance : AppCompatActivity() {
    lateinit var nameC:String
    lateinit var userid:String
    lateinit var rollno:String
    private var present:Int=0
    private  var working:Int=0
    lateinit var binding: ActivityStuAttendanceBinding
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityStuAttendanceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        headingAB.setText("Attendance")
        backButtonAB.setOnClickListener{
            val intentB=Intent(this,HomeStudent::class.java)
            startActivity(intentB)
        }
        val intent=getIntent()
        userid=intent.getStringExtra("userid").toString()
        nameC=intent.getStringExtra("class").toString()
        rollno=intent.getStringExtra("rollno").toString()
        present=intent.getIntExtra("present",0)
        working=intent.getIntExtra("working",0)

        var per: Int =present.divideToPercent(working)
        Log.v("att percentage", per.toString())

        attpercentage.text=per.toString()+"%"
        attPB.setProgress(per,true)
        attfrac.text=present.toString()
        work.text=working.toString()



    }

    fun Int.divideToPercent(divideTo: Int): Int {
        return if (divideTo == 0) 0
        else ((this / divideTo.toFloat())*100).toInt()
    }
}

