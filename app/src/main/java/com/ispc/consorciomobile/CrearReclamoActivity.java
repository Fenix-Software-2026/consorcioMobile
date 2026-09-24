package com.ispc.consorciomobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class CrearReclamoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_reclamo);

        Button btnCrearReclamo;
        Button btnCancelarReclamo;
        EditText etTituloCrearReclamo;
        Spinner spCategoriaCrearReclamo;
        EditText etDescripcionCrearReclamo;


        etTituloCrearReclamo= findViewById(R.id.etTituloCrearReclamo);
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

        ArrayAdapter <String> adapter = new ArrayAdapter<>(
                this , androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, categorias
        );

        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);

        spCategoriaCrearReclamo.setAdapter(adapter);

        btnCrearReclamo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CrearReclamoActivity.this, DashboardResidenteActivity.class);
                startActivity(intent);
                finish();
            }
        });
             btnCancelarReclamo.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick (View v){
                    Intent intent = new Intent(CrearReclamoActivity.this, DashboardResidenteActivity.class);
                    startActivity(intent);
                    finish();
                }
        });
    }

}