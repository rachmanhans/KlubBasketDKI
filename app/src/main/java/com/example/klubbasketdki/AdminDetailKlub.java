package com.example.klubbasketdki;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.klubbasketdki.Adapter.KlubAdapter;
import com.example.klubbasketdki.Adapter.PelatihAdapter;
import com.example.klubbasketdki.Model.KlubModel;
import com.example.klubbasketdki.Model.PelatihModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AdminDetailKlub extends AppCompatActivity  implements PelatihAdapter.OnItemClickListener{

    private TextView namaKlub_tv, deskripsi_tv, lokasi_tv, telepon_tv, instagram_tv;
    private ImageView pelatih_iv;
    private String idKey;
    private RecyclerView pelatih_rv;
    private List<PelatihModel> mPelatihModel;
    private PelatihAdapter mPelatihAdapter;
    private DatabaseReference mDatabaseRef;
    private ValueEventListener mDBListener;
    private FirebaseStorage mStorage;

        private void initializeWidgets() {
            namaKlub_tv = findViewById(R.id.txtnama_klub);
            pelatih_iv = findViewById(R.id.pelDetailImageView);
            deskripsi_tv = findViewById(R.id.txtdeskripsi);
            lokasi_tv = findViewById(R.id.txtlokasi);
            telepon_tv = findViewById(R.id.txttelepon);
            instagram_tv = findViewById(R.id.txtinstagram);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_admin_detail_klub);



//            sliderView = (SliderView) findViewById(R.id.sliderView);
//            mLinearLayout = (LinearLayout) findViewById(R.id.pagesContainer);
//            setupSlider();
//        }
//
//
//    private void setupSlider() {
//        sliderView.setDurationScroll(800);
//        List<Fragment> fragments = new ArrayList<>();
//        fragments.add(FragmentSlider.newInstance("http://www.menucool.com/slider/prod/image-slider-1.jpg"));
//        fragments.add(FragmentSlider.newInstance("http://www.menucool.com/slider/prod/image-slider-2.jpg"));
//        fragments.add(FragmentSlider.newInstance("http://www.menucool.com/slider/prod/image-slider-3.jpg"));
//        fragments.add(FragmentSlider.newInstance("http://www.menucool.com/slider/prod/image-slider-4.jpg"));
//
//        mSliderAdapter = new SliderPageAdapter(getSupportFragmentManager(), fragments);
//        sliderView.setAdapter(mSliderAdapter);
//        mIndicator = new SliderIndicator(this, mLinearLayout, sliderView, R.drawable.indicator_circle);
//        mIndicator.setPageCount(fragments.size());
//        mIndicator.show();
//
//
//
//        // atas slider code

            initializeWidgets();

            Intent i = this.getIntent();
            String nama = i.getExtras().getString("NAME_KEY");
            Log.v("errorid", "ini datanya di detail "+nama);
            String deskripsi = i.getExtras().getString("DESKRIPSI_KEY");
            String lokasi = i.getExtras().getString("LOKASI_KEY");
            Log.v("errorid", "ini datanya di detail "+lokasi);
            String instagram = i.getExtras().getString("INSTAGRAM_KEY");
            String telepon = i.getExtras().getString("TELEPON_KEY");
            String imageURL=i.getExtras().getString("IMAGE_KEY");
            idKey = i.getExtras().getString("ID_KEY");
            Log.v("errorid", "ini datanya di detail "+idKey);
            namaKlub_tv.setText(nama);
            lokasi_tv.setText(lokasi);
            deskripsi_tv.setText(deskripsi);
            telepon_tv.setText(telepon);
            instagram_tv.setText(instagram);
            Picasso.with(this)
                    .load(imageURL)
                    .placeholder(R.drawable.dw_pic_klubadapter)
                    .fit()
                    .centerCrop()
                    .into(pelatih_iv);

            pelatih_rv = findViewById(R.id.rv_pelatih);
            pelatih_rv.setHasFixedSize(true);
            pelatih_rv.setLayoutManager(new LinearLayoutManager(this));

            mPelatihModel = new ArrayList<>();
            mPelatihAdapter =  new  PelatihAdapter(AdminDetailKlub.this, mPelatihModel);
            pelatih_rv.setAdapter(mPelatihAdapter);
            mPelatihAdapter.setOnItemClickListener(AdminDetailKlub.this);

            mStorage = FirebaseStorage.getInstance();
            mDatabaseRef = FirebaseDatabase.getInstance().getReference("Klub/"+idKey+"/Pelatih");

            mDBListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    mPelatihModel.clear();

                    for (DataSnapshot teacherSnapshot : dataSnapshot.getChildren()) {
                        PelatihModel upload = teacherSnapshot.getValue(PelatihModel.class);
                        upload.setKey(teacherSnapshot.getKey());
                        mPelatihModel.add(upload);
                    }
                    mPelatihAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }


    @Override
    public void onItemClick(int position) {

    }

    public void onShowItemClick(int position) {
        PelatihModel clickedPelatihModel = mPelatihModel.get(position);
//        String[] teacherData={clickedPelatihModel.getName(), clickedPelatihModel.getDescription(), clickedPelatihModel.getImageUrl()};
//        openDetailActivity(teacherData);
//
        Intent intent = new Intent(this, EditPelatihActivity.class);
        intent.putExtra("NAMA_KEY", clickedPelatihModel.getNama());
        intent.putExtra("KETERANGAN_KEY", clickedPelatihModel.getKeterangan());
        intent.putExtra("IMAGE_KEY", clickedPelatihModel.getImageURL());
        intent.putExtra("ID_KEY", clickedPelatihModel.getKey());

        startActivity(intent);
    }

    public void onDeleteItemClick(int position) {
        PelatihModel selectedItem = mPelatihModel.get(position);
        final String selectedKey = selectedItem.getKey();

        StorageReference imageRef = mStorage.getReferenceFromUrl(selectedItem.getImageURL());
        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mDatabaseRef.child(selectedKey).removeValue();
                Toast.makeText(AdminDetailKlub.this, "Item deleted", Toast.LENGTH_SHORT).show();
            }
        });

    }
        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Membaca file menu dan menambahkan isinya ke action bar jika ada.
            getMenuInflater().inflate(R.menu.menu_detail, menu);
            return true;
        }
        public boolean onOptionsItemSelected(MenuItem item)
        {
            switch (item.getItemId()){
                case R.id.addpelatih:
                    Intent intent = new Intent(AdminDetailKlub.this, TambahPelatihActivity.class );
                    intent.putExtra("ID_KEY", idKey);
                    Log.v("errorid", "ini datanya di detail "+idKey);
                    startActivity(intent);
                    return true;

            }
            return false;
    }







}