package com.ispc.consorciomobile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ispc.consorciomobile.network.ApiService;
import com.ispc.consorciomobile.network.RetrofitClient;
import com.ispc.consorciomobile.model.Reclamo;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetalleReclamoActivity extends AppCompatActivity {
    private int reclamoId;
    private TextView tvEstado;
    private Spinner spCambiarEstado;
    private LinearLayout panelAdminEstado;
    private Button btnGuardarEstadoAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_reclamo);

        TextView txtTitulo = findViewById(R.id.txtTituloBarra);
        txtTitulo.setText("Detalle Reclamo");
        TextView btnVolverBarra = findViewById(R.id.btnVolverBarra);
        if (btnVolverBarra != null) {
            btnVolverBarra.setOnClickListener(v -> {
                boolean esAdmin = getIntent().getBooleanExtra("ES_ADMIN", false);
                Intent intent = new Intent(DetalleReclamoActivity.this, MisReclamosActivity.class);
                intent.putExtra("ES_ADMIN", esAdmin);
                startActivity(intent);
                finish();
            });;
        }

        TextView tvTitulo = findViewById(R.id.tvDetalleTitulo);
        TextView tvDescripcion = findViewById(R.id.tvDetalleDescripcion);
        TextView tvCategoria = findViewById(R.id.tvDetalleCategoria);
        TextView tvEstado = findViewById(R.id.tvDetalleEstado);
        TextView tvFecha = findViewById(R.id.tvDetalleFecha);

        panelAdminEstado = findViewById(R.id.panelAdminEstado);
        spCambiarEstado = findViewById(R.id.spCambiarEstado);
        btnGuardarEstadoAdmin = findViewById(R.id.btnGuardarEstadoAdmin);

        boolean esAdmin = getIntent().getBooleanExtra("ES_ADMIN", false);
        Log.d("DEBUG_ROL", "Valor exacto de ES_ADMIN recibido: " + esAdmin);

        if (panelAdminEstado != null) {
            if (esAdmin) {
                panelAdminEstado.setVisibility(View.VISIBLE);
                Log.d("DEBUG_ROL", "-> Panel Admin PUESTO EN VISIBLE");
            } else {
                panelAdminEstado.setVisibility(View.GONE);
                Log.d("DEBUG_ROL", "-> Panel Admin OCULTO (GONE)");
            }
        } else {
            Log.e("DEBUG_ROL", "¡ERROR! panelAdminEstado es NULL. Revisar el ID en el XML.");
        }
        reclamoId = getIntent().getIntExtra("id", -1);
        String titulo = getIntent().getStringExtra("titulo");
        String descripcion = getIntent().getStringExtra("descripcion");
        String categoria = getIntent().getStringExtra("categoria");
        String estado = getIntent().getStringExtra("estado");
        String fecha = getIntent().getStringExtra("fecha");

        if (tvTitulo != null) tvTitulo.setText(titulo != null ? titulo : "Sin título");
        if (tvDescripcion != null)
            tvDescripcion.setText(descripcion != null ? descripcion : "Sin descripción");
        if (tvCategoria != null)
            tvCategoria.setText("Categoría: " + (categoria != null ? categoria : "-"));
        if (tvEstado != null) tvEstado.setText(estado != null ? estado.toUpperCase() : "PENDIENTE");
        if (tvFecha != null) tvFecha.setText("Creado el: " + (fecha != null ? fecha : "-"));

        String[] estados = {"pendiente", "en_proceso", "resuelto", "archivado"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, estados);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        if (spCambiarEstado != null) {
            spCambiarEstado.setAdapter(adapter);

            if (estado != null) {
                for (int i = 0; i < estados.length; i++) {
                    if (estados[i].equalsIgnoreCase(estado)) {
                        spCambiarEstado.setSelection(i);
                        break;
                    }
                }
            }

        }


        if (btnGuardarEstadoAdmin != null) {
            btnGuardarEstadoAdmin.setOnClickListener(v -> {
                if (spCambiarEstado != null) {
                    String nuevoEstado = spCambiarEstado.getSelectedItem().toString();
                    actualizarEstadoEnBackend(nuevoEstado);
                }
            });
        }
    }

    private void actualizarEstadoEnBackend(String nuevoEstado) {
        if (reclamoId == -1) {
            Toast.makeText(this, "Error: ID de reclamo inválido", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, String> body = new HashMap<>();
        body.put("estado", nuevoEstado);

        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);

        apiService.actualizarEstadoReclamo(reclamoId, body).enqueue(new Callback<Reclamo>() {
            @Override
            public void onResponse(Call<Reclamo> call, Response<Reclamo> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(DetalleReclamoActivity.this, "¡Estado actualizado a: " + nuevoEstado + "!", Toast.LENGTH_SHORT).show();
                    if (tvEstado != null) {
                        tvEstado.setText(nuevoEstado.toUpperCase());
                    }
                } else {
                    Toast.makeText(DetalleReclamoActivity.this, "Error al actualizar (HTTP " + response.code() + ")", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Reclamo> call, Throwable t) {
                Toast.makeText(DetalleReclamoActivity.this, "Falla de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}


