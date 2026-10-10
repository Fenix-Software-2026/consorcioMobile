
package com.ispc.consorciomobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.text.method.PasswordTransformationMethod;
import android.text.method.HideReturnsTransformationMethod;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.ispc.consorciomobile.network.ApiService;
import com.ispc.consorciomobile.network.LoginRequest;
import com.ispc.consorciomobile.network.LoginResponse;
import com.ispc.consorciomobile.network.RetrofitClient;

import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText inputPassword;
    private EditText inputUsuario;
    private Button buttonLogin;
    private ImageButton buttonVerClave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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

        ApiService apiService = RetrofitClient
                .getRetrofitInstance()
                .create(ApiService.class);

        buttonLogin.setEnabled(false);

        apiService.iniciarSesion(
                new LoginRequest(username, password)
        ).enqueue(new Callback<LoginResponse>() {

            @Override
            public void onResponse(
                    Call<LoginResponse> call,
                    Response<LoginResponse> response
            ) {
                buttonLogin.setEnabled(true);

                if (!response.isSuccessful() || response.body() == null) {
                    Toast.makeText(
                            LoginActivity.this,
                            "Usuario o contraseña incorrectos",
                            Toast.LENGTH_SHORT
                    ).show();
                    return;
                }

                try {
                    LoginResponse loginResponse = response.body();

                    JSONObject datosToken = decodificarPayload(
                            loginResponse.getAccess()
                    );

                    String usernameToken = datosToken.optString("username");
                    String rol = datosToken.optString("rol");

                    // Guardar el access token
                    RetrofitClient.guardarToken(
                            loginResponse.getAccess()
                    );

                    // Redirigir según el rol
                    abrirPantallaSegunRol(rol, usernameToken);

                } catch (Exception exception) {
                    Toast.makeText(
                            LoginActivity.this,
                            "El token recibido no es válido",
                            Toast.LENGTH_LONG
                    ).show();
                }
            }

            @Override
            public void onFailure(
                    Call<LoginResponse> call,
                    Throwable t
            ) {
                buttonLogin.setEnabled(true);

                Toast.makeText(
                        LoginActivity.this,
                        "No se pudo conectar con el servidor",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }

    private JSONObject decodificarPayload(String token) throws Exception {

        // Un JWT tiene tres partes separadas por puntos.
        // La segunda contiene los claims.
        String[] partes = token.split("\\.");

        if (partes.length != 3) {
            throw new IllegalArgumentException("JWT inválido");
        }

        byte[] payload = Base64.decode(
                partes[1],
                Base64.URL_SAFE | Base64.NO_WRAP
        );

        return new JSONObject(
                new String(payload, StandardCharsets.UTF_8)
        );
    }

    private void abrirPantallaSegunRol(String rol, String username) {

        // Los roles administrativos acceden al alta.
        // Los demás acceden al dashboard.
        Intent intent;

        String rolNormalizado = rol.trim().toLowerCase();

        if (rolNormalizado.equals("administrador")
                || rolNormalizado.equals("admin")
                || rolNormalizado.equals("consorcio")) {

            intent = new Intent(this, AltaResidente.class);

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
