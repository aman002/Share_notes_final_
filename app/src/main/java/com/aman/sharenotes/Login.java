package com.aman.sharenotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    EditText email,password;
    Button login;
    ProgressDialog progressDialog;
    TextView nothaveacc ,forgetpassword,admin;

    private ImageView cloud1,star;
    Animation animCloud,animStar ;

    String logid="user";


    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        cloud1      = findViewById(R.id.cloud1);
        star        = findViewById(R.id.star);
        animCloud   = AnimationUtils.loadAnimation(this,R.anim.animcloud);
        animStar    = AnimationUtils.loadAnimation(this,R.anim.animstar);
        cloud1.startAnimation(animCloud);
        star.startAnimation(animStar);



        email = (EditText) findViewById(R.id.etUsername);
        password = (EditText) findViewById(R.id.etPassword);
        nothaveacc= findViewById(R.id.tvRegister);
        forgetpassword= findViewById(R.id.forgetpassword);
        admin =findViewById(R.id.tvAdmin);

        login=findViewById(R.id.btLogin);

        mAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Plz Wait");



        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();


            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            String uid = user.getUid();
            Intent intent = new Intent(Login.this,HomeActivity.class);
            intent.putExtra("full_name", name);
            startActivity(intent);
            finish();
        }

        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email.setHint("Number");
                email.setInputType(InputType.TYPE_CLASS_NUMBER);

                logid="admin";
                Toast.makeText(Login.this, " Admin", Toast.LENGTH_SHORT).show();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String emailid= email.getText().toString().trim();
                String pass= password.getText().toString().trim();


                if (logid.equals("user"))
                {
                    if (!Patterns.EMAIL_ADDRESS.matcher(emailid).matches())
                    {
                        //set error to email edittext
                        email.setError("Invalid Email");
                        email.setFocusable(true);
                    }
                    else
                    {
                        loginuser(emailid,pass);
                    }
                }
                else if (logid.equals("admin"))
                {
                    Toast.makeText(Login.this, "Admin Id login ", Toast.LENGTH_SHORT).show();

                    if (TextUtils.isEmpty(emailid))
                    {
                        Toast.makeText(Login.this, "Fill all detail", Toast.LENGTH_SHORT).show();
                    }

                    else if (TextUtils.isEmpty(pass))
                    {
                        Toast.makeText(Login.this, "Fill all detail", Toast.LENGTH_SHORT).show();
                    }

                    else if (emailid.length()>9)
                    {
                        //set error to email edittext
                        loginadmin(emailid,pass);

                    }
                    else
                    {
                        email.setError("Invalid Number");
                        email.setFocusable(true);
                    }
                }
                else
                {
                    Toast.makeText(Login.this, "Nothing in login id", Toast.LENGTH_SHORT).show();
                }


//                Intent intent = new Intent(MainActivity.this,RegisterActivity.class);
//                startActivity(intent);

            }
        });


        nothaveacc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Login.this,RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        forgetpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showrecoverpassword();

            }
        });


    }



    private void showrecoverpassword() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Recover password");


        LinearLayout linearLayout = new LinearLayout(this);



        final EditText emailet = new EditText(this);
        emailet.setHint("Enter email");
        emailet.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        emailet.setMinEms(16);


        linearLayout.addView(emailet);
        linearLayout.setPadding(10,10,10,10);

        builder.setView(linearLayout);


        builder.setPositiveButton("Recover", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String emailid = emailet.getText().toString().trim();
                beginRecovery(emailid);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        builder.create().show();


    }

    private void beginRecovery(String emailid) {
        progressDialog.show();
        mAuth.sendPasswordResetEmail(emailid)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful())
                        {
                            Toast.makeText(Login.this, "WAit Almost done", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            progressDialog.dismiss();
                            Toast.makeText(Login.this, "failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Login.this, "Fail"+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loginuser(String emailid, String pass)
    {
        progressDialog.show();
        mAuth.signInWithEmailAndPassword(emailid, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            FirebaseUser user = mAuth.getCurrentUser();
                            progressDialog.dismiss();
                            Intent intent = new Intent(Login.this,HomeActivity.class);
                            startActivity(intent);
                            finish();


                        } else {
                            // If sign in fails, display a message to the user.
                            progressDialog.dismiss();
                            Toast.makeText(Login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(Login.this, "Failed xyz "+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void loginadmin(final String number, final String pass)
    {
        progressDialog.show();

        if (number.equals("7303906045") && pass.equals("password"))
        {
            Toast.makeText(this, "Super Admin Login ", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();

            Intent intent = new Intent (  Login.this, SuperAdmin.class);
            startActivity(intent);
            //Intent
        }
        else
        {
            Toast.makeText(this, "Admin login in Process login", Toast.LENGTH_SHORT).show();

            final DatabaseReference RootRef;
            RootRef = FirebaseDatabase.getInstance().getReference().child("Admin");

            Toast.makeText(Login.this, "Admin child found", Toast.LENGTH_SHORT).show();

            RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {
                    if (dataSnapshot.child(number).exists())
                    {
                        Toast.makeText(Login.this, "Number exsists", Toast.LENGTH_SHORT).show();


                        String fpass= dataSnapshot.child(number).child("Password").getValue().toString();

                        Toast.makeText(Login.this, "Password Fetch", Toast.LENGTH_SHORT).show();


                        if (fpass.equals(pass))
                        {
                            Toast.makeText(Login.this, "Admin Loggin Done", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent (  Login.this, AdminActivity.class);
                            intent.putExtra("Number",number);
                            startActivity(intent);
                            progressDialog.dismiss();

                        }
                        else
                        {
                            Toast.makeText(Login.this, "Password is incorect ", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }

                    }
                    else
                    {
                        Toast.makeText(Login.this, "Create Account With This" + number + "Number", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError)
                {
                    progressDialog.dismiss();
                    Toast.makeText(Login.this, "Database eror", Toast.LENGTH_SHORT).show();
                }
            });

        }

    }




}

