package com.winision.velammalitapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class frdsList extends AppCompatActivity {

    private RecyclerView frds;
    private DatabaseReference mDatabase;
    private DatabaseReference mMetaData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frds_list);
        frds = findViewById(R.id.frds);
        frds.setHasFixedSize(true);
        frds.setLayoutManager(new LinearLayoutManager(this));
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Followers");
        mMetaData = FirebaseDatabase.getInstance().getReference().child("Users");
    }
    public static class FriendsViewHolder extends RecyclerView.ViewHolder {


        public FriendsViewHolder(View itemView) {
            super(itemView);
        }

        public void setName(String name) {
            TextView nameTxt = itemView.findViewById(R.id.nameTxt);
            nameTxt.setText(name);
        }

        public void setStatus(String status) {
            TextView statusTxt = itemView.findViewById(R.id.statusTxt);
            statusTxt.setText(status);
        }

        public void setdownloadUrl(String downloadUrl, Context ctx) {
            CircleImageView imageView = itemView.findViewById(R.id.thumbnail);
            Picasso.with(ctx).load(downloadUrl).resize(50, 50).centerCrop().into(imageView);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Members, FriendsViewHolder> adapter = new FirebaseRecyclerAdapter<Members, FriendsViewHolder>(
                Members.class,
                R.layout.member_row,
                FriendsViewHolder.class,
                mDatabase
        ) {
            @Override
            protected void populateViewHolder(final FriendsViewHolder viewHolder, final Members model, int position) {


                final String user_id = getRef(position).getKey();

                mMetaData.child(user_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String name = dataSnapshot.child("name").getValue().toString().trim();
                        String status = dataSnapshot.child("status").getValue().toString().trim();
                      //  String downloadUrl = dataSnapshot.child("downloadUrl").getValue().toString().trim();
                        viewHolder.setName(name);
                        viewHolder.setStatus(status);

                        viewHolder.setdownloadUrl(model.getDownloadUrl(), getApplicationContext());

                        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent memIntent = new Intent(frdsList.this, ResumeViewing.class);
                                memIntent.putExtra("user_id", user_id);
                                startActivity(memIntent);
                            }
                        });

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });



            }
        };
        frds.setAdapter(adapter);
    }
}
