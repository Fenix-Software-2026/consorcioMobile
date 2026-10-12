package com.ispc.consorciomobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ispc.consorciomobile.model.CambioContrasenaRequest;
import com.ispc.consorciomobile.model.Residente;
import com.ispc.consorciomobile.network.ApiService;
import com.ispc.consorciomobile.network.RetrofitClient;
import com.ispc.consorciomobile.network.TokenManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MiPerfilActivity extends AppCompatActivity {
    private TextView txtTituloBarra, txtNombreUsuarioConfig; // Referencias para datos dinámicos
    private EditText etContrasenaActual, etNuevaContrasena, etRepetirContrasena;
    private Button btnActualizarContrasena, btnCerrarSesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mi_perfil);

        // Configuración de la barra superior personalizada
        TextView btnVolver = findViewById(R.id.btnVolverBarra);
        btnVolver.setOnClickListener(view -> finish());
        txtTituloBarra = findViewById(R.id.txtTituloBarra);
        txtTituloBarra.setText("Mi Perfil");

        etContrasenaActual = findViewById(R.id.etContrasenaActual);
        etNuevaContrasena = findViewById(R.id.etNuevaContrasena);
        etRepetirContrasena = findViewById(R.id.etRepetirContrasena);
        txtNombreUsuarioConfig = findViewById(R.id.txtNombreUsuarioConfig);

        btnActualizarContrasena = findViewById(R.id.btnActualizarContrasena);
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion);

        //Obtenemos el username guardado globalmente en el TokenManager
        String username = TokenManager.obtenerUsername();
        if (username != null && !username.isEmpty()) {
            if (txtNombreUsuarioConfig != null) {
                txtNombreUsuarioConfig.setText(username);
            }
        }

        btnActualizarContrasena.setOnClickListener(view -> validarYActualizarContrasena());
        btnCerrarSesion.setOnClickListener(view -> cerrarSesionSegura());
    }

    private void validarYActualizarContrasena() {
        String passActual = etContrasenaActual.getText().toString().trim();
        String passNueva = etNuevaContrasena.getText().toString().trim();
        String passRepetir = etRepetirContrasena.getText().toString().trim();

        // Regex de seguridad: mínimo 8 caracteres, una mayúscula, una minúscula, un número y un carácter especial
        String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!_.-])(?=\\S+$).{8,}$";

        // Validación de campos vacíos incluyendo la repetición
        if (passActual.isEmpty() || passNueva.isEmpty() || passRepetir.isEmpty()) {
            Toast.makeText(MiPerfilActivity.this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validación de coincidencia de contraseñas nuevas
        if (!passNueva.equals(passRepetir)) {
            Toast.makeText(MiPerfilActivity.this, "Las nuevas contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validación de complejidad de la contraseña
        if (!passNueva.matches(PASSWORD_REGEX)) {
            Toast.makeText(MiPerfilActivity.this, "La contraseña debe tener al menos 8 caracteres, una mayúscula, una minúscula, un número y un carácter especial", Toast.LENGTH_LONG).show();
            return;
        }

        // Preparamos el cuerpo de la petición PATCH
        CambioContrasenaRequest cambioRequest = new CambioContrasenaRequest(passActual, passNueva);

        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        btnActualizarContrasena.setEnabled(false);

        apiService.actualizarMiPerfil(cambioRequest).enqueue(new Callback<Residente>() {
            @Override
            public void onResponse(Call<Residente> call, Response<Residente> response) {
                btnActualizarContrasena.setEnabled(true);
                if (response.isSuccessful()) {
                    Toast.makeText(MiPerfilActivity.this, "Contraseña actualizada con éxito", Toast.LENGTH_SHORT).show();
                    // Limpiamos los campos del formulario tras el éxito
                    etContrasenaActual.setText("");
                    etNuevaContrasena.setText("");
                    etRepetirContrasena.setText("");
                } else {
                    Toast.makeText(MiPerfilActivity.this, "Error al actualizar contraseña. Verifica tu contraseña actual.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Residente> call, Throwable t) {
                btnActualizarContrasena.setEnabled(true);
                Toast.makeText(MiPerfilActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

        //Btn Cerrar Sesion
        private void cerrarSesionSegura() {
        // Limpiamos cualquier preferencia de sesión local si existiera
        SharedPreferences preferences = getSharedPreferences("SesionPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();

        // Redireccionar al Login limpiando el historial de actividades
        Intent intent = new Intent(MiPerfilActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

        Toast.makeText(MiPerfilActivity.this, "Cerrando Sesión...", Toast.LENGTH_SHORT).show();
        finish();
    }

}