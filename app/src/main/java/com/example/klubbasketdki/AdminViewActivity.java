package com.example.klubbasketdki;

import android.content.Intent;
import android.renderscript.Sampler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.klubbasketdki.Adapter.KlubAdapter;
import com.example.klubbasketdki.Model.KlubModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class AdminViewActivity extends AppCompatActivity implements KlubAdapter.OnItemClickListener {

    private RecyclerView mRecyclerView;
    private KlubAdapter mAdapter;
    private ProgressBar mProgressBar;
    private FirebaseStorage mStorage;
    private DatabaseReference mDatabaseRef;
    private ValueEventListener mDBListener;
    private List<KlubModel> mKlubModels;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view);

        mRecyclerView = findViewById(R.id.rv_AdminView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mProgressBar = findViewById(R.id.pb_AdminView);
        mProgressBar.setVisibility(View.VISIBLE);

        mKlubModels = new ArrayList<>();
        mAdapter = new KlubAdapter(AdminViewActivity.this, mKlubModels);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(AdminViewActivity.this);

        mStorage = FirebaseStorage.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Klub");

        mDBListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mKlubModels.clear();

                for (DataSnapshot teacherSnapshot : dataSnapshot.getChildren()) {
                    KlubModel upload = teacherSnapshot.getValue(KlubModel.class);
                    upload.setKey(teacherSnapshot.getKey());
                    mKlubModels.add(upload);
                }
                mAdapter.notifyDataSetChanged();
                mProgressBar.setVisibility(View.GONE);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(AdminViewActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressBar.setVisibility(View.INVISIBLE);
            }
        });

    }

    public void onItemClick(int position) {
        KlubModel clickedKlubModel = mKlubModels.get(position);
        Intent intent = new Intent(this, AdminDetailKlub.class);
        intent.putExtra("NAME_KEY", clickedKlubModel.getName());
        intent.putExtra("LOKASI_KEY", clickedKlubModel.getLokasi());
        intent.putExtra("IMAGE_KEY", clickedKlubModel.getImageURL());
        intent.putExtra("DESKRIPSI_KEY", clickedKlubModel.getDescription());
        intent.putExtra("TELEPON_KEY", clickedKlubModel.getTelepon());
        intent.putExtra("INSTAGRAM_KEY", clickedKlubModel.getInstagram());
        intent.putExtra("ID_KEY", clickedKlubModel.getKey());
        startActivity(intent);
        Log.v("errorid", "ini datanya " + clickedKlubModel.getKey());
    }
    @Override
    public void onShowItemClick(int position) {
                KlubModel clickedKlubModel = mKlubModels.get(position);
//        String[] teacherData={clickedKlubModel.getName(), clickedKlubModel.getDescription(), clickedKlubModel.getImageUrl()};
//        openDetailActivity(teacherData);
//
        Intent intent = new Intent(this, EditKlubActivity.class);
        intent.putExtra("NAME_KEY", clickedKlubModel.getName());
        intent.putExtra("LOKASI_KEY", clickedKlubModel.getLokasi());
        intent.putExtra("IMAGE_KEY", clickedKlubModel.getImageURL());
        intent.putExtra("DESKRIPSI_KEY", clickedKlubModel.getDescription());
        intent.putExtra("TELEPON_KEY", clickedKlubModel.getTelepon());
        intent.putExtra("INSTAGRAM_KEY", clickedKlubModel.getInstagram());
        intent.putExtra("ID_KEY", clickedKlubModel.getKey());
        intent.putExtra("LATITUDE_KEY", clickedKlubModel.getLatitude());
        intent.putExtra("LONGITUDE_KEY", clickedKlubModel.getLongitude());

        startActivity(intent);
    }
    @Override
    public void onDeleteItemClick(int position) {
        KlubModel selectedItem = mKlubModels.get(position);
        final String selectedKey = selectedItem.getKey();

        StorageReference imageRef = mStorage.getReferenceFromUrl(selectedItem.getImageURL());
        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mDatabaseRef.child(selectedKey).removeValue();
                Toast.makeText(AdminViewActivity.this, "Item deleted", Toast.LENGTH_SHORT).show();
            }
        });

    }
    protected void onDestroy() {
        super.onDestroy();
        mDatabaseRef.removeEventListener(mDBListener);
    }
    }

