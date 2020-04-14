package com.aman.sharenotes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity {

    TextView name;
    private FirebaseAuth mAuth;

    Button logout,view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        name= findViewById(R.id.tvMountain);
        logout = findViewById(R.id.btLogout);
        view = findViewById(R.id.btshowpost);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            String email = user.getEmail();
        name.setText(email);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth= FirebaseAuth.getInstance();
                mAuth.signOut();
                Intent i=new Intent(HomeActivity.this,Login.class);
                startActivity(i);
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i=new Intent(HomeActivity.this,ShowPostActivity.class);
                startActivity(i);
            }
        });

    }
}
