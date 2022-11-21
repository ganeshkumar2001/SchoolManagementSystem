package com.example.schoolmanagementsystem

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.os.HandlerCompat.postDelayed
import com.example.schoolmanagementsystem.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.coroutines.delay
import org.w3c.dom.Text
import java.util.*


class Register : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var database:DatabaseReference
    private var classStudent:String="Faculty"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth
        binding.toLogText.setOnClickListener(){
            val intet=Intent(this, LoginActivity::class.java)
            startActivity(intet)
        }


        val classes= arrayOf("Faculty","Class 1", "Class 2", "Class 3", "Class 4", "Class 5", "Class 6", "Class 7", "Class 8","Class 9", "Class 10")
        binding.classDD.adapter=ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,classes)
        submitReg.setOnClickListener {
            classDD.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    classStudent = classes[p2]
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    classStudent = "Faculty"

                }
            }
            Log.v("class","${classStudent}")
            Handler().postDelayed({
                Toast.makeText(this@Register,"${classStudent}", Toast.LENGTH_SHORT).show()
                performSignUp()
            }, 2000)
        }


    }




    private fun performSignUp() {
        val name=binding.nameReg.text.toString()
        val email=binding.phone.text.toString()
        val password=binding.password.text.toString()
        val rollNo=binding.rollNo.text.toString()
        val classn=classDD.selectedItem.toString()
        val fees=when(classn){
            "Class 1" -> 10000
            "Class 2" -> 12000
            "Class 3" -> 14000
            "Class 4" -> 16000
            "Class 5" -> 18000
            "Class 6" -> 20000
            "Class 7" -> 22000
            "Class 8" -> 24000
            "Class 9" -> 26000
            "Class 10" -> 28000
            else -> {0}
        }

        if(email.isEmpty()||password.isEmpty()){
            Toast.makeText(this,"Enter all Fields", Toast.LENGTH_SHORT).show()
            return
        }
        else {
            database = FirebaseDatabase.getInstance().getReference("Faculty")
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        if (classDD.selectedItem == "Faculty") {

                            database.addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    if (snapshot.hasChild(rollNo)) {
                                        Toast.makeText(
                                            this@Register,
                                            "${rollNo} already exist",
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()
                                    } else {
                                        Log.v("firebasefac","I am in Faculty ${classStudent}")

                                        val faculty =
                                            FacultyData(name, email, auth.currentUser!!.uid)
                                        database.child(auth.currentUser!!.uid).setValue(faculty)
                                        val intent=Intent(this@Register, HomeFaculty::class.java)
                                        intent.putExtra("uid", auth.currentUser!!.uid)
                                        startActivity(intent)
                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    Toast.makeText(
                                        this@Register,
                                        "Error in Factulty",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                }
                            })
                        }

                        else{
                            Log.v("firebasestd","I am in Student ${classDD.selectedItem.toString()}")
                            database = FirebaseDatabase.getInstance().getReference("Students")
                            database.addListenerForSingleValueEvent(object: ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    if (snapshot.hasChild(rollNo)) {
                                        Toast.makeText(this@Register, "${rollNo} already exist", Toast.LENGTH_SHORT)
                                            .show()
                                    } else {
                                        val student = StudentData(name, email, classDD.selectedItem.toString(), rollNo,auth.currentUser!!.uid,0,0,fees)
                                        database.child(classDD.selectedItem.toString()).child(auth.currentUser!!.uid).setValue(student)
                                        Toast.makeText(this@Register,"You are suceesfully Registered, Login to Continue",Toast.LENGTH_LONG).show()
                                        val intentS=Intent(this@Register, LoginActivity::class.java)
                                        startActivity(intentS)
                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {
                                }
                            })



                        }


                    } else {
                        Toast.makeText(this, "Error occured", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Error Occured ${it.localizedMessage}", Toast.LENGTH_SHORT)
                        .show()
                }

        }
    }


}