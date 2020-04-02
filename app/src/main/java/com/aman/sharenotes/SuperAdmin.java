package com.aman.sharenotes;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SuperAdmin extends AppCompatActivity {

    Button newAdmin;
    String admin = "admin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_admin);

        newAdmin = findViewById(R.id.newadmin);

        newAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SuperAdmin.this,RegisterActivity.class);
                intent.putExtra("regid", admin);
                startActivity(intent);
            }
        });

    }
}