package com.example.schoolmanagementsystem

import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.example.schoolmanagementsystem.databinding.ActivityFacFeeBinding
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import kotlinx.android.synthetic.main.activity_fac_fee.*

class FacFeeActivity : AppCompatActivity() {
    lateinit var spinner: Spinner
    lateinit var studatabase:DatabaseReference
    lateinit var feedatabse:DatabaseReference
    lateinit var ulist:MutableList<String>
    lateinit var feelist:MutableList<Int?>
    lateinit var uids:MutableList<String>
    private var rollnos:String=""
    private var uid:String=""
    private var nameC:String=""
    private var feesP=0
    lateinit var binding: ActivityFacFeeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityFacFeeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        studatabase=FirebaseDatabase.getInstance().getReference("Students")
        feedatabse=FirebaseDatabase.getInstance().getReference("Fees")
        ulist= mutableListOf()
        uids= mutableListOf()
        feelist= mutableListOf()
        val intent=getIntent()
        spinner=findViewById(R.id.rollSpinner)
        nameC=intent.getStringExtra("className").toString()
        val arrayAdapter=
            ArrayAdapter<String>(this@FacFeeActivity,android.R.layout.simple_spinner_item,ulist)
        spinner.adapter=arrayAdapter
        studatabase.child(nameC).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                ulist.clear()
                uids.clear()
                for (item: DataSnapshot in snapshot.children){
                    ulist.add(item.child("rollno").getValue(String::class.java).toString())
                    feelist.add(item.child("fees").getValue(Int::class.java))
                    val userid=item.key
                    uids.add(userid.toString())
                    Log.v("firebase um", userid.toString())
                }
                arrayAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })

        spinner.onItemSelectedListener=object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                uid=uids[p2]
                Log.v("userid",uid)
                rollnos=ulist[p2]
                feesP= feelist[p2]!!
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

        }

        sButton.setOnClickListener{
            feedatails()
        }

    }

    private fun feedatails() {
        val testfee=amountU.text.toString()
        var payedfee=0
        var fee:Int?=0
        if(testfee.isNotEmpty()) {
            payedfee = amountU.text.toString().toInt()
            feedatabse.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    feedatabse.child(uid).push().setValue(payedfee)
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })

            studatabase.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    fee = snapshot.child(nameC).child(uid).child("fees").getValue(Int::class.java)
                    fee = fee?.minus(payedfee)
                    studatabase.child(nameC).child(uid).child("fees").setValue(fee)
                    ufee.text="The current pending fee is ${fee}"
                    pfee.text=" "

                }

                override fun onCancelled(error: DatabaseError) {
                }
            })


        }
        else{
            var justfee:Int?=0
            studatabase.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    justfee = snapshot.child(nameC).child(uid).child("fees").getValue(Int::class.java)
                    pfee.text="The pending fee is: ${justfee}"

                }

                override fun onCancelled(error: DatabaseError) {
                }
            })

        }


    }


}