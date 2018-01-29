package com.winision.velammalitapp;


import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.sql.Time;


public class Timeline extends AppCompatActivity {

    private RecyclerView mTimeline;
    private FloatingActionButton settings;
    private FirebaseAuth mAuth;
    private FloatingActionButton searchMemFab;
    private FloatingActionButton downloadMaterials;
    private FloatingActionButton resumeFab;
    private FloatingActionButton frdsFab;
    private DatabaseReference mReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        mTimeline = findViewById(R.id.timeline);
        mTimeline.setLayoutManager(new LinearLayoutManager(this));
        frdsFab = findViewById(R.id.frdsFab);
        searchMemFab = findViewById(R.id.searchMemFab);
        downloadMaterials = findViewById(R.id.downloadMaterials);
        resumeFab = findViewById(R.id.resumeFab);
        mReference = FirebaseDatabase.getInstance().getReference().child("Timeline Posts").child("Students");
        settings = findViewById(R.id.settings);
        mAuth = FirebaseAuth.getInstance();
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Timeline.this, Student_Settings.class));
            }
        });
        searchMemFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Timeline.this, searchMembers.class));
            }
        });
        downloadMaterials.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Timeline.this, studyList.class));
            }
        });
        resumeFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Timeline.this, Resume.class));
            }
        });
        frdsFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Timeline.this, frdsList.class));
            }
        });
    }

    public static class TimelineViewHolder extends RecyclerView.ViewHolder {


        public TimelineViewHolder(View itemView) {
            super(itemView);
        }

        public void setTitle (String title) {
            TextView postTitle = itemView.findViewById(R.id.titleTxt);
            postTitle.setText(title);
        }

        public void setDescription (String description) {
            TextView postDescription = itemView.findViewById(R.id.descTxt);
            postDescription.setText(description);
        }

        public void setName (String name) {
            TextView nameTxt = itemView.findViewById(R.id.nameTxt);
            nameTxt.setText(name);
        }

        public void setDepartment (String department) {
            TextView deptTxt = itemView.findViewById(R.id.deptTxt);
            deptTxt.setText(department);
        }

        public void setdownloadUrl(final String downloadUrl, final Context ctx) {
            final ImageView postImage = itemView.findViewById(R.id.imageView);
            Picasso.with(ctx).load(downloadUrl).resize(1280, 720).centerCrop()
                    .into(postImage);
        }

    }
    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Posts, TimelineViewHolder> adapter = new FirebaseRecyclerAdapter<Posts, TimelineViewHolder>(
                Posts.class,
                R.layout.timeline_row,
                TimelineViewHolder.class,
                mReference
        ) {
            @Override
            protected void populateViewHolder(TimelineViewHolder viewHolder, Posts model, int position) {
                viewHolder.setTitle(model.getTitle());
                viewHolder.setName(model.getName());
                viewHolder.setDescription(model.getDescription());
                viewHolder.setDepartment(model.getDepartment());
                viewHolder.setdownloadUrl(model.getDownloadUrl(), getApplicationContext());

                final String user_id = getRef(position).toString();

                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent memIntent = new Intent(Timeline.this, PostBrief.class);
                        memIntent.putExtra("user_id", user_id);
                        startActivity(memIntent);
                    }
                });
            }
        };
        mTimeline.setAdapter(adapter);
    }

}
