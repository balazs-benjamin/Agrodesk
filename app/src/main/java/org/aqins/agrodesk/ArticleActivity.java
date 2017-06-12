package org.aqins.agrodesk;

        import android.app.ProgressDialog;
        import android.content.Intent;
        import android.net.Uri;
        import android.support.annotation.NonNull;
        import android.support.design.widget.FloatingActionButton;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.text.TextUtils;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.ImageButton;

        import com.google.android.gms.tasks.OnCompleteListener;
        import com.google.android.gms.tasks.OnSuccessListener;
        import com.google.android.gms.tasks.Task;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.auth.FirebaseUser;
        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.database.ServerValue;
        import com.google.firebase.database.ValueEventListener;
        import com.google.firebase.storage.FirebaseStorage;
        import com.google.firebase.storage.StorageReference;
        import com.google.firebase.storage.UploadTask;

public class ArticleActivity extends AppCompatActivity {


    private EditText mPostDesc;
    private Button mSubmitBtn;

    private static final String REQUIRED = "Message vide non autorisé";
    private DatabaseReference mDatabase;

    private ProgressDialog mProgress;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private DatabaseReference mDatabaseUser;

    private FloatingActionButton mSubmitArticle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        mAuth = FirebaseAuth.getInstance();

        mCurrentUser =mAuth.getCurrentUser();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Blog");

        mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());


        mPostDesc = (EditText) findViewById(R.id.descField);

        mSubmitBtn = (Button) findViewById(R.id.submitBtn);

        mProgress = new ProgressDialog(this);



        mSubmitArticle = (FloatingActionButton) findViewById(R.id.fab_submit_post);

        mSubmitArticle.setOnClickListener(new View.OnClickListener() {
                                              @Override
                                              public void onClick(View view) {
                                                  startActivity(new Intent(ArticleActivity.this, PostActivity.class));
                                              }
                                          });


        mSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPosting();
            }
        });


    }

    private void startPosting() {

        mProgress.setMessage("En cours de publication sur Agrodesk...");


        final String desc_val = mPostDesc.getText().toString().trim();

        if(!TextUtils.isEmpty(desc_val) ) {
            mProgress.show();


                    final DatabaseReference newPost = mDatabase.push();
                    mDatabaseUser.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {


                            newPost.child("desc").setValue(desc_val);

                            newPost.child("uid").setValue(mCurrentUser.getUid());
                            newPost.child("post_time").setValue(ServerValue.TIMESTAMP);
                            newPost.child("username").setValue(dataSnapshot.child("name").getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()) {

                                        startActivity(new Intent(ArticleActivity.this, MainActivity.class));
                                    }
                                }
                            });

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {


                        }
                    });

                    mProgress.dismiss();

            }
        else {
            mPostDesc.setError(REQUIRED);
            return;
        }

        }



}
