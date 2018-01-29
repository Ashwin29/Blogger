package com.winision.velammalitapp;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class studyList extends AppCompatActivity {

    private ListView lists;
    private DatabaseReference mDatabase;
    private ArrayList<String> mMaterials = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_list);
        lists = findViewById(R.id.list);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Study Materials");
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, mMaterials);
        lists.setAdapter(arrayAdapter);
        try {
            mDatabase.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    final String downloadUrl = dataSnapshot.child("downloadUrl").getValue().toString();
                    String department = dataSnapshot.child("department").getValue().toString();
                  //  String year = dataSnapshot.child("year").getValue().toString();

                    mMaterials.add("New Study Material is available for " + " "  + " " + department);
                    arrayAdapter.notifyDataSetChanged();
                    lists.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                            Uri uri = Uri.parse(downloadUrl);
                            DownloadManager.Request request = new DownloadManager.Request(uri);
                            request.setTitle("Study Material");
                            request.setDescription("Downloading....");
                            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "file.pdf");
                            downloadManager.enqueue(request);
                        }
                    });

                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        } catch (Exception e) {
            Toast.makeText(studyList.this, e.toString(), Toast.LENGTH_LONG).show();
        }

    }
}



