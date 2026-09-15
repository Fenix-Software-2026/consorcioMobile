package com.ispc.consorciomobile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class ComunicadosResidenteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comunicados_residente);

        // Referencia al botón de retorno en la barra superior
        Button btnVolverDashboard = findViewById(R.id.btnVolverDashboard);

        // Configuración del Intent para volver al Dashboard del Residente
        btnVolverDashboard.setOnClickListener(v -> {
            Intent intent = new Intent(ComunicadosResidenteActivity.this, DashboardResidenteActivity.class);
            startActivity(intent);
            finish(); // Cierra esta activity opcionalmente para mantener limpia la pila de navegación
        });
    }
}