package com.winision.velammalitapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_resume_viewing.*

class ResumeViewing : AppCompatActivity() {

    lateinit var user_key: String
    lateinit var mDatabase: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resume_viewing)

        user_key = intent.getStringExtra("user_id")
        mDatabase = FirebaseDatabase.getInstance().getReference("Resume")

        try {
            mDatabase.child(user_key).addValueEventListener(object: ValueEventListener {
                override fun onCancelled(p0: DatabaseError?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    aboutTxt.text = dataSnapshot.child("About Yourself").value.toString().trim()
                    technicalSkills.text = dataSnapshot.child("Technical Skills").value.toString().trim()
                    interpersonalSkillsTxt.text = dataSnapshot.child("Interpersonal Skills").value.toString().trim()
                    marketingTxt.text = dataSnapshot.child("Marketing Skills").value.toString().trim()
                    achievementsTxt.text = dataSnapshot.child("Achievements").value.toString().trim()
                    activitiesTxt.text = dataSnapshot.child("Activities").value.toString().trim()
                    percentSSLC.text = dataSnapshot.child("HSC").value.toString().trim()
                    percentHSC.text = dataSnapshot.child("SSLC").value.toString().trim()
                    percentCGPA.text = dataSnapshot.child("CGPA").value.toString().trim()
                    fbTxt.text = dataSnapshot.child("Facebook").value.toString().trim()
                    instaTxt.text = dataSnapshot.child("Instagram").value.toString().trim()
                    linkedInTxt.text = dataSnapshot.child("linkedIn").value.toString().trim()
                    otherNetTxt.text = dataSnapshot.child("Other Networks").value.toString().trim()
                    gitTxt.text = dataSnapshot.child("GitHub").value.toString().trim()
                    blogsTxt.text = dataSnapshot.child("Blogs").value.toString().trim()
                    additionalInfoTxt.text = dataSnapshot.child("Additional Information").value.toString().trim()
                }
            })
        }catch (e: Exception) {
            Toast.makeText(applicationContext, e.toString(), Toast.LENGTH_LONG).show()
        }


    }
}
