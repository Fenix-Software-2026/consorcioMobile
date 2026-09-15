package com.ispc.consorciomobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CrearReclamoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_reclamo);

        Button btnCrearReclamo;
        Button btnCancelarReclamo;

        btnCrearReclamo = findViewById(R.id.btnCrearReclamo);
        btnCancelarReclamo = findViewById(R.id.btnCancelarCrearReclamo);

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