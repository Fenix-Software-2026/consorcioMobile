package com.ispc.consorciomobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ispc.consorciomobile.viewmodel.MiPerfilViewModel;

public class MiPerfilActivity extends AppCompatActivity {
    private TextView txtTituloBarra, txtNombreUsuarioConfig;
    private EditText etContrasenaActual, etNuevaContrasena, etRepetirContrasena;
    private Button btnActualizarContrasena, btnCerrarSesion;
    private MiPerfilViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mi_perfil);

        viewModel = new ViewModelProvider(this).get(MiPerfilViewModel.class);

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

        setupObservers();

        viewModel.cargarDatosUsuario();

        btnActualizarContrasena.setOnClickListener(view -> {
            String passActual = etContrasenaActual.getText().toString().trim();
            String passNueva = etNuevaContrasena.getText().toString().trim();
            String passRepetir = etRepetirContrasena.getText().toString().trim();
            viewModel.validarYActualizarContrasena(passActual, passNueva, passRepetir);
        });

        btnCerrarSesion.setOnClickListener(view -> viewModel.cerrarSesionSegura());
    }

    private void setupObservers() {
        viewModel.getUsernameLiveData().observe(this, username -> {
            if (txtNombreUsuarioConfig != null) {
                txtNombreUsuarioConfig.setText(username);
            }
        });

        viewModel.getIsLoadingLiveData().observe(this, isLoading -> {
            btnActualizarContrasena.setEnabled(!isLoading);
        });

        viewModel.getToastMessageLiveData().observe(this, message -> {
            if (message != null && !message.isEmpty()) {
                Toast.makeText(MiPerfilActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.getPasswordUpdatedSuccessLiveData().observe(this, success -> {
            if (Boolean.TRUE.equals(success)) {
                etContrasenaActual.setText("");
                etNuevaContrasena.setText("");
                etRepetirContrasena.setText("");
            }
        });

        viewModel.getSessionClosedLiveData().observe(this, closed -> {
            if (Boolean.TRUE.equals(closed)) {
                Intent intent = new Intent(MiPerfilActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
    }
}