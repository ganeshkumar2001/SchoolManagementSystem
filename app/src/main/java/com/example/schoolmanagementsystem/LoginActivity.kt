package com.example.schoolmanagementsystem

import android.R
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.example.schoolmanagementsystem.databinding.ActivityLoginBinding
import com.example.schoolmanagementsystem.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*

class LoginActivity : AppCompatActivity() {
    private lateinit var auth:FirebaseAuth
    private lateinit var binding: ActivityLoginBinding
    val classes= arrayOf("Faculty","Class 1", "Class 2", "Class 3", "Class 4", "Class 5", "Class 6", "Class 7", "Class 8","Class 9", "Class 10")
    var classStr=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth=Firebase.auth
        binding.classL.adapter= ArrayAdapter<String>(this, R.layout.simple_list_item_1,classes)

        binding.logButton.setOnClickListener {
            classL.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    classStr = classes[p2]
                    classStr=classL.selectedItem.toString()
                    Log.v("class Test",classes[p2])

                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    classStr="Faculty"
                }
            }
            performLogIn()
        }
        binding.toRegText.setOnClickListener(){
            val intent=Intent(this, Register::class.java )
            startActivity(intent)
        }

    }

    private fun performLogIn() {
        val email=binding.loginid.text.toString()
        val pass=binding.pass.text.toString()


        if(email.isEmpty()||pass.isEmpty()){
            Toast.makeText(this,"Enter all Fields", Toast.LENGTH_SHORT).show()
            return
        }
        auth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    if(email.contains("srk.com")){
                        val intent=Intent(this@LoginActivity, HomeFaculty::class.java)
                        intent.putExtra("userid",auth.currentUser!!.uid)
                        intent.putExtra("class",classL.selectedItem.toString())
                        startActivity(intent)
                    }
                    else{
                        val intent=Intent(this@LoginActivity, HomeStudent::class.java)
                        intent.putExtra("userid",auth.currentUser!!.uid)
                        intent.putExtra("class",classL.selectedItem.toString())
                        startActivity(intent)
                    }


                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(this,"Not Registered", Toast.LENGTH_SHORT).show()
                }

            }
            .addOnFailureListener {
                Toast.makeText(this,"Error Occured ${it.localizedMessage}",Toast.LENGTH_SHORT).show()
            }

    }
}