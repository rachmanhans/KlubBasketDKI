package com.example.klubbasketdki;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.klubbasketdki.Model.KlubModel;
import com.example.klubbasketdki.Model.PelatihModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class EditPelatihActivity extends AppCompatActivity {

    private EditText namaPel_Edit, ketPel_Edit;
    private Button uploadImage_Btn, editBtn, cancelBtn;
    private ImageView imageView_edit;
    private ProgressBar loadProgressBar;
    private DatabaseReference mDatabaseRef;
    private StorageReference mStorageRef;
    private StorageTask mUploadTask;

    private Uri mImageUri;
    private static final int PICK_IMAGE_REQUEST = 1;

    private void initializeWidgets(){

        namaPel_Edit = findViewById(R.id.namaPelatih_et_edit);
        ketPel_Edit = findViewById(R.id.ketPelatih_et_edit);
        imageView_edit = findViewById(R.id.imageViewPel_edit);
        uploadImage_Btn = findViewById(R.id.btn_image_edit);
        editBtn = findViewById(R.id.btnPel_edit);
        cancelBtn = findViewById(R.id.btnPel_cancel_edit);
        loadProgressBar = findViewById(R.id.progress_barPel);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pelatih);


     String idKeys = getIntent().getExtras().getString("ID_KEY");

        initializeWidgets();
        Intent i = this.getIntent();
        String nama = i.getExtras().getString("NAMA_KEY");
        String keterangan = i.getExtras().getString("KETERANGAN_KEY");
        String imageURL = i.getExtras().getString("IMAGE_KEY");

        namaPel_Edit.setText(nama);
        ketPel_Edit.setText(keterangan);
        Picasso.with(this).load(imageURL).into(imageView_edit);


        mStorageRef = FirebaseStorage.getInstance().getReference("Klub");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Klub/"+idKeys);
        uploadImage_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUploadTask != null && mUploadTask.isInProgress()) {
                    Toast.makeText(EditPelatihActivity.this, "An Upload is Still in Progress", Toast.LENGTH_SHORT).show();
                } else {
                    uploadFile();
                }
            }
        });

    }
    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();

            Picasso.with(this).load(mImageUri).into(imageView_edit);
        }
    }
    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile() {
        if (mImageUri != null) {
            mStorageRef = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));

            loadProgressBar.setVisibility(View.VISIBLE);
            loadProgressBar.setIndeterminate(true);

            mUploadTask = mStorageRef.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            mStorageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri downloadPhotoUrl) {
                                    PelatihModel upload = new PelatihModel(

                                            namaPel_Edit.getText().toString().trim(),
                                            downloadPhotoUrl.toString(),
                                            ketPel_Edit.getText().toString());
                                   String idKey = getIntent().getExtras().getString("ID_KEY");
                                    mDatabaseRef.child(idKey)
                                            .setValue(upload);

//                                    String uploadId = mDatabaseRef.getKey();
//                                    mDatabaseRef.child(uploadId).setValue(upload);

                                    loadProgressBar.setVisibility(View.INVISIBLE);
                                    openImagesActivity ();
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            loadProgressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(EditPelatihActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            loadProgressBar.setProgress((int) progress);
                        }
                    });
        }else {
            Toast.makeText(this, "You haven't Selected Any file selected", Toast.LENGTH_SHORT).show();
        }
    }
    private void openImagesActivity(){
        Intent intent = new Intent(this, AdminViewActivity.class);
        startActivity(intent);






    }
}
