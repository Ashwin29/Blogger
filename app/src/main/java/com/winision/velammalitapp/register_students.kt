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
import java.lang.Exception
import java.util.regex.Matcher
import java.util.regex.Pattern

class register_students : AppCompatActivity() {

    lateinit var mDatabase: DatabaseReference
    lateinit var mAuth: FirebaseAuth

    val dept = arrayOf ("CSE", "IT", "ECE", "EEE", "Mech")
    val year = arrayOf ("I", "II", "III", "IV")
    lateinit var deptSpin: Spinner
    lateinit var yearSpin: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_students)
        val signBtn = findViewById<View>(R.id.signBtn) as Button
        deptSpin = findViewById<View>(R.id.deptSpin) as Spinner
        mDatabase = FirebaseDatabase.getInstance().getReference("Users")
        yearSpin = findViewById<View>(R.id.yearSpin) as Spinner
        mAuth = FirebaseAuth.getInstance()
        val deptAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, dept)
        deptAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        deptSpin!!.setAdapter (deptAdapter)
        val yearAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, year)
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        yearSpin!!.setAdapter (yearAdapter)
        signBtn.setOnClickListener {
            view: View? -> signIn ()
        }
    }
    private fun signIn () {
        val nameTxt = findViewById<View>(R.id.nameTxt) as EditText
        val regMailTxt = findViewById<View>(R.id.regMailTxt) as EditText
        val emailTxt = findViewById<View>(R.id.emailTxt) as EditText
        val passwordTxt = findViewById<View>(R.id.passwordTxt) as EditText
        var password = passwordTxt.text.toString().trim()
        var name = nameTxt.text.toString().trim()
        var regMail = regMailTxt.text.toString().trim()
        var email = emailTxt.text.toString().trim()
        val VALID_EMAIL_ADDRESS_REGEX: Pattern = Pattern.compile("^[0-9-]+@[v,e,l,a,m,m,a,l,i,t-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE)
        val dept = deptSpin.selectedItem.toString().trim()
        val year = yearSpin.selectedItem.toString().trim()
        val matcher: Matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(regMail)
        val check: Boolean = matcher.find()
        if (!name.isEmpty() && !regMail.isEmpty() && !dept.isEmpty() && !year.isEmpty() && !email.isEmpty()) {
            if (check == true) {
                mAuth.createUserWithEmailAndPassword(regMail, password).addOnCompleteListener {
                    task: Task<AuthResult> -> if (task.isSuccessful) {
                    val mUser = mAuth.currentUser
                    var uid = mUser!!.uid
                    val mRef = mDatabase.child(uid)
                    val dataMap: HashMap<String, Any> = hashMapOf()
                    dataMap.put("name", name)
                    dataMap.put("email", email)
                    dataMap.put("department", dept)
                    dataMap.put("year", year)
                    dataMap.put("regno", regMail)
                    mRef.setValue(dataMap).addOnCompleteListener {
                        task: Task<Void> -> if (task.isSuccessful) {
                        val intent = Intent(this, Timeline::class.java)
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        startActivity(intent)
                        Toast.makeText(this, "Successfully registered :)", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this, "Error signing up :(", Toast.LENGTH_LONG).show()
                    }
                    }
                }
                }.addOnFailureListener {
                    exception: Exception -> Toast.makeText(this, exception.toString(), Toast.LENGTH_LONG).show()
                }
            }else {
                Toast.makeText(this, "Enter regNo@velammalit.com", Toast.LENGTH_LONG).show()
            }

        }else {
            Toast.makeText(this, "Enter the credentials :|", Toast.LENGTH_LONG).show()
        }
    }
}
