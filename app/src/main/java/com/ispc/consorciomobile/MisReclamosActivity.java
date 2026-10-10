package com.ispc.consorciomobile;

import static android.content.ContentValues.TAG;

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
import com.ispc.consorciomobile.ReclamoAdapter;


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_reclamos);
        Log.d(TAG, "--> onCreate de MisReclamosActivity INICIADO");

        TextView txtTitulo = findViewById(R.id.txtTituloBarra);
        txtTitulo.setText("Mis reclamos");

        Log.d("PRUEBA_RECLAMOS", "--> onCreate de MisReclamosActivity INICIADO");
        Toast.makeText(this, "Abriendo Mis Reclamos...", Toast.LENGTH_SHORT).show();


        Button btn_reclamos_volver = findViewById(R.id.btn_reclamos_volver);

        Button btn_crear_reclamo_activity = findViewById(R.id.btn_crear_reclamo_activity);
        recyclerMisReclamos = findViewById(R.id.recyclerMisReclamos);

        recyclerMisReclamos.setLayoutManager(new LinearLayoutManager(this));
        probarConexionReclamos();

        btn_reclamos_volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MisReclamosActivity.this, DashboardResidenteActivity.class
                );
                startActivity(intent);
                finish();
            }
        });

        btn_crear_reclamo_activity.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MisReclamosActivity.this, CrearReclamoActivity.class);
                startActivity(intent);
            }
        });
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
                        reclamoAdapter = new ReclamoAdapter(MisReclamosActivity.this, misReclamos);
                        Toast.makeText(MisReclamosActivity.this,
                                "¡Éxito! Reclamos del usuario recibidos: " + misReclamos.size(),
                                Toast.LENGTH_LONG).show();

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





