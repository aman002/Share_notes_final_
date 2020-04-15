package com.aman.sharenotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ShowPostActivity extends AppCompatActivity {
    ListView listView;
    DatabaseReference databaseReference;
    List<UploadPdf> uploadPdfs;

    ImageView nav1,nav2;

    int test = 0;


    String id = "user";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_post);


        Intent intent=getIntent();
        id = intent.getStringExtra("id");

        listView = findViewById(R.id.listview);
        uploadPdfs = new ArrayList<>();

        nav1= findViewById(R.id.Nav1);//we are going to use this for refresh and hide this when admin login
        nav2= findViewById(R.id.Nav2);

        ViewAllFiles();

        nav2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(ShowPostActivity.this,HomeActivity.class);
                startActivity(intent);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                UploadPdf uploadPdf = uploadPdfs.get(i);

                Toast.makeText(ShowPostActivity.this, "This is name" + uploadPdf.getFilename(), Toast.LENGTH_SHORT).show();

                try {
                    if(id.equals("admin"))
                    {
                        Toast.makeText(ShowPostActivity.this, "Admin Delete file " , Toast.LENGTH_SHORT).show();


//                    StorageReference photoRef = mFirebaseStorage.getReferenceFromUrl(mImageUrl);
//                    mFirebaseStorage is just "FirebaseStorage.getInstance()"

//                    mFirebaseDatabase!!.child(userId!!).removeValue()
//                    Toast.makeText(applicationContext, "Successfully deleted user", Toast.LENGTH_SHORT).show()



                        StorageReference deleteFile = FirebaseStorage.getInstance().getReferenceFromUrl(uploadPdf.getFile());
                        deleteFile.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(ShowPostActivity.this, "Previous File Deleted", Toast.LENGTH_SHORT).show();

                            }
                        });

                        databaseReference.child(uploadPdf.getFileRandomKey()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(ShowPostActivity.this, "Previous Data deleted ", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ShowPostActivity.this, "PRoblem ---- Eror", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
                catch (Exception e)
                {

                    test = 1;
                }

                if (test==1){
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse(uploadPdf.getFile()), "application/pdf");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    Intent newIntent = Intent.createChooser(intent, "Open File");
                    try {
                        startActivity(newIntent);
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(ShowPostActivity.this, "", Toast.LENGTH_SHORT).show();
                        // Instruct the user to install a PDF reader here, or something
                    }
                }


//                Intent intent= new Intent();
//                intent.setType(Intent.ACTION_VIEW);
//                intent.setData(Uri.parse(uploadPdf.getImage()));
//                startActivity(intent);
            }
        });


    }

    private void ViewAllFiles() {

        databaseReference = FirebaseDatabase.getInstance().getReference("PDF");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) { //Check for existens

                    for (DataSnapshot postsnapshot: dataSnapshot.getChildren())
                    {
                        UploadPdf uploadPdf = postsnapshot.getValue(UploadPdf.class);
                        uploadPdfs.add(uploadPdf);

                    }

                    String[] uploads = new String[uploadPdfs.size()];
                    for (int i=0; i<uploads.length;i++){

                        uploads[i] = uploadPdfs.get(i).getFilename();

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
                    Toast.makeText(ShowPostActivity.this, "Sorrry data not available", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ShowPostActivity.this, "Suck OFFF", Toast.LENGTH_SHORT).show();

            }
        });


    }
}



