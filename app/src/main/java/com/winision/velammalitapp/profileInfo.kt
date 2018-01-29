package com.winision.velammalitapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class profileInfo : AppCompatActivity() {

    lateinit var db: DatabaseReference
    lateinit var mAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_info)
        db = FirebaseDatabase.getInstance().getReference("Users")
        mAuth = FirebaseAuth.getInstance()
        update()
    }
    private fun update() {
        val user = mAuth.currentUser
        val uid = user!!.uid
        val name = findViewById<View>(R.id.nameTxt) as TextView
        val status = findViewById<View>(R.id.statusTxt) as TextView

    }
}