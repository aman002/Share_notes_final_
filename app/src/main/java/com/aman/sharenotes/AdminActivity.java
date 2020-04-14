package com.aman.sharenotes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AdminActivity extends AppCompatActivity {

    private String  name, Description , subject,  saveCurrentDate, saveCurrentTime,admin="admin";
    private Button AddNewFile, viewpdf;
    private ImageView InputFile;
    private EditText InputFileName, InputFileDescription, InputSubjectName;
    private static final int GalleryPick = 1;
    private Uri PdfUri;
    private String FileRandomKey, DownloadPdfUrl;
    private StorageReference FileRef;
    private DatabaseReference productRef;


    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);


        FileRef = FirebaseStorage.getInstance().getReference().child("PDF");
        productRef = FirebaseDatabase.getInstance().getReference().child("PDF");


        InputFileName = (EditText) findViewById(R.id.File_name);
        InputFileDescription = (EditText) findViewById(R.id.File_description);
        InputSubjectName = (EditText) findViewById(R.id.Subject_Name);
        InputFile = (ImageView) findViewById(R.id.upload_file);
        AddNewFile = (Button) findViewById(R.id.add_file);
        viewpdf = (Button) findViewById(R.id.viewpdf);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Plz Wait");



        InputFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                OpenGallery();
            }
        });


        AddNewFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                validateProductData();
            }
        });

        viewpdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(AdminActivity.this, ShowPostActivity.class);
                intent.putExtra("id", admin);
                startActivity(intent);
            }
        });



    }


    private void OpenGallery()
    {
        Intent galleryIntent =new Intent();
        galleryIntent.setType("application/pdf");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(galleryIntent, GalleryPick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == GalleryPick && resultCode ==RESULT_OK && data !=null )
        {
            PdfUri = data.getData();

        }
        else
        {
            Toast.makeText(AdminActivity.this, "Failed ", Toast.LENGTH_SHORT).show();
        }

    }

    private void validateProductData()
    {
        Description = InputFileDescription.getText().toString();
        subject = InputSubjectName.getText().toString();
        name = InputFileName.getText().toString();

        progressDialog.show();

        if (PdfUri==null)
        {
            Toast.makeText(this, "Select pdf", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Description))
        {
            Toast.makeText(this, "Select pdf", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(subject))
        {
            Toast.makeText(this, "Price", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(name))
        {
            Toast.makeText(this, "name", Toast.LENGTH_SHORT).show();
        }

        else
        {
            StoreProductDetail();
        }

    }

    private void StoreProductDetail()
    {


        Calendar calendar= Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, YYYY");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        FileRandomKey = saveCurrentDate + saveCurrentTime;

        final StorageReference filepath = FileRef.child(PdfUri.getLastPathSegment() + FileRandomKey + ".pdf");

        final UploadTask uploadtask = filepath.putFile(PdfUri);

        uploadtask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                String message = e.toString();
                Toast.makeText(AdminActivity.this, "Error "+ message, Toast.LENGTH_SHORT).show();

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AdminActivity.this, "Pdf Uploaded succesFully", Toast.LENGTH_SHORT).show();

                Task<Uri> urlTask = uploadtask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception
                    {
                        if (!task.isSuccessful())
                        {
                            throw task.getException();
                        }

                        DownloadPdfUrl = filepath.getDownloadUrl().toString();
                        return filepath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task)
                    {
                        if (task.isSuccessful())
                        {
                            DownloadPdfUrl = task.getResult().toString();
                            Toast.makeText(AdminActivity.this, "Successs Url", Toast.LENGTH_SHORT).show();

                            SaveProductInfoToDataBase();
                        }
                    }
                });
            }
        });
    }


    private void SaveProductInfoToDataBase()
    {
        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("FileRandomKey",FileRandomKey);
        productMap.put("date",saveCurrentDate);
        productMap.put("time",saveCurrentTime);
        productMap.put("description",Description);
        productMap.put("File",DownloadPdfUrl);
        productMap.put("Filename",name);

        productRef.child(FileRandomKey).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            Toast.makeText(AdminActivity.this, "Final finish", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();


//                            Intent intent = new Intent(AdminActivity.this, SuperAdmin.class);
//                            startActivity(intent);

                        }
                        else {

                            String Message= task.getException().toString();
                            progressDialog.dismiss();
                            Toast.makeText(AdminActivity.this, "Faildes: "+ Message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }


}
