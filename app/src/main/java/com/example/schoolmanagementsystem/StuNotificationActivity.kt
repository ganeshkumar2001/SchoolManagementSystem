package com.example.schoolmanagementsystem

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class StuNotificationActivity : AppCompatActivity() {
    private lateinit var notidatabase:DatabaseReference
    private lateinit var recyclerview:RecyclerView
    private lateinit var list:ArrayList<NotificationData>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stu_notification)

        recyclerview=findViewById(R.id.notiRv)
        recyclerview.layoutManager=LinearLayoutManager(this)

        list= arrayListOf()

        getNotiData()
    }

    private fun getNotiData() {
        notidatabase=FirebaseDatabase.getInstance().getReference("Notifications")
        notidatabase.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for(notiSnapshot in snapshot.children){
                        val cid:String=notiSnapshot.key.toString()
                        val noti=snapshot.child(cid).getValue(NotificationData::class.java)
                        list.add(noti!!)
                    }

                    list.sortByDescending {
                        it.date.toString()
                    }

                    recyclerview.adapter=MyAdapter(this@StuNotificationActivity, list)
                }

            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }
}