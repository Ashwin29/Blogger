package com.winision.velammalitapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_student__settings.*

class Student_Settings : AppCompatActivity() {

    lateinit var mDatabase: DatabaseReference
    lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student__settings)
        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance().getReference("Users").child(uid)

        mDatabase.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                nameTxt.text = snapshot.child("name").value.toString().trim()
                statusTxt.text = snapshot.child("status").value.toString().trim()
            }
        })

        editBtn.setOnClickListener {
            view: View? ->
        }
        signOut.setOnClickListener {
            view: View? ->  mAuth.signOut()
            startActivity(Intent(this, MainPage::class.java))
        }

    }
}
