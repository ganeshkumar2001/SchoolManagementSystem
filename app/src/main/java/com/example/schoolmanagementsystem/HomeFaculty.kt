package com.example.schoolmanagementsystem

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.ImageButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.drawerlayout.widget.DrawerLayout
import com.example.schoolmanagementsystem.classR.ClassActivity
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_home_faculty.*

class HomeFaculty : AppCompatActivity() {

    lateinit var database:DatabaseReference
    lateinit var mprogress:ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_faculty)
        val markAttendance=findViewById<CardView>(R.id.markAtt)
        val navLayout=findViewById<DrawerLayout>(R.id.navLayout)
        val navbarView=findViewById<NavigationView>(R.id.navView)
        val headerView=navbarView.getHeaderView(0)
        val navName:TextView=headerView.findViewById(R.id.navName)
        val navButton=findViewById<ImageButton>(R.id.facMenu)
        val dashName=findViewById<TextView>(R.id.nameDash)
        val uploadMarks=findViewById<CardView>(R.id.uploadMarks)

        mprogress= ProgressDialog(this)
        mprogress.setTitle("Loading")
        mprogress.setMessage("Please wait... ")
        mprogress.show()


        val intent=getIntent()
        val userid=intent.getStringExtra("userid").toString()
        database=FirebaseDatabase.getInstance().getReference("Faculty")
        database.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val fac:FacultyData?=snapshot.child(userid).getValue(FacultyData::class.java)
                navName.text=fac!!.name
                dashName.text=fac!!.name
                mprogress.dismiss()
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
        markAttendance.setOnClickListener {
            val intentM=Intent(this, ClassActivity::class.java)
            intentM.putExtra("from","Mark Attendance")
            startActivity(intentM)

        }
        uploadMarks.setOnClickListener{
            val intentUM=Intent(this, ClassActivity::class.java)
            intentUM.putExtra("from","Upload Marks")
            startActivity(intentUM)
        }
        complaint.setOnClickListener{
            val intentF=Intent(this, ClassActivity::class.java)
            intentF.putExtra("from", "Fee Update")
            startActivity(intentF)
        }
        notification.setOnClickListener{
            val intentN=Intent(this, MessageFacActivity::class.java)
            startActivity(intentN)
        }
        navButton.setOnClickListener(){
            navLayout.openDrawer(Gravity.LEFT)
        }

        navbarView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.homeN->{
                    val intenth=Intent(this,HomeFaculty::class.java)
                    startActivity(intenth)

                }
                R.id.uploadN-> {
                    val intentUM=Intent(this, ClassActivity::class.java)
                    intentUM.putExtra("from","Upload Marks")
                    startActivity(intentUM)
                }
                R.id.marAttendanceN->{
                val intentM=Intent(this, ClassActivity::class.java)
                intentM.putExtra("from","Mark Attendance")
                startActivity(intentM)
                }
                R.id.feeUpdate-> {
                    val intentF=Intent(this, ClassActivity::class.java)
                    intentF.putExtra("from", "Fee Update")
                    startActivity(intentF)
                }

            }
            true
        }


    }
}