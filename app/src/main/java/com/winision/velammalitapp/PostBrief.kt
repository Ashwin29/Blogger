package com.winision.velammalitapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_post_brief.*

class PostBrief : AppCompatActivity() {

    lateinit var mDatabase: DatabaseReference
    lateinit var uid: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_brief)

        uid = intent.getStringExtra("user_id")
        mDatabase = FirebaseDatabase.getInstance().getReference("Timeline Posts").child(uid)

        mDatabase.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val name = snapshot.child("name").getValue().toString().trim()
                val title = snapshot.child("title").getValue().toString().trim()
                val description = snapshot.child("description").getValue().toString().trim()
                val downloadUrl = snapshot.child("downloadUrl").getValue().toString().trim()

                Picasso.with(applicationContext).load(downloadUrl).resize(1280, 720).centerCrop().into(imageView)

                nameTxt.text = name
                titleTxt.text = title
                descTxt.text = description


            }

            override fun onCancelled(p0: DatabaseError?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })


    }
}
