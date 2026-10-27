package com.ispc.consorciomobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;


import com.ispc.consorciomobile.model.Reclamo;
import com.ispc.consorciomobile.adapter.ReclamoAdapter;


import com.ispc.consorciomobile.network.ApiService;
import com.ispc.consorciomobile.network.RetrofitClient;


import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import androidx.recyclerview.widget.RecyclerView;


public class MisReclamosActivity extends AppCompatActivity {
    private static final String TAG = "PRUEBA_RECLAMOS";
    private RecyclerView recyclerMisReclamos;
    private ReclamoAdapter reclamoAdapter;
    private Button btn_crear_reclamo_activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_reclamos);
        Log.d(TAG, "--> onCreate de MisReclamosActivity INICIADO");


        Log.d("PRUEBA_RECLAMOS", "--> onCreate de MisReclamosActivity INICIADO");
        Toast.makeText(this, "Abriendo Mis Reclamos...", Toast.LENGTH_SHORT).show();


        btn_crear_reclamo_activity = findViewById(R.id.btn_crear_reclamo_activity);
        recyclerMisReclamos = findViewById(R.id.recyclerMisReclamos);

        recyclerMisReclamos.setLayoutManager(new LinearLayoutManager(this));
        probarConexionReclamos();

        btn_crear_reclamo_activity.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MisReclamosActivity.this, CrearReclamoActivity.class);
                startActivity(intent);
            }
        });

        // Salimos del panel para volver a la pantalla anterior.
        TextView btnVolver = findViewById(R.id.btnVolverBarra);
        btnVolver.setOnClickListener(view -> finish());

        // Titulo barra
        TextView txtTituloBarra = findViewById(R.id.txtTituloBarra);
        txtTituloBarra.setText("Mis Reclamos");
    }
          @Override
            protected void onResume() {
             super.onResume();
            probarConexionReclamos();
    }

        private void probarConexionReclamos() {
            ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);

            apiService.obtenerReclamos().enqueue(new Callback<List<Reclamo>>() {
                @Override
                public void onResponse(Call<List<Reclamo>> call, Response<List<Reclamo>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<Reclamo> misReclamos = response.body();

                        boolean esAdmin = getIntent().getBooleanExtra("ES_ADMIN", false);
                        if (esAdmin) {
                            btn_crear_reclamo_activity.setVisibility(View.GONE);
                        } else {
                            btn_crear_reclamo_activity.setVisibility(View.VISIBLE);
                        }
                        reclamoAdapter = new ReclamoAdapter(MisReclamosActivity.this, misReclamos, esAdmin);

                        /// Toast.makeText(MisReclamosActivity.this,
                           ///     "¡Éxito! Reclamos del usuario recibidos: " + misReclamos.size(),
                             ///   Toast.LENGTH_LONG).show();

                        recyclerMisReclamos.setAdapter(reclamoAdapter);
                        for (Reclamo r : misReclamos) {
                            Log.d(TAG, "Reclamo ID: " + r.getId() + " | Título: " + r.getTitulo() + " | Estado: " + r.getEstado());
                        }

                    } else {
                        Log.e(TAG, "Error HTTP: " + response.code());
                        Toast.makeText(MisReclamosActivity.this,
                                "Error HTTP: " + response.code() + " (Verificá el Token JWT)",
                                Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<List<Reclamo>> call, Throwable t) {
                    Log.e(TAG, "Error de red: " + t.getMessage());
                    Toast.makeText(MisReclamosActivity.this,
                            "Fallo la conexión: " + t.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            });
        }
    }





