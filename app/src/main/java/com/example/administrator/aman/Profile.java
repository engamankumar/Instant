package com.example.administrator.aman;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import static android.app.Activity.RESULT_OK;

public class Profile extends Fragment {
ImageView propic;
    private TextView emailEditText;
    private TextView passEditText;
    private TextView nam,unam;
    private Button sp,pro;
    private TextView mobile;
    private EditText cuse;
    private StorageReference mStorageRef;
    private static final int GALLERY_INTENT=2;
private ProgressDialog mprogressdailog;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Profile");
        emailEditText = (TextView) getView().findViewById(R.id.emailp);
        unam = (TextView) getView().findViewById(R.id.userp);
        propic=(ImageView)getView().findViewById(R.id.propic);
        pro=(Button)getView().findViewById(R.id.pic);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mprogressdailog=new ProgressDialog(getActivity());

        nam = (TextView) getView().findViewById(R.id.namep);

        mobile = (TextView) getView().findViewById(R.id.mobilep);
        sp=(Button)getView().findViewById(R.id.change);
        cuse=(EditText)getView().findViewById(R.id.cuser);
        pro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, GALLERY_INTENT);

            }
        });



        DatabaseReference mo = FirebaseDatabase.getInstance().getReference("user").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("mobile");
        mo.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.getValue(String.class);
                mobile.setText(name);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        DatabaseReference un = FirebaseDatabase.getInstance().getReference("user").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("userId");
        un.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.getValue(String.class);
                unam.setText(name);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        String ema = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        emailEditText.setText(ema);

        DatabaseReference nm = FirebaseDatabase.getInstance().getReference("user").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("name");
        nm.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.getValue(String.class);
                nam.setText(name);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        mStorageRef = FirebaseStorage.getInstance().getReference("userpic");

        final StorageReference riversRef = mStorageRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("photo");
        mStorageRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("photo").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'

                Picasso.with(getActivity()).load(uri).fit().centerCrop().into(propic);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
              Toast.makeText(getActivity(),"No Profile pic ",Toast.LENGTH_LONG).show();
            }
        });

        sp.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        String use = cuse.getText().toString();
        if(use.length()>=3) {
        DatabaseReference mo = FirebaseDatabase.getInstance().getReference("user").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("mobile");
        mo.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final String name = dataSnapshot.getValue(String.class);
                DatabaseReference mo = FirebaseDatabase.getInstance().getReference("user").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("userId");
                mo.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String userId = dataSnapshot.getValue(String.class);
                        String use = cuse.getText().toString();
                        DatabaseReference mo = FirebaseDatabase.getInstance().getReference("username").child(name + userId);
                        mo.child("UserId").removeValue();
                        DatabaseReference c = FirebaseDatabase.getInstance().getReference("username").child(name + use);
                        c.child("UserId").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
                        DatabaseReference changeuser = FirebaseDatabase.getInstance().getReference("user").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("userId");
                        changeuser.setValue(use);


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

            Toast.makeText(getActivity(), "User Name Updated" , Toast.LENGTH_LONG).show();

        //if close
    }
    else
        {
            Toast.makeText(getActivity(), "Enter the User Name First " , Toast.LENGTH_LONG).show();


        }
    }
});

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GALLERY_INTENT && resultCode==RESULT_OK) {
            mprogressdailog.setMessage("Uploading");
            mprogressdailog.show();
            mStorageRef = FirebaseStorage.getInstance().getReference("userpic");
           Uri uri=data.getData();
            final StorageReference riversRef = mStorageRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("photo");

            riversRef.putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get a URL to the uploaded content
                            Toast.makeText(getActivity(), "Upload done", Toast.LENGTH_LONG).show();
                            mprogressdailog.dismiss();



                        }
                            // ...


                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                    // ...
                    Toast. makeText(getActivity(), "Error", Toast.LENGTH_LONG).show();

                }
            });

        }
        else {
            return;

        }
    }
}
