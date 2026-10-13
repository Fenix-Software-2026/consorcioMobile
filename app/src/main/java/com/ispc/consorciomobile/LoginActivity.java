package com.ispc.consorciomobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.ispc.consorciomobile.data.AuthRepository;

public class LoginActivity extends AppCompatActivity {

    private EditText inputPassword;
    private EditText inputUsuario;
    private Button buttonLogin;
    private ImageButton buttonVerClave;
    private AuthRepository authRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        authRepository = new AuthRepository();

        inputPassword = findViewById(R.id.inputPassword);
        inputUsuario = findViewById(R.id.inputUsuario);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonVerClave = findViewById(R.id.buttonVerClave);

        // Mostrar u ocultar la contraseña
        buttonVerClave.setOnClickListener(v -> {
            int posicion = inputPassword.getSelectionStart();

            if (inputPassword.getTransformationMethod()
                    instanceof PasswordTransformationMethod) {

                inputPassword.setTransformationMethod(
                        HideReturnsTransformationMethod.getInstance()
                );

                buttonVerClave.setContentDescription(
                        "Ocultar contraseña"
                );

            } else {

                inputPassword.setTransformationMethod(
                        PasswordTransformationMethod.getInstance()
                );

                buttonVerClave.setContentDescription(
                        "Mostrar contraseña"
                );
            }

            // Mantener la posición del cursor
            if (posicion >= 0) {
                inputPassword.setSelection(posicion);
            }
        });

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

        buttonLogin.setEnabled(false);

        authRepository.login(username, password, new AuthRepository.LoginCallback() {
            @Override
            public void onSuccess(String rol, String usernameToken) {
                buttonLogin.setEnabled(true);
                abrirPantallaSegunRol(rol, usernameToken);
            }

            @Override
            public void onErrorCredenciales() {
                buttonLogin.setEnabled(true);
                Toast.makeText(
                        LoginActivity.this,
                        "Usuario o contraseña incorrectos",
                        Toast.LENGTH_SHORT
                ).show();
            }

            @Override
            public void onErrorTokenInvalido() {
                buttonLogin.setEnabled(true);
                Toast.makeText(
                        LoginActivity.this,
                        "El token recibido no es válido",
                        Toast.LENGTH_LONG
                ).show();
            }

            @Override
            public void onErrorConexion() {
                buttonLogin.setEnabled(true);
                Toast.makeText(
                        LoginActivity.this,
                        "No se pudo conectar con el servidor",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }

    private void abrirPantallaSegunRol(String rol, String username) {
        // Los roles administrativos acceden al alta.
        // Los demás acceden al dashboard.
        Intent intent;

        String rolNormalizado = rol.trim().toLowerCase();

        if (rolNormalizado.equals("administrador")
                || rolNormalizado.equals("admin")
                || rolNormalizado.equals("consorcio")) {

            intent = new Intent(this, AdminDashboard.class);

        } else {

            intent = new Intent(
                    this,
                    DashboardResidenteActivity.class
            );
        }

        intent.putExtra("username", username);

        startActivity(intent);
        finish();
    }
}