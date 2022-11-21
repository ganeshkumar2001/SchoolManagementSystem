package com.example.schoolmanagementsystem

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat.getSystemService
import com.google.firebase.database.*
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList

class MarkAttendanceActivity : AppCompatActivity() {
    lateinit var nameC: String
    lateinit var toolbar: androidx.appcompat.widget.Toolbar
    var c1 = 0
    var c2 = 0
    var p=0
    lateinit var selectedItems: MutableList<String?>
    lateinit var notSelectedItems: MutableList<String?>
    lateinit var sButton: Button
    lateinit var sidarrayadapter:MarkAttAdapter
    lateinit var currentDate:String
    lateinit var attDatabaseReference: DatabaseReference
    lateinit var stuDatabaseReference: DatabaseReference
    lateinit var mprogress: ProgressDialog
    lateinit var Userlist: MutableList<String?>
    lateinit var Userids: MutableList<String?>
    lateinit var ch1:ListView

    lateinit var mDialog: ProgressDialog
    var internetsts: Boolean=true

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mark_attendance)
        val intentM = getIntent()
        notSelectedItems= mutableListOf()
        selectedItems= mutableListOf()
        Userids= mutableListOf()
        nameC = intentM.getStringExtra("className").toString()
        sButton = findViewById(R.id.submitatten_btn)
        currentDate = LocalDate.now().toString()
        toolbar = findViewById(R.id.mtoolBar)
        setSupportActionBar(toolbar)
        toolbar.setTitle(nameC + " Attendance")
        Toast.makeText(this,"${nameC}",Toast.LENGTH_SHORT).show()
        Userlist= mutableListOf()
        attDatabaseReference = FirebaseDatabase.getInstance().getReference("Attendance")
        stuDatabaseReference = FirebaseDatabase.getInstance().getReference("Students")
        ch1= findViewById(R.id.mrList)
        ch1.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE)
        sidarrayadapter = MarkAttAdapter(this@MarkAttendanceActivity, R.layout.checkedtestviewlayout, Userlist)
        ch1.setAdapter(sidarrayadapter)
        stuDatabaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (snapshot in snapshot.child(nameC).getChildren()) {
                    val std:StudentData?= snapshot.getValue(StudentData::class.java)
                    Userlist.add(std!!.rollno)
                    Userids.add(std!!.userId)
                    Log.v("STudent Roll", "${std!!.rollno}")
                    c1 = c1 + 1
                    Log.v("STudent count", "${c1}")


                }
                checkboxFunction(Userids)
                sidarrayadapter.notifyDataSetChanged()

            }

            override fun onCancelled(error: DatabaseError) {
            }

        })


        sButton.setOnClickListener {
            internetsts = isNetworkAvailable
            if (internetsts != true) {
                val builder = AlertDialog.Builder(this@MarkAttendanceActivity)
                builder.setMessage("Internet is not available")
                builder.setTitle("Alert !")
                val alertDialog: AlertDialog = builder.create()
                alertDialog.show()
            } else {
                checkAtt()
            }
        }
    }

    private fun checkboxFunction(userlist: MutableList<String?>) {
        notSelectedItems=userlist
        ch1.setOnItemClickListener(object: AdapterView.OnItemClickListener {
            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val checkedItem:String = (p1 as TextView).getText().toString()
                if(selectedItems.contains(userlist[p2])) {
                    selectedItems.remove(userlist[p2])
                }
                else{
                    selectedItems.add(userlist[p2])
                }
            }

        })



    }


    private fun checkAtt() {
        Log.v("checkAtt","I Am  in checkAtt")
        val builder=AlertDialog.Builder(this@MarkAttendanceActivity)
            .setTitle("Confirm")
            .setMessage("Do you want to Submit?")
            .setCancelable(true)
            .setPositiveButton("Yes",object : DialogInterface.OnClickListener {
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    uploadAttendance()
                }

            })
            .setNegativeButton("No", object:DialogInterface.OnClickListener{
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    p0!!.dismiss()
                }
            })
        val ad:AlertDialog=builder.create()
        ad.show()
    }

    private fun uploadAttendance() {
        Log.v("upload","I Am  in upload")
        mDialog=ProgressDialog(this@MarkAttendanceActivity)
        mDialog.setTitle("Please Wait")
        mDialog.setCanceledOnTouchOutside(false)
        mDialog.show()
        for (item in selectedItems) {
            Log.v("item in sel", "${item}")
            notSelectedItems.remove(item)
            if (item != null) {
                Log.v("item",item)
                attDatabaseReference.child("${currentDate}").child(nameC).child(item).setValue("P")
                stuDatabaseReference.addListenerForSingleValueEvent(object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val stu=snapshot.child(nameC).child(item).getValue(StudentData::class.java)
                        var pre=stu!!.present
                        var deli=stu!!.working
                        if (pre != null) {
                            pre=pre+1
                        }
                        if(deli !=null){
                            deli=deli+1
                        }
                        stuDatabaseReference.child(nameC).child(item).child("present").setValue(pre)
                        stuDatabaseReference.child(nameC).child(item).child("working").setValue(deli)

                    }

                    override fun onCancelled(error: DatabaseError) {
                    }

                })
            }
            p=p+1
            c2=c2+1
        }
        for(item in notSelectedItems){
            Log.v("item in notsel", "${item}")
            if (item != null) {
                attDatabaseReference.child("${currentDate}").child(nameC).child(item).setValue("A")
                stuDatabaseReference.addListenerForSingleValueEvent(object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val stu=snapshot.child(nameC).child(item).getValue(StudentData::class.java)
                        var deli=stu!!.working
                        deli= deli!! +1
                        stuDatabaseReference.child(nameC).child(item).child("working").setValue(deli)

                    }

                    override fun onCancelled(error: DatabaseError) {
                    }

                })
            }
            c2=c2+1
        }
        mDialog.dismiss()
        Log.v("cal","${c1} ${c2} ${p}")
        if(c1==c2){
            Log.v("ifCkecked","Hurrayyy!!")
            var ab=c1-p
            val builder=AlertDialog.Builder(this@MarkAttendanceActivity)
            builder.setTitle("Submitted Successfully")
            builder.setCancelable(false)
            builder.setMessage("Present=${p} Absent=${ab} Total ${c1}")
            builder.setNegativeButton("Ok", object :DialogInterface.OnClickListener{
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    p0!!.dismiss()
                    finish()
                }
            })
            val alertDialog:AlertDialog=builder.create()
            alertDialog.show()
        }
        else{
            Log.v("ifChecked","Saddddd")

        }


    }




    private val isNetworkAvailable: Boolean
        private get() {
            val connectivityManager: ConnectivityManager =
                getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo: NetworkInfo? = connectivityManager.getActiveNetworkInfo()
            return activeNetworkInfo != null && activeNetworkInfo.isConnected()
        }
}