package com.winision.velammalitapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_resume.*

class Resume : AppCompatActivity() {

    lateinit var mDatabase: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resume)
        mDatabase = FirebaseDatabase.getInstance().getReference("Resume")
        submitBtn.setOnClickListener {
            view: View? ->
            submit()
        }
    }
    private fun submit() {
        val about = aboutTxt.text.toString().trim()
        val technicalSkills = technicalSkills.text.toString().trim()
        val interpersonalSkills = interpersonalSkillsTxt.text.toString().trim()
        val marketingSkills = marketingTxt.text.toString().trim()
        val achievements = achievementsTxt.text.toString().trim()
        val activities = activitiesTxt.text.toString().trim()
        val percentSSLCTxt = percentSSLC.text.toString().trim()
        val percentHSCTxt = percentHSC.text.toString().trim()
        val percentCGPATxt = percentCGPA.text.toString().trim()
        val fb = fbTxt.text.toString().trim()
        val insta = instaTxt.text.toString().trim()
        val linkedIn = linkedInTxt.text.toString().trim()
        val otherNetworks = otherNetTxt.text.toString().trim()
        val gitHub = gitTxt.text.toString().trim()
        val blogs = blogsTxt.text.toString().trim()
        val additionalInformation = additionalInfoTxt.text.toString().trim()


        try {
            val dataItems = HashMap<String, Any>()
            dataItems.put("About Yourself", about)
            dataItems.put("Technical Skills", technicalSkills)
            dataItems.put("Interpersonal Skills", interpersonalSkills)
            dataItems.put("Marketing Skills", marketingSkills)
            dataItems.put("Achievements", achievements)
            dataItems.put("Activities", activities)
            dataItems.put("HSC", percentHSCTxt)
            dataItems.put("SSLC", percentSSLCTxt)
            dataItems.put("CGPA", percentCGPATxt)
            dataItems.put("Facebook", fb)
            dataItems.put("Instagram", insta)
            dataItems.put("linkedIn", linkedIn)
            dataItems.put("Other Networks", otherNetworks)
            dataItems.put("GitHub", gitHub)
            dataItems.put("Blogs", blogs)
            dataItems.put("Additional Information", additionalInformation)
            val uid = FirebaseAuth.getInstance().currentUser!!.uid
            mDatabase.child(uid).setValue(dataItems).addOnCompleteListener {
                task: Task<Void> ->
                if (task.isSuccessful) {
                    toast("Successfully Updated the Resume :)")
                }else {
                    toast("Error in Updating Resume :(")
                }
            }
        } catch (e: Exception) {
            toast(e.toString())
        }

    }
    private fun toast (msg: String) {
        Toast.makeText(applicationContext, msg, Toast.LENGTH_LONG).show()
    }
}
