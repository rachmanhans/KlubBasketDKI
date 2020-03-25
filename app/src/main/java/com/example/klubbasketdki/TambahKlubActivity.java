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

public class TambahKlubActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private Button InputLogo, UploadKlub;
    private EditText etNamaKlub, etLat, etLong, etLokasi, etTelepon, etInstagram, etDeskripsi;
    private ImageView logoview;
    private ProgressBar uProgressBar;

    private Uri mImageUri;
    private DatabaseReference mDatabaseRef;
    private StorageReference mStorageRef;


    private StorageTask mUploadTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_klub);

        InputLogo = findViewById(R.id.btn_logo);
        UploadKlub = findViewById(R.id.btn_upload);

        etNamaKlub = findViewById(R.id.et_nama_klub);
        etLat = findViewById(R.id.et_lat);
        etLong = findViewById(R.id.et_long);
        etLokasi = findViewById(R.id.et_lokasi);
        etTelepon = findViewById(R.id.et_telepon);
        etInstagram = findViewById(R.id.et_instagram);
        etDeskripsi = findViewById(R.id.et_deskripsi);
        logoview = findViewById(R.id.logoView);
        uProgressBar = findViewById(R.id.progress_bar);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Klub");
        mStorageRef = FirebaseStorage.getInstance().getReference("Klub");

        InputLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        UploadKlub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUploadTask != null && mUploadTask.isInProgress()) {
                    Toast.makeText(TambahKlubActivity.this, "An Upload is Still in Progress", Toast.LENGTH_SHORT).show();
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

            Picasso.with(this).load(mImageUri).into(logoview);
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

            uProgressBar.setVisibility(View.VISIBLE);
            uProgressBar.setIndeterminate(true);

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
                                            etLong.getText().toString(),
                                            etTelepon.getText().toString(),
                                            etInstagram.getText().toString());
                                    String uploadId = mDatabaseRef.push().getKey();
                                    mDatabaseRef.child(uploadId).setValue(upload);

                                    uProgressBar.setVisibility(View.INVISIBLE);
                                    openImagesActivity ();
                                }
                            });
                        }

        })
                    .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                uProgressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(TambahKlubActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                        uProgressBar.setProgress((int) progress);
                    }
                });
    } else {
        Toast.makeText(this, "You haven't Selected Any file selected", Toast.LENGTH_SHORT).show();
    }
}
                        private void openImagesActivity(){
                            Intent intent = new Intent(this, AdminHomeActivity.class);
                            startActivity(intent);
                        }


}
