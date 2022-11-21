package com.example.schoolmanagementsystem

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.example.schoolmanagementsystem.databinding.ActivityHomeStudentBinding
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_home_student.*
import kotlinx.android.synthetic.main.nav_header.*

class HomeStudent : AppCompatActivity() {
    lateinit var stoggle:ActionBarDrawerToggle
    lateinit var database:DatabaseReference
    lateinit var user: FirebaseUser
    lateinit var pd:ProgressDialog
    lateinit var binding:ActivityHomeStudentBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityHomeStudentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val snavLayout=findViewById<DrawerLayout>(R.id.drawerLayout)
        val snavView=findViewById<NavigationView>(R.id.nav_view)
        val toolbar=findViewById<Toolbar>(R.id.toolbar)
        stoggle= ActionBarDrawerToggle(this,snavLayout,R.string.open,R.string.close)
        snavLayout.addDrawerListener(stoggle)
        stoggle.syncState()
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val name=findViewById<TextView>(R.id.textView4)
        val intent=getIntent()
        val userid=intent.getStringExtra("userid").toString()
        val classStr=intent.getStringExtra("class").toString()
        var rollno:String=""
        var present:Int?=0
        var working:Int?=0
        pd=ProgressDialog(this@HomeStudent)
        pd.setTitle("Loading")
        pd.setMessage("Please Wait ...")
        pd.show()

        user= FirebaseAuth.getInstance().currentUser!!
        database=FirebaseDatabase.getInstance().getReference("Students").child(classStr).child(userid)
        database.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val stu = snapshot.getValue(StudentData::class.java)
                if (!(stu!!.classname.equals(classStr))) {

                    Toast.makeText(this@HomeStudent, "You selected wrong class", Toast.LENGTH_LONG).show()
                    val intents=Intent(this@HomeStudent,LoginActivity::class.java)
                    startActivity(intents)
                }
                name.setText(stu.name)
                rollno=stu!!.rollno.toString()
                present=stu!!.present
                working=stu!!.working
                pd.dismiss()


            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
        c1att.setOnClickListener{
            val intentA=Intent(this,StuAttendance::class.java)
            intentA.putExtra("userid",userid)
            intentA.putExtra("class",classStr)
            intentA.putExtra("rollno",rollno)
            intentA.putExtra("present",present)
            intentA.putExtra("working", working)
            startActivity(intentA)
        }
        c2marks.setOnClickListener{
            val intentM=Intent(this@HomeStudent, StuMarks::class.java)
            intentM.putExtra("userid",userid)
            startActivity(intentM)
        }
        c3noti.setOnClickListener{
            val intentN=Intent(this, StuNotificationActivity::class.java)
            startActivity(intentN)
        }
        c4.setOnClickListener{
            val intentF=Intent(this@HomeStudent, StuFeeActivity::class.java)
            intentF.putExtra("userid",userid)
            intentF.putExtra("class", classStr)
            startActivity(intentF)
        }
        snavView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.homeS -> Toast.makeText(this,"Clicked Home",Toast.LENGTH_LONG).show()
                R.id.att-> {
                    val intentA=Intent(this,StuAttendance::class.java)
                    intentA.putExtra("userid",userid)
                    intentA.putExtra("class",classStr)
                    intentA.putExtra("rollno",rollno)
                    intentA.putExtra("present",present)
                    intentA.putExtra("working", working)
                    startActivity(intentA)
                }
                R.id.marks->{
                    val intentM=Intent(this@HomeStudent, StuMarks::class.java)
                    intentM.putExtra("userid", userid)
                    startActivity(intentM)
                }
                R.id.fees->{
                    val intentF=Intent(this@HomeStudent, StuFeeActivity::class.java)
                    intentF.putExtra("userid",userid)
                    intentF.putExtra("class", classStr)
                    startActivity(intentF)
                }

            }
            true
        }



    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(stoggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}