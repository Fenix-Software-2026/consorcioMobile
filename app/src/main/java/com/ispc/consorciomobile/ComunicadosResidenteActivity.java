package com.ispc.consorciomobile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ispc.consorciomobile.adapter.ComunicadoAdapter;
import com.ispc.consorciomobile.network.ApiService;
import com.ispc.consorciomobile.network.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ComunicadosResidenteActivity extends AppCompatActivity {

    private static final String TAG = "COMUNICADOS";
    private RecyclerView recyclerComunicados;
    private TextView tvSinComunicados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comunicados_residente);

        // Salimos del panel para volver a la pantalla anterior.
        TextView btnVolver = findViewById(R.id.btnVolverBarra);
        btnVolver.setOnClickListener(view -> finish());

        // Titulo barra
        TextView txtTituloBarra = findViewById(R.id.txtTituloBarra);
        txtTituloBarra.setText("Comunicados");

        recyclerComunicados = findViewById(R.id.recyclerComunicados);
        tvSinComunicados = findViewById(R.id.tvSinComunicados);
        recyclerComunicados.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Pedimos la lista cada vez que entramos a la pantalla para ver los cambios al instante.
        cargarComunicados();
    }

    private void cargarComunicados() {
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);

        apiService.obtenerComunicados().enqueue(new Callback<List<Comunicado>>() {
            @Override
            public void onResponse(Call<List<Comunicado>> call, Response<List<Comunicado>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Comunicado> comunicados = response.body();
                    Log.d(TAG, "Comunicados recibidos: " + comunicados.size());

                    recyclerComunicados.setAdapter(
                            new ComunicadoAdapter(ComunicadosResidenteActivity.this, comunicados));

                    // Si no hay ninguno, mostramos el mensaje de "sin comunicados".
                    tvSinComunicados.setVisibility(comunicados.isEmpty()
                            ? TextView.VISIBLE : TextView.GONE);
                } else {
                    Log.e(TAG, "Error HTTP: " + response.code());
                    Toast.makeText(ComunicadosResidenteActivity.this,
                            "Error HTTP: " + response.code() + " (Verificá el Token JWT)",
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Comunicado>> call, Throwable t) {
                Log.e(TAG, "Error de red: " + t.getMessage());
                Toast.makeText(ComunicadosResidenteActivity.this,
                        "Fallo la conexión: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}
