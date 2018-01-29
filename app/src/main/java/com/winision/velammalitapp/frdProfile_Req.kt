package com.winision.velammalitapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_frd_profile__req.*
import java.text.DateFormat
import java.util.*
import kotlin.collections.HashMap

class frdProfile_Req : AppCompatActivity() {

    lateinit var mDatabase: DatabaseReference
    lateinit var followRequest: DatabaseReference
    lateinit var followDatabase: DatabaseReference
    lateinit var mNotification: DatabaseReference
    lateinit var user_key: String
    lateinit var uid: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_frd_profile__req)
        user_key = intent.getStringExtra("user_id")
        val user = FirebaseAuth.getInstance().currentUser
        uid = user!!.uid
        mDatabase = FirebaseDatabase.getInstance().getReference("Users").child(user_key)
        followRequest = FirebaseDatabase.getInstance().getReference("Follow Request Database")
        followDatabase = FirebaseDatabase.getInstance().getReference("Followers")
        mNotification = FirebaseDatabase.getInstance().getReference("Notifications")

        if (user_key.equals(uid)) {
            requestBtn.visibility = View.INVISIBLE
        }
        mDatabase.keepSynced(true)
        mDatabase.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val name = dataSnapshot.child("name").getValue().toString().trim()
                val status = dataSnapshot.child("status").getValue().toString().trim()

                nameTxt.text = name
                statusTxt.text = status

            }

            override fun onCancelled(p0: DatabaseError?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })

        followRequest.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.hasChild(user_key)) {
                    val requestType = dataSnapshot.child(user_key).child(uid).child("Request Type").getValue().toString()
                    if (requestType.equals("Sent")) {
                        requestBtn.text = "Accept Follow Request"
                    }else if (requestType.equals("Received")) {
                        requestBtn.text = "Cancel Request"
                    }
                }
            }
        })

        followDatabase.child(uid).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.hasChild(user_key)) {
                    requestBtn.text = "Un Follow"
                }
            }
        })

    requestBtn.setOnClickListener {
        view: View? ->
        requestBtn.isClickable = false
        if (requestBtn.text == "Follow") {
            follow ()
        }else if (requestBtn.text == "Un Follow") {
            followCancel()
        }else if (requestBtn.text == "Cancel Request") {
            unFollow()
        }else if (requestBtn.text == "Accept Follow Request") {
            acceptFollow()
        }
    }

    }
                        // Follow Logic //
    private fun follow () {
        followRequest.child(uid).child(user_key).child("Request Type").setValue("Sent").addOnCompleteListener {
            task: Task<Void> ->
            if (task.isSuccessful) {
                followRequest.child(user_key).child(uid).child("Request Type").setValue("Received").addOnCompleteListener {
                    task: Task<Void> ->
                    if (task.isSuccessful) {
                        val items = HashMap<String, Any>()
                        items.put("From", uid)
                        items.put("To", user_key)
                        items.put("type", "request")
                        mNotification.child(user_key).push().setValue(items).addOnCompleteListener {
                            task: Task<Void> ->
                        }
                        requestBtn.isClickable = true
                        requestBtn.text = "Cancel Request"
                        toast("Follow Request sent successfully :)")
                    }
                }
            }else {
                toast("Follow Request failed to send :(")
            }
        }
    }

                       //UnFollow Logic //

    private fun unFollow() {
        followRequest.child(uid).child(user_key).removeValue().addOnCompleteListener {
            task: Task<Void> ->
            if (task.isSuccessful) {
                followRequest.child(user_key).child(uid).removeValue().addOnCompleteListener {
                    task: Task<Void> ->
                    if (task.isSuccessful) {
                        requestBtn.text = "Follow"
                        requestBtn.isClickable = true
                        toast("Follow request cancelled successfully :)")
                    }
                }
            }
        }
    }

    private fun acceptFollow() {
        followRequest.child(uid).child(user_key).removeValue().addOnCompleteListener {
            task: Task<Void> ->
            if (task.isSuccessful) {
                followRequest.child(user_key).child(uid).removeValue()
            }
        }
        val date: String = DateFormat.getTimeInstance().format(Date())
        followDatabase.child(uid).child(user_key).setValue(date).addOnCompleteListener {
            task: Task<Void> ->
            if (task.isSuccessful) {
                followDatabase.child(user_key).child(uid).setValue(date).addOnCompleteListener {
                    task: Task<Void> ->
                    if (task.isSuccessful) {
                        requestBtn.isClickable = true
                        requestBtn.text = "Un Follow"
                    }
                }
            }
        }
    }
    private fun followCancel() {
        followDatabase.child(uid).child(user_key).removeValue().addOnCompleteListener {
            task: Task<Void> ->
            if (task.isSuccessful) {
                followDatabase.child(user_key).child(uid).removeValue().addOnCompleteListener {
                    task: Task<Void> ->
                    if (task.isSuccessful) {
                        requestBtn.text = "Follow"
                        requestBtn.isClickable = true
                        toast("Un Followed successfully :)")
                    }
                }
            }
        }
    }

    private fun toast (msg: String) {
        Toast.makeText(applicationContext, msg, Toast.LENGTH_LONG).show()
    }
}
