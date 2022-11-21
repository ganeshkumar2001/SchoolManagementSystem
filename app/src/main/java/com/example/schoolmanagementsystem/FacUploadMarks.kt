package com.example.schoolmanagementsystem

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.example.schoolmanagementsystem.databinding.ActivityFacUploadMarksBinding
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_fac_upload_marks.*

class FacUploadMarks : AppCompatActivity() {
    lateinit var spinner: Spinner
    lateinit var slist:MutableList<String>
    lateinit var sids:MutableList<String>
    lateinit var databaseReference: DatabaseReference
    lateinit var studatabase:DatabaseReference
    private var nameC: String=""
    private var rollno:String=""
    private var uid:String=""
    lateinit var mProcess:ProgressDialog
    lateinit var binding: ActivityFacUploadMarksBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityFacUploadMarksBinding.inflate(layoutInflater)
        setContentView(binding.root)
        databaseReference=FirebaseDatabase.getInstance().getReference("Marks")
        studatabase=FirebaseDatabase.getInstance().getReference("Students")
        val intent=getIntent()
        slist= mutableListOf()
        sids= mutableListOf()
        mProcess=ProgressDialog(this)
        mProcess.setTitle("Loading")
        mProcess.setMessage("Please wait...")
        mProcess.show()
        nameC=intent.getStringExtra("className").toString()
        spinner=findViewById(R.id.rollNo)
        val arrayAdapter=ArrayAdapter<String>(this@FacUploadMarks,android.R.layout.simple_spinner_item,slist)
        spinner.adapter=arrayAdapter
        studatabase.child(nameC).addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                mProcess.hide()
                slist.clear()
                sids.clear()

                for (item:DataSnapshot in snapshot.children){
                    slist.add(item.child("rollno").getValue(String::class.java).toString())
                    val userid=item.key
                    sids.add(userid.toString())
                    Log.v("firebase um", userid.toString())
                }
                arrayAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })

        spinner.onItemSelectedListener=object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                uid=sids[p2]
                Log.v("userid",uid)
                rollno=slist[p2]
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                Toast.makeText(this@FacUploadMarks,"Nothing Selected", Toast.LENGTH_SHORT).show()
            }

        }

        umButton.setOnClickListener{
            uploadmarks()
        }

    }

    private fun uploadmarks() {

        val check=validateform()
        if(check==true){
            val m1=l1m.text.toString().toInt()
            val m2=l2m.text.toString().toInt()
            val m3=l3m.text.toString().toInt()
            val m4=l4m.text.toString().toInt()
            val m5=l5m.text.toString().toInt()
            val m6=l6m.text.toString().toInt()
            val marks=MarksData(m1,m2,m3,m4,m5,m6)
            databaseReference.addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.hasChild(uid)){
                        alreadymarked()
                        return
                    }
                    else {
                        databaseReference.child(uid).setValue(marks)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })

        }

    }

    private fun alreadymarked() {
        val dialogg=AlertDialog.Builder(this@FacUploadMarks)
        dialogg.setTitle("Warning")
        dialogg.setMessage("The marks of this student is already marked.")
        dialogg.setCancelable(false)
        dialogg.setNegativeButton("Ok", object : DialogInterface.OnClickListener{
            override fun onClick(p0: DialogInterface?, p1: Int) {
                p0!!.dismiss()
            }
        })
        val ad:AlertDialog=dialogg.create()
        ad.show()
    }

    private fun dialog() {
        val dialogg=AlertDialog.Builder(this@FacUploadMarks)
        dialogg.setTitle("Confirmation")
        dialogg.setMessage("Are you want to submit?")
        dialogg.setCancelable(true)
        dialogg.setNegativeButton("No", object : DialogInterface.OnClickListener{
            override fun onClick(p0: DialogInterface?, p1: Int) {
                p0!!.dismiss()
            }
        })
        dialogg.setPositiveButton("Yes", object : DialogInterface.OnClickListener{
            override fun onClick(p0: DialogInterface?, p1: Int) {
                uploadmarks()
            }
        })
        val ad:AlertDialog=dialogg.create()
        ad.show()
    }

    private fun validateform():Boolean {
        if(l1m.text.toString().isEmpty() || l2m.text.toString().isEmpty() || l3m.text.toString().isEmpty() || l4m.text.toString().isEmpty() || l5m.text.toString().isEmpty() || l6m.text.toString().isEmpty()  ){
            Toast.makeText(this,"Enter All Marks", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }


}