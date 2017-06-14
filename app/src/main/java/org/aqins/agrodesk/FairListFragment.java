package org.aqins.agrodesk;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.sql.Date;
import java.text.SimpleDateFormat;

public class FairListFragment extends Fragment {

    private RecyclerView mFairList;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private DatabaseReference mDatabaseLike;
    private DatabaseReference mDatabaseJoin;

    private boolean mProcessLike = false;
    private boolean mProcessJoin = false;

    public static FairListFragment newInstance() {
        FairListFragment fragment = new FairListFragment();
        return fragment;
    }

    public FairListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Fair");
        mDatabaseLike = FirebaseDatabase.getInstance().getReference().child("Likes");
        mDatabaseJoin = FirebaseDatabase.getInstance().getReference().child("Join");

        mDatabaseLike.keepSynced(true);
        mDatabaseJoin.keepSynced(true);
        mDatabase.keepSynced(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_fair, container,
                false);

        mFairList = (RecyclerView) rootView.findViewById(R.id.fair_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);

        mFairList.setHasFixedSize(true);
        mFairList.setLayoutManager(layoutManager);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Fair,FairViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Fair, FairViewHolder>(
                Fair.class,
                R.layout.fair_row,
                FairViewHolder.class,
                mDatabase

        ) {
            @Override
            protected void populateViewHolder(final FairViewHolder viewHolder, final Fair model, int position) {

                final String post_key = getRef(position).getKey();

                viewHolder.setUsername(model.username);
                viewHolder.setPost_time(model.post_time);

                viewHolder.setLikeNum(post_key);
                viewHolder.setCommentNum(post_key);
                viewHolder.setLikeBtn(post_key);

                viewHolder.setJoinBtn(post_key);
                viewHolder.setJoinNum(post_key);

                viewHolder.mView.setOnClickListener(new RecyclerView.OnClickListener(){
                    @Override
                    public void onClick(View view) {

                        //Toast.makeText(BlogListFragment.this, post_key, Toast.LENGTH_LONG).show();

                        Intent singleBlogIntent = new Intent(getActivity(), FairSingleActivity.class);
                        singleBlogIntent.putExtra("blog_id", post_key);
                        startActivity(singleBlogIntent);

                    }

                });

                viewHolder.mLikeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        mProcessLike = true;
                        mDatabaseLike.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                if (mProcessLike) {

                                    if (dataSnapshot.child(post_key).hasChild(mAuth.getCurrentUser().getUid())) {

                                        mDatabaseLike.child(post_key).child(mAuth.getCurrentUser().getUid()).removeValue();
                                        mProcessLike = false;

                                    } else {

                                        mDatabaseLike.child(post_key).child(mAuth.getCurrentUser().getUid()).setValue("RandomValue");
                                        mProcessLike = false;

                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                });

                viewHolder.mJoinBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mProcessJoin = true;
                        mDatabaseJoin.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(mProcessJoin) {
                                    if (dataSnapshot.child(post_key).hasChild(mAuth.getCurrentUser().getUid())) {
                                        mDatabaseJoin.child(post_key).child(mAuth.getCurrentUser().getUid()).removeValue();
                                    } else {
                                        mDatabaseJoin.child(post_key).child(mAuth.getCurrentUser().getUid()).setValue("RandomValue");
                                    }
                                    mProcessJoin = false;
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                });

                viewHolder.tvPrjName.setText(model.projName);
                viewHolder.tvQualification.setText(model.quality);
                viewHolder.tvNumVolun.setText(model.num_volun);
                viewHolder.tvActivity1.setText(model.activity1);
                viewHolder.tvActivity2.setText(model.activity2);
                viewHolder.tvCountry.setText(model.country);
                viewHolder.tvCity.setText(model.meeting_city);
                viewHolder.tvDate.setText(model.meeting_date);
                viewHolder.tvDesc.setText(model.meeting_desc);
                viewHolder.tvPlace.setText(model.meeting_place);
            }
        };

        mFairList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class FairViewHolder extends RecyclerView.ViewHolder{
        View mView;

        ImageButton mCommentBtn;
        ImageButton mLikeBtn;
        ImageButton mJoinBtn;
        DatabaseReference mDatabaseLike;
        DatabaseReference mDatabaseJoin;
        DatabaseReference mDatabaseUsers;

        DatabaseReference mCommentsReference;
        FirebaseAuth mAuth;

        TextView tvPrjName, tvQualification, tvNumVolun, tvActivity1, tvActivity2, tvCountry, tvCity, tvDate, tvDesc, tvPlace;

        public FairViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            mLikeBtn =  (ImageButton) mView.findViewById(R.id.like_btn);
            mJoinBtn =  (ImageButton) mView.findViewById(R.id.join_btn);
            mCommentBtn = (ImageButton) mView.findViewById(R.id.PostCommentBtn);
            mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");


            mDatabaseLike = FirebaseDatabase.getInstance().getReference().child("Likes");
            mDatabaseJoin = FirebaseDatabase.getInstance().getReference().child("Join");
            mCommentsReference = FirebaseDatabase.getInstance().getReference().child("post-comments");

            mAuth = FirebaseAuth.getInstance();

            mDatabaseLike.keepSynced(true);
            mDatabaseJoin.keepSynced(true);
            mCommentsReference.keepSynced(true);
            mDatabaseUsers.keepSynced(true);

            tvPrjName = (TextView) itemView.findViewById(R.id.project_name);
            tvQualification = (TextView) itemView.findViewById(R.id.qualification);
            tvNumVolun = (TextView) itemView.findViewById(R.id.numVolun);
            tvActivity1 = (TextView) itemView.findViewById(R.id.activity1);
            tvActivity2 = (TextView) itemView.findViewById(R.id.activity2);
            tvCountry = (TextView) itemView.findViewById(R.id.country);
            tvCity = (TextView) itemView.findViewById(R.id.city);
            tvDate = (TextView) itemView.findViewById(R.id.date);
            tvDesc = (TextView) itemView.findViewById(R.id.desc);
            tvPlace = (TextView) itemView.findViewById(R.id.place);
        }

        public void setLikeBtn(final String post_key){
            mDatabaseLike.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.child(post_key).hasChild(mAuth.getCurrentUser().getUid())){
                        mLikeBtn.setImageResource(R.mipmap.thumb_ic_green);
                    } else {
                        mLikeBtn.setImageResource(R.mipmap.thumb_ic_gray);
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }

        public void setJoinBtn(final String post_key){
            mDatabaseJoin.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.child(post_key).hasChild(mAuth.getCurrentUser().getUid())){
                        mJoinBtn.setImageResource(R.drawable.icn_schedule_green);
                    } else {
                        mJoinBtn.setImageResource(R.drawable.icn_schedule_gray);
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }

        public void setLikeNum(final String post_key){
            mDatabaseLike.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String A = String.valueOf(dataSnapshot.child(post_key).getChildrenCount());
                    TextView PostLikeNum = (TextView) mView.findViewById(R.id.PostLikeNum);
                    PostLikeNum.setText(A);}

                @Override
                public void onCancelled(DatabaseError databaseError) {}});
        }

        public void setJoinNum(final String post_key){
            mDatabaseJoin.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    TextView takepart = (TextView) mView.findViewById(R.id.takepart);
                    if(dataSnapshot.child(post_key).hasChild(mAuth.getCurrentUser().getUid())){
                        String A = String.valueOf(dataSnapshot.child(post_key).getChildrenCount() - 1);
                        takepart.setText("You and " + A + " others joined.");
                    } else {
                        String A = String.valueOf(dataSnapshot.child(post_key).getChildrenCount());
                        takepart.setText(A + " others joined.");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {}});
        }


        void setCommentNum(final String Post_Key){
            mCommentsReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String C = String.valueOf(dataSnapshot.child(Post_Key).getChildrenCount());
                    TextView PostCommentNum = (TextView) mView.findViewById(R.id.PostCommentNum);
                    PostCommentNum.setText(C);}

                @Override
                public void onCancelled(DatabaseError databaseError) {}});
        }

        public void setPost_time(Long post_time){
            TextView time = (TextView) mView.findViewById(R.id.post_time);
            SimpleDateFormat sfd = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            try {
                time.setText(sfd.format(new Date(post_time)));
            }catch (Exception e){}
        }

        public void setUsername(String username){
            TextView post_username = (TextView) mView.findViewById(R.id.post_username);
            post_username.setText(username);
        }
    }
}
