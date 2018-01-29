package com.winision.velammalitapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class MainPage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView newsFeed;
    private DatabaseReference mDatabase;
    private FloatingActionButton students;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        Toolbar toolbar = findViewById(R.id.toolbar);
        newsFeed = findViewById(R.id.newsFeed);
        toolbar.setTitle("Velammal-ITech");
        newsFeed.setLayoutManager(new LinearLayoutManager(this));
        setSupportActionBar(toolbar);
        students = findViewById(R.id.loginStds);

        students.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainPage.this, Students_Login.class));
            }
        });

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Timeline Posts").child("Public");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.login) {
            startActivity(new Intent(MainPage.this, FacultyCode.class));
        } else if (id == R.id.nav_gallery) {
            startActivity(new Intent(MainPage.this, About_Clg.class));
        } else if (id == R.id.nav_slideshow) {
            startActivity(new Intent(MainPage.this, CSE.class));
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public static class NewsFeedHolder extends RecyclerView.ViewHolder {

        public NewsFeedHolder(View itemView) {
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
        FirebaseRecyclerAdapter<Posts, NewsFeedHolder> adapter = new FirebaseRecyclerAdapter<Posts, NewsFeedHolder>(
                Posts.class,
                R.layout.timeline_row,
                NewsFeedHolder.class,
                mDatabase
        ) {
            @Override
            protected void populateViewHolder(NewsFeedHolder viewHolder, Posts model, int position) {
                viewHolder.setTitle(model.getTitle());
                viewHolder.setName(model.getName());
                viewHolder.setDescription(model.getDescription());
                viewHolder.setDepartment(model.getDepartment());
                viewHolder.setdownloadUrl(model.getDownloadUrl(), getApplicationContext());

            }
        };
        newsFeed.setAdapter(adapter);

    }


}
