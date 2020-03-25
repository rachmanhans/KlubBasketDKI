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
import android.widget.Toast;

import com.example.klubbasketdki.Model.KlubModel;
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

public class EditKlubActivity extends AppCompatActivity {
    
    private EditText etNamaKlub, etLokasi, etLat, etLongi, etTelepon, etInstagram, etDeskripsi;
    private Button btnInputImg, btnEdit, btnCancel;
    private ImageView editViewImage;
    private DatabaseReference mDatabaseRef;
    private StorageReference mStorageRef;
    private ProgressBar loadProgressBar;
    private StorageTask mUploadTask;
    
    private Uri mImageUri;
    private static final int PICK_IMAGE_REQUEST = 1;
    
    private void initializeWidgets(){
        
        etNamaKlub = findViewById(R.id.et_edit_nama_klub);
        etLokasi = findViewById(R.id.et_edit_lokasi);
        etLat = findViewById(R.id.et_edit_lat);
        etLongi = findViewById(R.id.et_edit_long);
        etTelepon = findViewById(R.id.et_edit_telepon);
        etInstagram = findViewById(R.id.et_edit_instagram);
        etDeskripsi = findViewById(R.id.et_edit_deskripsi);
        editViewImage = findViewById(R.id.editLogoView);
        btnInputImg = findViewById(R.id.btn_logo_edit);
        btnEdit = findViewById(R.id.btn_edit);
        btnCancel = findViewById(R.id.btn_cancel_edit);
        loadProgressBar = findViewById(R.id.progress_bar_edit);
        
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_klub);
        String idKey = getIntent().getExtras().getString("ID_KEY");

        initializeWidgets();
        Intent i = this.getIntent();
        String nama = i.getExtras().getString("NAME_KEY");
        String lokasi = i.getExtras().getString("LOKASI_KEY");
        String lat = i.getExtras().getString("LATITUDE_KEY");
        String longi = i.getExtras().getString("LONGITUDE_KEY");
        String telepon = i.getExtras().getString("TELEPON_KEY");
        String instagram = i.getExtras().getString("INSTAGRAM_KEY");
        String deskripsi = i.getExtras().getString("DESKRIPSI_KEY");
        String imageURL = i.getExtras().getString("IMAGE_KEY");

        etNamaKlub.setText(nama);
        etLokasi.setText(lokasi);
        etLat.setText(lat);
        etLongi.setText(longi);
        etTelepon.setText(telepon);
        etInstagram.setText(instagram);
        etDeskripsi.setText(deskripsi);
        Picasso.with(this).load(imageURL).into(editViewImage);


        mStorageRef = FirebaseStorage.getInstance().getReference("Klub");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Klub/"+idKey);
        btnInputImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUploadTask != null && mUploadTask.isInProgress()) {
                    Toast.makeText(EditKlubActivity.this, "An Upload is Still " +
                            "in Progress", Toast.LENGTH_SHORT).show();
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

            Picasso.with(this).load(mImageUri).into(editViewImage);
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
                                    KlubModel upload = new KlubModel(

                                            etNamaKlub.getText().toString().trim(),
                                            downloadPhotoUrl.toString(),
                                            etDeskripsi.getText().toString(),
                                            etLokasi.getText().toString(),
                                            etLat.getText().toString(),
                                            etLongi.getText().toString(),
                                            etInstagram.getText().toString(),
                                            etTelepon.getText().toString());
//
                                    mDatabaseRef
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
                            Toast.makeText(EditKlubActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
