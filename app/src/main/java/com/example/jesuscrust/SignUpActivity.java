package com.example.jesuscrust;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SignUpActivity extends AppCompatActivity {

    // ...existing code...
    private Button btnlogin;
    private Button btnsignUp;
    private Button btnAnonymous;

    // UI fields for registration
    private EditText etName, etEmail, etPassword, etPasswordConfirm;

    private OkHttpClient client;
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private static final String REGISTER_URL = "http://10.0.2.2/api_tienda/register.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // ...existing code...
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_register), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnlogin = findViewById(R.id.btnLogin);
        btnsignUp = findViewById(R.id.btnSignUp);


        etName = findViewById(R.id.etEmail);
        etEmail = findViewById(R.id.correo);
        etPassword = findViewById(R.id.etPassword);
        etPasswordConfirm = null;

        client = new OkHttpClient();

        btnlogin.setOnClickListener(v -> {
            Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
            startActivity(intent);
        });

        btnsignUp.setOnClickListener(v -> {
            String name = etName != null ? etName.getText().toString().trim() : "";
            String email = etEmail != null ? etEmail.getText().toString().trim() : "";
            String pass = etPassword != null ? etPassword.getText().toString() : "";
            String pass2 = (etPasswordConfirm != null) ? etPasswordConfirm.getText().toString() : pass;

            if (name.isEmpty() || email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Email inválido", Toast.LENGTH_SHORT).show();
                return;
            }
            if (pass.length() < 6) {
                Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
                return;
            }
            if (etPasswordConfirm != null && !pass.equals(pass2)) {
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                return;
            }
            if (etPasswordConfirm == null) {
                Toast.makeText(this, "Nota: campo de confirmación no encontrado, se omitirá la comprobación.", Toast.LENGTH_SHORT).show();
            }

            try {
                JSONObject json = new JSONObject();
                json.put("name", name);
                json.put("email", email);
                json.put("password", pass);
                postRegister(json.toString());
                btnsignUp.setEnabled(false);
                btnsignUp.setText("Registrando...");
            } catch (Exception e) {
                Toast.makeText(this, "Error interno", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private EditText safeFindEditText(int[] ids) {
        for (int id : ids) {
            try {
                EditText e = findViewById(id);
                if (e != null) return e;
            } catch (Exception ignored) {}
        }
        return null;
    }

    private void postRegister(String jsonBody) {
        RequestBody body = RequestBody.create(jsonBody, JSON);
        Request request = new Request.Builder()
                .url(REGISTER_URL)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> {
                    btnsignUp.setEnabled(true);
                    btnsignUp.setText("Sign up");
                    Toast.makeText(SignUpActivity.this, "Error de conexión: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
            }

            @Override public void onResponse(Call call, Response response) throws IOException {
                final String resp = response.body() != null ? response.body().string() : "";
                runOnUiThread(() -> {
                    btnsignUp.setEnabled(true);
                    btnsignUp.setText("Sign up");
                    try {
                        JSONObject o = new JSONObject(resp);
                        boolean ok = o.optBoolean("success", false);
                        String msg = o.optString("message", resp);
                        Toast.makeText(SignUpActivity.this, msg, Toast.LENGTH_LONG).show();
                        if (ok) {
                            if (etName != null) etName.setText("");
                            if (etEmail != null) etEmail.setText("");
                            if (etPassword != null) etPassword.setText("");
                            if (etPasswordConfirm != null) etPasswordConfirm.setText("");
                            // Redirigir a MainActivity cuando el registro sea correcto
                            Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }
                    } catch (Exception e) {
                        Toast.makeText(SignUpActivity.this, "Respuesta inválida: " + resp, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
}