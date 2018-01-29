package com.winision.velammalitapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_faculty_code.*

class FacultyCode : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_faculty_code)
        enterBtn.setOnClickListener {
            view: View? ->
            pass()
        }
    }
    private fun pass() {
        if (facultyPass.text.toString() == "abc") {
            startActivity(Intent(this, Login::class.java))
        }else {
            Toast.makeText(this, "Enter right pass phase :(", Toast.LENGTH_LONG).show()
        }
    }
}
