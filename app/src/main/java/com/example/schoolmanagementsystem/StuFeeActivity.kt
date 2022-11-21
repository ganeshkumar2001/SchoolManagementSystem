package com.example.schoolmanagementsystem

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_stu_fee.*

class StuFeeActivity : AppCompatActivity() {
    lateinit var studatabase:DatabaseReference
    var fee:String=""
    lateinit var classN:String
    lateinit var userid:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stu_fee)
        val intent=getIntent()
        classN= intent.getStringExtra("class").toString()
        userid= intent.getStringExtra("userid").toString()
        studatabase=FirebaseDatabase.getInstance().getReference("Students")
        studatabase.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                fee=snapshot.child(classN).child(userid).child("fees").getValue(Int::class.java).toString()
                balFee.setText("The Balance fee is ${fee}")
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })



    }
}