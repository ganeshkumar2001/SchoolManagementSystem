package com.example.schoolmanagementsystem

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.schoolmanagementsystem.databinding.ActivityStuMarksBinding
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_stu_marks.*

class StuMarks : AppCompatActivity() {
    lateinit var binding: ActivityStuMarksBinding
    private var userid=""
    lateinit var databaseReference: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityStuMarksBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val intent=getIntent()
        userid=intent.getStringExtra("userid").toString()

        databaseReference=FirebaseDatabase.getInstance().getReference("Marks")

        databaseReference.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val mar=snapshot.child(userid).getValue(MarksData::class.java)
                l1m.text=mar!!.m1.toString()
                l2m.text=mar!!.m2.toString()
                l3m.text=mar!!.m3.toString()
                l4m.text=mar!!.m4.toString()
                l5m.text=mar!!.m5.toString()
                l6m.text=mar!!.m6.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.v("errorinfire","Yusshhhhhhh")
            }

        })





    }
}