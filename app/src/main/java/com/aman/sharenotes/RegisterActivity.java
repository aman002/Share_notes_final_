package com.aman.sharenotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Patterns;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private ImageView cloud1,star;
    Animation animCloud,animStar ;

    EditText email,password,username;
    Button register;
    ProgressDialog progressDialog;
    TextView alreadyacc;

    String id="user";

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        cloud1      = findViewById(R.id.cloud1);
        star        = findViewById(R.id.star);
        animCloud   = AnimationUtils.loadAnimation(this,R.anim.animcloud);
        animStar    = AnimationUtils.loadAnimation(this,R.anim.animstar);
        cloud1.startAnimation(animCloud);
        star.startAnimation(animStar);

        email = (EditText) findViewById(R.id.etUsername);
        username = findViewById(R.id.etUsername1);
        password = (EditText) findViewById(R.id.etPassword);
        alreadyacc= findViewById(R.id.tvLogin);

        register=findViewById(R.id.btReg);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Plz Wait");


        try {
            Intent intent=getIntent();
            id = intent.getStringExtra("regid");

            if (id.equals("admin"))
            {
                Toast.makeText(RegisterActivity.this, "This is Admin block", Toast.LENGTH_SHORT).show();
                email.setHint("Number");
                email.setInputType(InputType.TYPE_CLASS_NUMBER);
            }
        }
        catch (Exception e)
        {

            Toast.makeText(this, "This exception", Toast.LENGTH_SHORT).show();
        }




        mAuth = FirebaseAuth.getInstance();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mailid= email.getText().toString().trim();
                String pass= password.getText().toString().trim();
                String name = username.getText().toString().trim();

                try {
                    if (id.equals("admin"))
                    {
                        registeradmin(mailid,pass,name);
                    }
                }
                catch (Exception e)
                {
                    Toast.makeText(RegisterActivity.this, "Second Exception", Toast.LENGTH_SHORT).show();
                }
                int i=1;

                if (i==1)
                {
                    //register id == user


                    if (!Patterns.EMAIL_ADDRESS.matcher(mailid).matches())
                    {
                        //set error to email edittext
                        email.setError("Invalid Email");
                        email.setFocusable(true);



                    }
                    else if(password.length()<6) {

                        //set error to email edittext
                        password.setError("Password length at least 6 char");
                        password.setFocusable(true);

                    }
                    else
                    {
                        registerUser(mailid,pass);
                    }

                    //end
                }




//                Intent intent = new Intent(MainActivity.this,RegisterActivity.class);
//                startActivity(intent);

            }
        });

        alreadyacc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(RegisterActivity.this,Login.class);
                startActivity(intent);

            }
        });


    }

    private void registeradmin(final String emailid, final String pass, final String name) {

        progressDialog.show();

        //admin register

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference().child("Admin");

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (!(dataSnapshot.child(emailid).exists()))
                {
                    HashMap<String, Object> usersdataMap = new HashMap<>();
                    usersdataMap.put("Name",name);
                    usersdataMap.put("Password",pass);
                    usersdataMap.put("Number",emailid);


                    RootRef.child(emailid).updateChildren(usersdataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {
                            if (task.isSuccessful())
                            {
                                Toast.makeText(RegisterActivity.this, "Acoount Created ", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();

                                Intent intent = new Intent ( RegisterActivity.this, AdminActivity.class);
                                startActivity(intent);
                            }
                            else
                            {
                                progressDialog.dismiss();
                                Toast.makeText(RegisterActivity.this, "Network Eror Plz try again", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();

                Toast.makeText(RegisterActivity.this, "Cancelledddddddd", Toast.LENGTH_SHORT).show();
            }
        });



        // end

    }


    private void registerUser(String emailid, String pass)
    {
        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(emailid,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    progressDialog.dismiss();
                    FirebaseUser currentUser = mAuth.getCurrentUser();
//                            updateUI(currentUser);
                    Toast.makeText(RegisterActivity.this, "Registered...\n"+currentUser.getEmail(), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this,ShowPostActivity.class));
                    finish();


                } else {
                    // If sign in fails, display a message to the user.
                    progressDialog.dismiss();
                    Toast.makeText(RegisterActivity.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(RegisterActivity.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }





}















