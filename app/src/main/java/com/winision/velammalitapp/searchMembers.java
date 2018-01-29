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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class searchMembers extends AppCompatActivity {

    private RecyclerView memList;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_members);
        memList = findViewById(R.id.memList);
        memList.setHasFixedSize(true);
        memList.setLayoutManager(new LinearLayoutManager(this));
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
    }

    public static class MemberViewHolder extends RecyclerView.ViewHolder {


        public MemberViewHolder(View itemView) {
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

        FirebaseRecyclerAdapter<Members, MemberViewHolder> adapter = new FirebaseRecyclerAdapter<Members, MemberViewHolder>(
                Members.class,
                R.layout.member_row,
                MemberViewHolder.class,
                mDatabase
        ) {
            @Override
            protected void populateViewHolder(MemberViewHolder viewHolder, Members model, int position) {
                viewHolder.setName(model.getName());
                viewHolder.setStatus(model.getStatus());
                viewHolder.setdownloadUrl(model.getDownloadUrl(), getApplicationContext());

                final String user_id = getRef(position).getKey();

                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent memIntent = new Intent(searchMembers.this, frdProfile_Req.class);
                        memIntent.putExtra("user_id", user_id);
                        startActivity(memIntent);
                    }
                });


            }
        };
        memList.setAdapter(adapter);
    }
}
