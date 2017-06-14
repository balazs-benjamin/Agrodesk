package org.aqins.agrodesk;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
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

public class BlogListFragment extends Fragment {

    private RecyclerView mBlogList;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private DatabaseReference mDatabase;

    private DatabaseReference mDatabaseLike;

    private boolean mProcessLike = false;

    public static BlogListFragment newInstance() {
        BlogListFragment fragment = new BlogListFragment();
        return fragment;
    }

    public BlogListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener(){

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if (firebaseAuth.getCurrentUser() == null){

                    Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(loginIntent);
                }

            }
        };

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Blog");
        mDatabaseLike = FirebaseDatabase.getInstance().getReference().child("Likes");

        mDatabaseLike.keepSynced(true);
        mDatabase.keepSynced(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_blog, container,
                false);

        mBlogList = (RecyclerView) rootView.findViewById(R.id.blog_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);

        mBlogList.setHasFixedSize(true);
        mBlogList.setLayoutManager(layoutManager);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthStateListener);
        FirebaseRecyclerAdapter<Blog,BlogViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Blog, BlogViewHolder>(
                Blog.class,
                R.layout.blog_row,
                BlogViewHolder.class,
                mDatabase

        ) {
            @Override
            protected void populateViewHolder(final BlogViewHolder viewHolder, final Blog model, int position) {

                final String post_key = getRef(position).getKey();

                viewHolder.setUsername(model.getUsername());

                viewHolder.setTitle(model.getTitle());
                viewHolder.setDesc(model.getDesc());
                viewHolder.setPost_time(model.getPost_time());
                viewHolder.setImage(getActivity().getApplicationContext(), model.getImage());

                viewHolder.setLikeNum(post_key);
                viewHolder.setCommentNum(post_key);
                viewHolder.setLikeBtn(post_key);

                viewHolder.mView.setOnClickListener(new RecyclerView.OnClickListener(){
                    @Override
                     public void onClick(View view) {

                       //Toast.makeText(BlogListFragment.this, post_key, Toast.LENGTH_LONG).show();

                        Intent singleBlogIntent = new Intent(getActivity(), BlogSingleActivity.class);
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
            }
        };

        mBlogList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class BlogViewHolder extends RecyclerView.ViewHolder{

        View mView;

        ImageButton mCommentBtn;
        ImageButton mLikeBtn;
        DatabaseReference mDatabaseLike;
        DatabaseReference mDatabaseUsers;

        DatabaseReference mCommentsReference;
        FirebaseAuth mAuth;


        public BlogViewHolder(View itemView) {
            super(itemView);

            mView = itemView;

            mLikeBtn =  (ImageButton) mView.findViewById(R.id.like_btn);
            mCommentBtn = (ImageButton) mView.findViewById(R.id.PostCommentBtn);
            mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");


            mDatabaseLike = FirebaseDatabase.getInstance().getReference().child("Likes");
            mCommentsReference = FirebaseDatabase.getInstance().getReference()
                    .child("post-comments");

            mAuth = FirebaseAuth.getInstance();

            mDatabaseLike.keepSynced(true);
            mCommentsReference.keepSynced(true);
            mDatabaseUsers.keepSynced(true);

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

        public void setLikeNum(final String post_key){
            mDatabaseLike.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String A = String.valueOf(dataSnapshot.child(post_key).getChildrenCount());
                    TextView PostLikeNum = (TextView) mView.findViewById(R.id.PostLikeNum);
                    PostLikeNum.setText(A);}

                @Override
                public void onCancelled(DatabaseError databaseError) {}});}


        void setCommentNum(final String Post_Key){
            mCommentsReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String C = String.valueOf(dataSnapshot.child(Post_Key).getChildrenCount());
                    TextView PostCommentNum = (TextView) mView.findViewById(R.id.PostCommentNum);
                    PostCommentNum.setText(C);}

                @Override
                public void onCancelled(DatabaseError databaseError) {}});}




        public void setTitle(String title){

            TextView post_title = (TextView) mView.findViewById(R.id.post_title);
            post_title.setText(title);
        }

        public void setPost_time(Long post_time){
            TextView time = (TextView) mView.findViewById(R.id.post_time);
            SimpleDateFormat sfd = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            try {
                time.setText(sfd.format(new Date(post_time)));
            }catch (Exception e){}
        }





        public void setDesc(String desc){
            TextView post_desc = (TextView) mView.findViewById(R.id.post_desc);
            post_desc.setText(desc);

        }

        public void setUsername(String username){
            TextView post_username = (TextView) mView.findViewById(R.id.post_username);
            post_username.setText(username);


        }
        public void setImage(final Context ctx, final String image){
            final ImageView post_image = (ImageView) mView.findViewById(R.id.post_image);
            Picasso.with(ctx).load(image).resize(600,400).centerCrop().networkPolicy(NetworkPolicy.OFFLINE).into(post_image, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {

                    Picasso.with(ctx).load(image).resize(600,400).centerCrop().placeholder(R.drawable.cache)
                            .into(post_image);
                }
            });

        }
    }

}
