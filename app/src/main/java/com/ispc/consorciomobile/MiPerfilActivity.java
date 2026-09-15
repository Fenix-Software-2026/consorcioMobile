package com.ispc.consorciomobile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MiPerfilActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mi_perfil);

        TextView btnVolver = findViewById(R.id.btnVolverBarra);
        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //Btn actualizar contraseña
        Button btnActualizar = findViewById(R.id.btnActualizarContrasena);
        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO:Programar validacion
                Toast.makeText(MiPerfilActivity.this,"Funcion en desarrollo",Toast.LENGTH_SHORT).show();
            }
        });

        //Btn Cerrar Sesion
        Button btnCerrarSesion = findViewById(R.id.btnCerrarSesion);
        btnCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO:Borrar token y volver Login
                Toast.makeText(MiPerfilActivity.this, "Cerrando Sesión....", Toast.LENGTH_SHORT).show();
            }
        });
    }
}