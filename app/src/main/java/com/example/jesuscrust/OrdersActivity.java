package com.example.jesuscrust;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class OrdersActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.orders);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.orders_main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        LinearLayout miHome = findViewById(R.id.home);
        LinearLayout miMenu = findViewById(R.id.menu);
        LinearLayout miCart = findViewById(R.id.cart);
        LinearLayout miOrders = findViewById(R.id.orders);
        LinearLayout miProfile = findViewById(R.id.profile);


        miHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrdersActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
        miMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrdersActivity.this, MenuActivity.class);
                startActivity(intent);
            }
        });
        miCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrdersActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });
        miOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrdersActivity.this, OrdersActivity.class);
                startActivity(intent);
            }
        });
        miProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrdersActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });
    }
}
