package com.winision.velammalitapp

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.OnProgressListener
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_study_mat__upload.*

class StudyMat_Upload : AppCompatActivity() {

    var FILE_CHOOSE_CODE: Int = 0
    lateinit var mStorage: StorageReference
    val dept = arrayOf ("CSE", "IT", "ECE", "EEE", "Mech")
    val year = arrayOf ("I", "II", "III", "IV")
    lateinit var mDatabase: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_study_mat__upload)
        progress.visibility = View.INVISIBLE
        val deptAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, dept)
        deptAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        deptSpin.setAdapter (deptAdapter)
        val yearAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, year)
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        yearSpin.setAdapter (yearAdapter)
        mStorage = FirebaseStorage.getInstance().getReference("Study Materials")
        mDatabase = FirebaseDatabase.getInstance().getReference("Study Materials")
        uplBtn.setOnClickListener {
            view: View? -> pickFiles()
        }
    }
    private fun pickFiles() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.setType("*/*")
        intent.addCategory(Intent.CATEGORY_OPENABLE)

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a file to Upload."),
                    FILE_CHOOSE_CODE
            )
        }catch (e: Exception) {
            toast(e.toString().trim())
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == FILE_CHOOSE_CODE && resultCode == Activity.RESULT_OK) {

            val fileUri: Uri = data!!.data

            try {
                val mReference = mStorage.child(fileUri.lastPathSegment)
                 mReference.putFile(fileUri).addOnSuccessListener {
                    taskSnapshot: UploadTask.TaskSnapshot? ->
                    val downloadUrl = taskSnapshot!!.downloadUrl.toString()
                     val items = HashMap<String, Any>()
                     items.put("downloadUrl", downloadUrl)
                     items.put("year", yearSpin.selectedItem.toString())
                     items.put("department", deptSpin.selectedItem.toString())
                     val mRef = mDatabase.push();
                     mRef.setValue(items).addOnCompleteListener {
                         task: Task<Void> ->
                         if (task.isSuccessful) {
                             toast("Uploaded Successfully :)")
                             startActivity(Intent(this, Staffs_Timeline::class.java))
                         }
                     }
                }.addOnProgressListener(object : OnProgressListener<UploadTask.TaskSnapshot> {
                            override fun onProgress(taskSnapshot: UploadTask.TaskSnapshot) {
                                val progressVal: Double = (100.0 + taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount)
                                progress.setProgress(progressVal.toInt())
                                val bytesTrans: Long = taskSnapshot.bytesTransferred / (1024 * 1024)
                                val byteCount: Long =   taskSnapshot.totalByteCount / (1024 * 1024)
                                progressNo.text = bytesTrans.toString() + " / " + byteCount + "mb"
                            }
                        })
            }catch (e: Exception) {
                toast(e.toString())
            }



        }
    }
    private fun toast(msg: String) {
        Toast.makeText(applicationContext, msg, Toast.LENGTH_LONG).show()
    }
}
