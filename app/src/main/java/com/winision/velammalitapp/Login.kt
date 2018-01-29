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
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_login.*
import java.lang.Exception
import java.util.regex.Matcher
import java.util.regex.Pattern

class Login : AppCompatActivity() {

    lateinit var mAuth: FirebaseAuth
    lateinit var mUserDatabaseReference: DatabaseReference
    lateinit var authStateListener: FirebaseAuth.AuthStateListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val login = findViewById<View>(R.id.loginBtn) as Button
        val account = findViewById<View>(R.id.create_accountTxt) as TextView
        mAuth = FirebaseAuth.getInstance()
        authStateListener = FirebaseAuth.AuthStateListener {
            firebaseAuth: FirebaseAuth ->
            if (firebaseAuth.currentUser != null) {
                val intent = Intent(this, Staffs_Timeline::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
            }
        }
        mUserDatabaseReference = FirebaseDatabase.getInstance().getReference("Users")
        account.setOnClickListener {
            view: View? -> startActivity(Intent(this, register_staffs::class.java))
        }
        mAuth = FirebaseAuth.getInstance()

        login.setOnClickListener {
            view: View? -> login()
        }
    }
    private fun login () {
        val regMailTxt = findViewById<View>(R.id.emailTxt) as EditText
        val passwordTxt = findViewById<View>(R.id.passwordTxt) as EditText
        var regMail = regMailTxt.text.toString().trim()
        var password = passwordTxt.text.toString().trim()
        progress.visibility = View.VISIBLE
      //  val VALID_EMAIL_ADDRESS_REGEX: Pattern = Pattern.compile("^[0-9-]+@[v,e,l,a,m,m,a,l,i,t-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE)
      //  val matcher: Matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(regMail)
     //   val check: Boolean = matcher.find()
        if (!regMail.isEmpty() && !password.isEmpty()) {
          //  if (check == true) {
                mAuth.signInWithEmailAndPassword(regMail, password).addOnCompleteListener {
                    task: Task<AuthResult> -> if (task.isSuccessful) {
                    val uid = mAuth.currentUser!!.uid
                    val deviceToken = FirebaseInstanceId.getInstance().getToken()
                    mUserDatabaseReference.child(uid).child("device_token").setValue(deviceToken).addOnCompleteListener {
                        task: Task<Void> ->
                        progress.visibility = View.INVISIBLE
                        startActivity(Intent(this, Staffs_Timeline::class.java))
                        Toast.makeText(this, "Successfully Logged in :)", Toast.LENGTH_LONG).show()
                    }

                }
                }.addOnFailureListener {
                    exception: Exception -> Toast.makeText(this, exception.toString(), Toast.LENGTH_LONG).show()
                }
         //   }else {
           //     Toast.makeText(this, "Enter regNo@velammalit.com", Toast.LENGTH_LONG).show()
          //  }

        }else {
            Toast.makeText(this, "Enter the credentials :|", Toast.LENGTH_LONG).show()
        }
    }
    public override fun onStart() {
        super.onStart()
        mAuth.addAuthStateListener(authStateListener)
    }

    public override fun onPause() {
        super.onPause()
        mAuth.removeAuthStateListener(authStateListener)
    }
}
