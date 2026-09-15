package com.ispc.consorciomobile;

import androidx.appcompat.app.AppCompatActivity;

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

                Toast.makeText(this, "Login correcto", Toast.LENGTH_SHORT).show();

            } else {

                Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();

            }
        });
    }
}