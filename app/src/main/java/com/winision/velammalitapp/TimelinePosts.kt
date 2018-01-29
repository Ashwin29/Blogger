package com.winision.velammalitapp

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_timeline_posts.*

class TimelinePosts : AppCompatActivity() {

    val IMAGE: Int = 1
    lateinit var uri: Uri
    lateinit var mDatabase: DatabaseReference
    lateinit var mStorage: StorageReference
    lateinit var mAuth: FirebaseAuth
    lateinit var postImg: ImageButton
    lateinit var mDBRef: DatabaseReference
    lateinit var newsFeed: DatabaseReference

    val dept = arrayOf("CSE", "IT", "ECE", "EEE", "Mech")
    val type = arrayOf("Public", "Students")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timeline_posts)
        postImg = findViewById<View>(R.id.postImg) as ImageButton
        val postBtn = findViewById<View>(R.id.postBtn) as Button
        val postType = findViewById<View>(R.id.postType) as Spinner
        val deptSpin = findViewById<View>(R.id.deptSpin) as Spinner
        val deptAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, dept)
        deptAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        deptSpin.setAdapter(deptAdapter)
        val postAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, type)
        postAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        postType.setAdapter(postAdapter)
        mAuth = FirebaseAuth.getInstance()
        val user = mAuth.currentUser
        val uid = user!!.uid
        mDatabase = FirebaseDatabase.getInstance().getReference("Timeline Posts").child("Students")
        newsFeed = FirebaseDatabase.getInstance().getReference("Timeline Posts").child("Public")
        mStorage = FirebaseStorage.getInstance().getReference("Timeline Posts").child("Post Images")
        mDBRef = FirebaseDatabase.getInstance().getReference("Users").child(uid)
        postImg.setOnClickListener { view: View? ->
            galleryIntent()
        }
        postBtn.setOnClickListener { view: View? ->
            post()
        }
    }

    private fun galleryIntent() {
        val intent = Intent()
        intent.setType("image/*")
        intent.setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(Intent.createChooser(intent, "Select PDF"), IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE) {
                uri = data!!.data
                postImg.setImageURI(uri)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun post() {
        val postTitleTxt = findViewById<View>(R.id.postTitle) as EditText
        val postDescTxt = findViewById<View>(R.id.postDesc) as EditText
        val postTitle = postTitleTxt.text.toString().trim()
        val postDesc = postDescTxt.text.toString().trim()
        val visi = postType.selectedItem.toString().trim()
        val department = deptSpin.selectedItem.toString().trim()
        var downloadUrl: String
        val user = mAuth.currentUser
        val uid = user!!.uid
        val dataItems = HashMap<String, Any>()

        if (!postTitle.isEmpty() && !postDesc.isEmpty()) {
            if (visi.equals("Public")) {
                val mReference = mStorage.child(uri.lastPathSegment)
                mReference.putFile(uri).addOnSuccessListener { taskSnapshot: UploadTask.TaskSnapshot? ->
                    downloadUrl = taskSnapshot!!.downloadUrl.toString()
                    dataItems.put("title", postTitle)
                    dataItems.put("description", postDesc)
                    dataItems.put("department", department)
                    dataItems.put("type", visi)
                    dataItems.put("uid", uid)
                    dataItems.put("downloadUrl", downloadUrl)
                    val mREf = newsFeed.push()

                    mDBRef.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(p0: DataSnapshot?) {
                            val name = p0!!.child("name").value.toString()
                            mREf.setValue(dataItems)
                            mREf.child("name").setValue(name).addOnCompleteListener { task: Task<Void> ->
                                if (task.isSuccessful) {
                                    startActivity(Intent(applicationContext, Timeline::class.java))
                                }
                            }
                        }

                        override fun onCancelled(p0: DatabaseError?) {
                            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        }
                    })
                }
            }else if (visi.equals("Students")) {
                val mReference = mStorage.child(uri.lastPathSegment)
                mReference.putFile(uri).addOnSuccessListener { taskSnapshot: UploadTask.TaskSnapshot? ->
                    downloadUrl = taskSnapshot!!.downloadUrl.toString()
                    dataItems.put("title", postTitle)
                    dataItems.put("description", postDesc)
                    dataItems.put("department", department)
                    dataItems.put("type", visi)
                    dataItems.put("uid", uid)
                    dataItems.put("downloadUrl", downloadUrl)
                    val mREf = mDatabase.push()

                    mDBRef.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(p0: DataSnapshot?) {
                            val name = p0!!.child("name").value.toString()
                            mREf.setValue(dataItems)
                            mREf.child("name").setValue(name).addOnCompleteListener { task: Task<Void> ->
                                if (task.isSuccessful) {
                                    startActivity(Intent(applicationContext, Timeline::class.java))
                                }
                            }
                        }

                        override fun onCancelled(p0: DatabaseError?) {
                            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        }
                    })
                }
            }

        }


    }
}

