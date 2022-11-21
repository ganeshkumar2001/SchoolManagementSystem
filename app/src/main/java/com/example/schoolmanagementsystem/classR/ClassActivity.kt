package com.example.schoolmanagementsystem.classR

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import com.example.schoolmanagementsystem.FacFeeActivity
import com.example.schoolmanagementsystem.FacUploadMarks
import com.example.schoolmanagementsystem.MarkAttendanceActivity
import com.example.schoolmanagementsystem.R
import com.google.firebase.database.*

class ClassActivity : AppCompatActivity() {
    private lateinit var listview: ListView
    private lateinit var cdatabase: DatabaseReference
    private lateinit var arrayList: MutableList<String?>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_class)
        listview = findViewById(R.id.listClass)
        arrayList = mutableListOf()
        val intent=getIntent()
        val from=intent.getStringExtra("from")
        val aadapter = classAdapter(this, R.layout.classlayout, arrayList)
        listview.adapter = aadapter
        cdatabase = FirebaseDatabase.getInstance().getReference("Students")
        cdatabase.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val cstring = snapshot.key
                Log.v("added into list", "${cstring}")
                arrayList.add(cstring)
                aadapter.notifyDataSetChanged()
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                aadapter.notifyDataSetChanged()
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
        if(from.equals("Mark Attendance")) {
            listview.setOnItemClickListener { adapterView, view, i, l ->
                val selectedc = adapterView.getItemAtPosition(i) as String
                Log.v("sellected class", selectedc)
                val intentMA = Intent(this, MarkAttendanceActivity::class.java)
                intentMA.putExtra("className", selectedc)
                startActivity(intentMA)
            }
        }
         else if(from.equals("Upload Marks")){
            listview.setOnItemClickListener { adapterView, view, i, l ->
                val selectedc = adapterView.getItemAtPosition(i) as String
                Log.v("sellected class", selectedc)
                val intentUML = Intent(this, FacUploadMarks::class.java)
                intentUML.putExtra("className", selectedc)
                startActivity(intentUML)
            }

        }
        else{
            listview.setOnItemClickListener { adapterView, view, i, l ->
                val selectedc = adapterView.getItemAtPosition(i) as String
                Log.v("sellected class", selectedc)
                val intentUF = Intent(this, FacFeeActivity::class.java)
                intentUF.putExtra("className", selectedc)
                startActivity(intentUF)
            }
        }

    }
}
