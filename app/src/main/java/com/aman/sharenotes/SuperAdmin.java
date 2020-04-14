package com.aman.sharenotes;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SuperAdmin extends AppCompatActivity {

    Button newAdmin;
    TextView text;
    String admin = "admin";

    ListView listView;
    DatabaseReference databaseReference;
    List<adminlist> adminlist1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_admin);

        newAdmin = findViewById(R.id.newadmin);
        text = findViewById(R.id.textview);

        newAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SuperAdmin.this,RegisterActivity.class);
                intent.putExtra("regid", admin);
                startActivity(intent);
            }
        });


        listView = findViewById(R.id.listview);
        adminlist1 = new ArrayList<>();
        
        viewalladmin();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adminlist admintodelete = adminlist1.get(position);

                databaseReference.child(admintodelete.getNumber()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {


                        Toast.makeText(SuperAdmin.this, "Previous Data deleted ", Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SuperAdmin.this, "PRoblem ---- Eror", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

    }

    private void viewalladmin()
    {

        databaseReference = FirebaseDatabase.getInstance().getReference("Admin");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) { //Check for existens

                    for (DataSnapshot postsnapshot: dataSnapshot.getChildren())
                    {
                        adminlist adminlist2 = postsnapshot.getValue(adminlist.class);
                        adminlist1.add(adminlist2);

                    }

                    String[] uploads = new String[adminlist1.size()];
                    for (int i=0; i<uploads.length;i++)
                    {

                        uploads[i] = adminlist1.get(i).getName();

                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,uploads)
                    {

                        @Override
                        public View getView(int position,  View convertView,  ViewGroup parent) {

                            View view = super.getView(position, convertView, parent);

                            TextView mytext = (TextView) view.findViewById(android.R.id.text1);
                            mytext.setTextColor(Color.BLACK);


                            return view;
                        }
                    };
                    listView.setAdapter(adapter);

                }

                else{
                    Toast.makeText(SuperAdmin.this, "Sorrry data not available", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(SuperAdmin.this, "Suck OFFF", Toast.LENGTH_SHORT).show();

            }
        });


    }
        
        

}