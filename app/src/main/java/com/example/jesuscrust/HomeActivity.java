package com.example.jesuscrust;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HomeActivity extends AppCompatActivity {

    private ImageView notificationIcon;
    private Spinner toolbarSpinner;
    private boolean isSpinnerInitialized = false;

    private ArrayList<String> opcionesList;
    private ArrayAdapter<String> adapter;

    private OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        LinearLayout miHome = findViewById(R.id.home);
        LinearLayout miMenu = findViewById(R.id.menu);
        LinearLayout miCart = findViewById(R.id.cart);
        LinearLayout miOrders = findViewById(R.id.orders);
        LinearLayout miProfile = findViewById(R.id.profile);

        miHome.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, HomeActivity.class);
            startActivity(intent);
        });
        miMenu.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, MenuActivity.class);
            startActivity(intent);
        });
        miCart.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, CartActivity.class);
            startActivity(intent);
        });
        miOrders.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, OrdersActivity.class);
            startActivity(intent);
        });
        miProfile.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
            startActivity(intent);
        });

        notificationIcon = findViewById(R.id.toolbar_notification_icon);
        toolbarSpinner = findViewById(R.id.toolbar_spinner);

        opcionesList = new ArrayList<>();

        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, opcionesList);
        toolbarSpinner.setAdapter(adapter);

        cargarCategoriasDesdeAPI();

        notificationIcon.setOnClickListener(v -> toolbarSpinner.performClick());

        toolbarSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!isSpinnerInitialized) {
                    if (!opcionesList.isEmpty()) {
                        isSpinnerInitialized = true;
                    }
                    return;
                }

                String opcion = opcionesList.get(position);
                Toast.makeText(HomeActivity.this, "Seleccionaste: " + opcion, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void cargarCategoriasDesdeAPI() {

        String url = "http://10.0.2.2/api_tienda/getCategorias.php";

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> {
                    Toast.makeText(HomeActivity.this, "Error de conexión con el servidor", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String respuesta = response.body().string();

                runOnUiThread(() -> {
                    try {
                        JSONArray jsonResp = new JSONArray(respuesta);

                        opcionesList.clear();

                        for (int i = 0; i < jsonResp.length(); i++) {
                            opcionesList.add(jsonResp.getString(i));
                        }

                        adapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        Toast.makeText(HomeActivity.this, "Error al procesar respuesta", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}