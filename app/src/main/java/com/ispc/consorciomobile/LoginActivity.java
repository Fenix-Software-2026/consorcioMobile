
package com.ispc.consorciomobile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText inputUsuario;
    private EditText inputPassword;
    private Button buttonLogin;

    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        inputUsuario = findViewById(R.id.inputUsuario);
        inputPassword = findViewById(R.id.inputPassword);
        buttonLogin = findViewById(R.id.buttonLogin);


        apiService = RetrofitClient
                .getRetrofitInstance()
                .create(ApiService.class);


        buttonLogin.setOnClickListener(v -> iniciarSesion());
    }

    private void iniciarSesion() {

        String username = inputUsuario.getText().toString().trim();
        String password = inputPassword.getText().toString();


        if (username.isEmpty() || password.isEmpty()) {

            Toast.makeText(
                    this,
                    "Completá usuario y contraseña",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }


        LoginRequest request = new LoginRequest(
                username,
                password
        );


        apiService.login(request).enqueue(new Callback<LoginResponse>() {

            @Override
            public void onResponse(
                    Call<LoginResponse> call,
                    Response<LoginResponse> response
            ) {

                if (response.isSuccessful() && response.body() != null) {

                    LoginResponse loginResponse = response.body();


                    Toast.makeText(
                            LoginActivity.this,
                            "Inicio de sesión correcto",
                            Toast.LENGTH_SHORT
                    ).show();


                    Intent intent = new Intent(
                            LoginActivity.this,
                            DashboardResidenteActivity.class
                    );

                    startActivity(intent);
                    finish();

                } else {


                    Toast.makeText(
                            LoginActivity.this,
                            "Usuario o contraseña incorrectos",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }

            @Override
            public void onFailure(
                    Call<LoginResponse> call,
                    Throwable t
            ) {


                Toast.makeText(
                        LoginActivity.this,
                        "No se pudo conectar con el servidor",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }
}

