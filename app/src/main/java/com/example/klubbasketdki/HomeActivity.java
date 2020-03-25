package com.example.klubbasketdki;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class HomeActivity extends AppCompatActivity {

    private Button btn_menu_login, btn_menu_list_klub, btn_menu_maps, btn_menu_info, btn_menu_exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btn_menu_login = findViewById(R.id.btn_menu_login);
        btn_menu_list_klub = findViewById(R.id.btn_menu_list_klub);
        btn_menu_maps = findViewById(R.id.btn_menu_maps);
        btn_menu_info = findViewById(R.id.btn_menu_info);
        btn_menu_exit = findViewById(R.id.btn_menu_exit);

        btn_menu_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( HomeActivity.this, LoginActivity.class);
                startActivity(intent);

            }
        }
        );

        btn_menu_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                System.exit(0);
            }
        });

        btn_menu_maps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("https://www.google.com/maps/dir/48.8276261,2.3350114/48.8476794,2.340595/48.8550395,2.300022/48.8417122,2.3028844"));
                startActivity(intent);
            }
        });

    }
}
