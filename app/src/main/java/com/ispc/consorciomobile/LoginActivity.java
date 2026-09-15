package com.ispc.consorciomobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private EditText inputPassword;
    private EditText inputUsuario;
    private Button buttonLogin;

    private static final String USUARIO_VALIDO = "admin";
    private static final String CLAVE_VALIDA = "1234";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputPassword = findViewById(R.id.inputPassword);
        inputUsuario = findViewById(R.id.inputUsuario);
        buttonLogin = findViewById(R.id.buttonLogin);

        buttonLogin.setOnClickListener(v -> {

            String usuario = inputUsuario.getText().toString();
            String clave = inputPassword.getText().toString();

            if (usuario.equals(USUARIO_VALIDO) && clave.equals(CLAVE_VALIDA)) {

                Intent intent = new Intent(LoginActivity.this, DashboardResidenteActivity.class);
                startActivity(intent);
                finish();

            } else {

                Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();

            }
        });
    }
}