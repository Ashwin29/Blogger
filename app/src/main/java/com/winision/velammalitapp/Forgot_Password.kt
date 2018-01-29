package com.winision.velammalitapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_forgot__password.*

class Forgot_Password : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot__password)
        submitBtn.setOnClickListener {
            view: View? ->
            reset()
        }
    }
    private fun reset() {
        val email = emailTxt.text.toString().trim()
        FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener {
            task: Task<Void> ->
            startActivity(Intent(this, MainPage::class.java))
            Toast.makeText(this, "Password reset mail has been sent to your mail id :)", Toast.LENGTH_LONG).show()
        }
    }
}
