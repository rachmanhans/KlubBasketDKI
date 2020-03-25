package com.example.klubbasketdki;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AdminHomeActivity extends AppCompatActivity {

    private Button btn_add, btn_list ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        btn_add = findViewById(R.id.btn_addKlub);
        btn_list = findViewById(R.id.btn_lvAdmin);

                btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( AdminHomeActivity.this, TambahKlubActivity.class);
                startActivity(intent);
            }
        });
                btn_list.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(AdminHomeActivity.this, AdminViewActivity.class);
                        startActivity(intent);

                    }
                });


    }
}
