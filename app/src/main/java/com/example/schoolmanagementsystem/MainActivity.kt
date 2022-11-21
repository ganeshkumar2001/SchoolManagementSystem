package com.example.schoolmanagementsystem

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Handler(Looper.getMainLooper()).postDelayed(
            {
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
                val i = Intent(this, LoginActivity::class.java)
                startActivity(i)
                finish() },1500)

    }
    }
