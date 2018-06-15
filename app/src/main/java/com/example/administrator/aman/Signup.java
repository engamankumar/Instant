package com.example.administrator.aman;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.administrator.aman.R.id.pusernames;

public class Signup extends BaseActivity implements View.OnClickListener {
    private EditText emailEditText;

    private EditText passEditText;
    private EditText name,unam;
    private Button sp;
    private EditText mobile;
    private EditText Confirm;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;



    private static final String TAG = "SignUp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        emailEditText = (EditText) findViewById(R.id.pemail);
        unam = (EditText) findViewById(pusernames);
        passEditText = (EditText) findViewById(R.id.ppassword2);
        name = (EditText) findViewById(R.id.prname);
        Confirm = (EditText) findViewById(R.id.pConpassword1);
        mobile = (EditText) findViewById(R.id.pmobile);
        sp = (Button) findViewById(R.id.pSign);
        sp.setOnClickListener(this);

    }

    @Override
    public void onStart() {
        super.onStart();

        // Check auth on Activity start
        if (mAuth.getCurrentUser() != null) {
            onAuthSuccess(mAuth.getCurrentUser());
        }
    }


    public void Signp()
    { final String email = emailEditText.getText().toString();
        final String password = passEditText.getText().toString();
        final String cpass = Confirm.getText().toString();
        final String mo=mobile.getText().toString();
        final String uname=unam.getText().toString();
        if (!isValidEmail(email)) {
            //Set error message for email field
            emailEditText.setError("Invalid Email");
        }


        if (!isValidPassword(password)) {
            //Set error message for password field
            passEditText.setError("Invalid password");

        }
        if (!isValidCPassword(cpass,password)) {
            //Set error message for password field
            passEditText.setError("Password didn't match");

        }
        if (!isValidmobile(mo)) {
            //Set error message for password field
            mobile.setError("Enter Correct mobile no.");

        }
        if (!isValiduname(uname)) {
            //Set error message for password field
            unam.setError("Enter uname.");

        }





        if( isValidEmail(email) && isValidPassword(password) && isValidmobile(mo) && isValidCPassword(cpass,password) && isValiduname(uname) )

        {


            showProgressDialog();


            mDatabase = FirebaseDatabase.getInstance().getReference();

            mAuth = FirebaseAuth.getInstance();
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d(TAG, "createUser:onComplete:" + task.isSuccessful());
                            hideProgressDialog();
                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (task.isSuccessful()) {
                                onAuthSuccess(task.getResult().getUser());
                            } else {
                                Toast.makeText(Signup.this, "Sign Up Failed",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });}

    }
    private void onAuthSuccess(FirebaseUser user) {
        String username = usernameFromEmail(user.getEmail());
        String na=name.getText().toString();
        String mo=mobile.getText().toString();
        String uname=unam.getText().toString();

        // Write new user
        writeNewUser(user.getUid(), na, user.getEmail(),mo,uname);

        // Go to MainActivity
        startActivity(new Intent(Signup.this, Sign.class));
        finish();
    }
    private boolean isValidmobile(String mo) {
        if (mo != null && mo.length()==10) {
            return true;
        }
        return false;
    }

    private boolean isValidCPassword(String cpass,String pass) {
        if (cpass != null && cpass!=pass) {
            return true;
        }
        return false;
    }
    private boolean isValiduname(String uname) {
        if (uname != null ) {
            return true;
        }

        return false;
    }



    private boolean isValidPassword(String pass) {
        if (pass != null && pass.length() >= 8) {
            return true;
        }
        return false;
    }
    private String usernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }
    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    // [START basic_write]
    private void writeNewUser(String userId, String name, String email,String mo, String uname) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference uref = database.getReference("username");
        DatabaseReference uRef1 = uref.child(mo+uname);

        uRef1.child("UserId").setValue(userId);
        DatabaseReference ref = database.getReference("user");
        DatabaseReference usersRef = ref.child(userId);

        usersRef.child("name").setValue(name);
        usersRef.child("userId").setValue(uname);
        usersRef.child("email").setValue(email);
        usersRef.child("mobile").setValue(mo);
        usersRef.child("person1").setValue("");
        usersRef.child("person2").setValue("");
        usersRef.child("person3").setValue("");
        usersRef.child("person4").setValue("");
        usersRef.child("person5").setValue("");


    }
    // [END basic_writ
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.pSign) {
            Signp();
        }
    }
}
