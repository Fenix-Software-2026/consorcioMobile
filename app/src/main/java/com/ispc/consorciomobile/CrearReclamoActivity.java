package com.ispc.consorciomobile;

import androidx.appcompat.app.AppCompatActivity;
import java.util.List;
import android.util.Log;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ispc.consorciomobile.model.Reclamo;
import com.ispc.consorciomobile.network.ApiService;
import com.ispc.consorciomobile.network.RetrofitClient;

public class CrearReclamoActivity extends AppCompatActivity {
    private static final String TAG = "CREAR_RECLAMO";

    Button btnCrearReclamo;
    Button btnCancelarReclamo;
    EditText etTituloCrearReclamo;
    Spinner spCategoriaCrearReclamo;
    EditText etDescripcionCrearReclamo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_reclamo);

        TextView txtTitulo = findViewById(R.id.txtTituloBarra);
        txtTitulo.setText(" Crear Reclamo");

        etTituloCrearReclamo = findViewById(R.id.etTituloCrearReclamo);
        spCategoriaCrearReclamo = findViewById(R.id.spCategoriaCrearReclamo);
        etDescripcionCrearReclamo = findViewById(R.id.etDescripcionCrearReclamo);

        btnCrearReclamo = findViewById(R.id.btnCrearReclamo);
        btnCancelarReclamo = findViewById(R.id.btnCancelarCrearReclamo);

        String[] categorias = {
                "Seleccionar categoría",
                "Plomería",
                "Electricidad",
                "Gas",
                "Humedad / Filtraciones",
                "Ascensor",
                "Limpieza",
                "Seguridad / Accesos",
                "Ruidos molestos",
                "Internet / Antena",
                "Otros"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, categorias
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spCategoriaCrearReclamo.setAdapter(adapter);

        btnCrearReclamo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarReclamo();
            }
        });

        btnCancelarReclamo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CrearReclamoActivity.this, MisReclamosActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void enviarReclamo() {
        String titulo = etTituloCrearReclamo.getText().toString().trim();
        String categoria = spCategoriaCrearReclamo.getSelectedItem().toString();
        String descripcion = etDescripcionCrearReclamo.getText().toString().trim();

        if (titulo.isEmpty() || descripcion.isEmpty()) {
            Toast.makeText(this, "Por favor completá todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (categoria.equals("Seleccionar categoría")) {
            Toast.makeText(this, "Por favor elegí una categoría válida", Toast.LENGTH_SHORT).show();
            return;
        }

        String categoriaKey = obtenerClaveCategoria(categoria);

        Reclamo nuevoReclamo = new Reclamo();
        nuevoReclamo.setTitulo(titulo);
        nuevoReclamo.setDescripcion(descripcion);
        nuevoReclamo.setCategoria(categoriaKey);

        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);

        apiService.crearReclamo(nuevoReclamo).enqueue(new Callback<Reclamo>() {
            @Override
            public void onResponse(Call<Reclamo> call, Response<Reclamo> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(CrearReclamoActivity.this, "¡Reclamo creado con éxito!", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(CrearReclamoActivity.this, MisReclamosActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "Sin cuerpo";
                        Log.e(TAG, "Error HTTP 400 detalle: " + errorBody);
                        Toast.makeText(CrearReclamoActivity.this, "Error 400: " + errorBody, Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Reclamo> call, Throwable t) {
                Log.e(TAG, "Falla de red: " + t.getMessage());
                Toast.makeText(CrearReclamoActivity.this, "Fallo de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

        private String obtenerClaveCategoria (String textoSeleccionado){
            switch (textoSeleccionado) {
                case "Plomería":
                    return "plomeria";
                case "Electricidad":
                    return "electricidad";
                case "Gas":
                    return "gas";
                case "Humedad / Filtraciones":
                    return "humedad";
                case "Ascensor":
                    return "ascensor";
                case "Limpieza":
                    return "limpieza";
                case "Seguridad / Accesos":
                    return "seguridad";
                case "Ruidos molestos":
                    return "ruidos";
                case "Internet / Antena":
                    return "internet";
                default:
                    return "otros";
            }
        }
    }
