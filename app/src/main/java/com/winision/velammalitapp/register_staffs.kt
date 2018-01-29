package com.winision.velammalitapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register_staffs.*
import java.lang.Exception

class register_staffs : AppCompatActivity() {

    val dept = arrayOf ("CSE", "IT", "ECE", "EEE", "Mech")
    lateinit var mDatabase: DatabaseReference
    lateinit var mAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_staffs)
        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance().getReference("Users")
        val deptAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, dept)
        deptAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        deptSpin.setAdapter (deptAdapter)
        signUp.setOnClickListener {
            view: View? -> register()
        }
    }
    private fun register() {
        val nameTxt = findViewById<View>(R.id.nameTxt) as EditText
        val emailTxt = findViewById<View>(R.id.emailTxt) as EditText
        val passwordTxt = findViewById<View>(R.id.passwordTxt) as EditText
        val dept = deptSpin.selectedItem.toString().trim()
        val name = nameTxt.text.toString().trim()
        val password = passwordTxt.text.toString().trim()
        val email = emailTxt.text.toString().trim()
        if (!name.isEmpty() && !password.isEmpty() && !email.isEmpty()) {
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                task: Task<AuthResult> -> if (task.isSuccessful) {
                val user = mAuth.currentUser
                val uid = user!!.uid
                val items = HashMap<String, Any>()
                items.put("name", name)
                items.put("password", password)
                items.put("department", dept)
                items.put("email", email)
                items.put("status", "Hey there:)")
                mDatabase.child(uid).setValue(items).addOnCompleteListener {
                    task: Task<Void> -> if (task.isSuccessful) {
                    startActivity(Intent(this, Staffs_Timeline::class.java))
                    toast("Successfully signed in :)")
                }
                }.addOnFailureListener {
                    exception: Exception -> toast(exception.toString())
                }
            }
            }.addOnFailureListener {
                exception: Exception -> toast(exception.toString())
            }
        }else {
            toast("Please fill up the fields :|")
        }

    }
    private fun toast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }
}
