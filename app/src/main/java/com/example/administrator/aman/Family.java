package com.example.administrator.aman;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Administrator on 3/18/2017.
 */

public class Family extends Fragment {
    EditText fam1,fam2,fam3,fam4,fam5;
    Button sfam;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        return inflater.inflate(R.layout.fragment_family, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Family");
        fam1=(EditText)getView().findViewById(R.id.f1);
        fam2=(EditText)getView().findViewById(R.id.f2);
        fam3=(EditText)getView().findViewById(R.id.f3);
        fam4=(EditText)getView().findViewById(R.id.f4);
        fam5=(EditText)getView().findViewById(R.id.f5);
        sfam=(Button)getView().findViewById(R.id.fsubmit);
        sfam.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                String p1=fam1.getText().toString();
                String p2=fam2.getText().toString();
                String p3=fam3.getText().toString();
                String p4=fam4.getText().toString();
                String p5=fam5.getText().toString();
                DatabaseReference un = FirebaseDatabase.getInstance().getReference("user").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                un.child("person1").setValue(p1);
                un.child("person2").setValue(p2);
                un.child("person3").setValue(p3);
                un.child("person4").setValue(p4);
                un.child("person5").setValue(p5);
                Toast.makeText(getActivity(), "Information Saved", Toast.LENGTH_LONG).show();


            }
        });


    }
}
