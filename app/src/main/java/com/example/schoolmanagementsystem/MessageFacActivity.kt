package com.example.schoolmanagementsystem

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_message_fac.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MessageFacActivity : AppCompatActivity() {
    lateinit var database:DatabaseReference
    lateinit var date:LocalDateTime
    lateinit var dateS:String
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_fac)

        database=FirebaseDatabase.getInstance().getReference("Notifications")

        val formatter=DateTimeFormatter.ofPattern("dd-MM-yyyy  HH:mm:ss")
        date=LocalDateTime.now()
        dateS=date.format(formatter).toString()

        submitMsg.setOnClickListener{
            validateForm()
        }



    }

    private fun validateForm() {
        if(titleMsg.text.isEmpty()|| contextMsg.text.isEmpty()){
            Toast.makeText(this, "Enter all fields", Toast.LENGTH_SHORT).show()
            return
        }
        else{
            database.addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val note=NotificationData(titleMsg.text.toString(), contextMsg.text.toString(),dateS)
                    database.push().setValue(note)
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })

        }
    }
}