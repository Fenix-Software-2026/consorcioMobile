package com.ispc.consorciomobile;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ispc.consorciomobile.adapter.ComunicadoAdminAdapter;
import com.ispc.consorciomobile.network.ApiService;
import com.ispc.consorciomobile.network.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ComunicadosAdminActivity extends AppCompatActivity {

    private static final String TAG = "COMUNICADOS_ADMIN";
    private EditText etTituloComunicado;
    private EditText etCuerpoComunicado;
    private Button btnPublicar;
    private RecyclerView recyclerComunicadosAdmin;
    private TextView tvSinComunicadosAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comunicados_admin);

        etTituloComunicado = findViewById(R.id.etTituloComunicado);
        etCuerpoComunicado = findViewById(R.id.etCuerpoComunicado);
        btnPublicar = findViewById(R.id.btnPublicar);

        recyclerComunicadosAdmin = findViewById(R.id.recyclerComunicadosAdmin);
        tvSinComunicadosAdmin = findViewById(R.id.tvSinComunicadosAdmin);
        recyclerComunicadosAdmin.setLayoutManager(new LinearLayoutManager(this));

        // Salimos del panel para volver a la pantalla anterior.
        Button btnVolverComunicados = findViewById(R.id.btnVolverComunicado​s);
        btnVolverComunicados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnPublicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String titulo = etTituloComunicado.getText().toString();
                String cuerpo = etCuerpoComunicado.getText().toString();

                if (!titulo.isEmpty() && !cuerpo.isEmpty()) {
                    Toast.makeText(ComunicadosAdminActivity.this, "Comunicado preparado (Sin API)", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ComunicadosAdminActivity.this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refrescamos el historial cada vez que entramos o volvemos de otra pantalla.
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

                    recyclerComunicadosAdmin.setAdapter(
                            new ComunicadoAdminAdapter(ComunicadosAdminActivity.this, comunicados));

                    tvSinComunicadosAdmin.setVisibility(comunicados.isEmpty()
                            ? TextView.VISIBLE : TextView.GONE);
                } else {
                    Log.e(TAG, "Error HTTP: " + response.code());
                    Toast.makeText(ComunicadosAdminActivity.this,
                            "Error HTTP: " + response.code() + " (Verificá el Token JWT)",
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Comunicado>> call, Throwable t) {
                Log.e(TAG, "Error de red: " + t.getMessage());
                Toast.makeText(ComunicadosAdminActivity.this,
                        "Fallo la conexión: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}
