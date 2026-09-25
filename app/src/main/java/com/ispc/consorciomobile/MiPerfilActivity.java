package com.ispc.consorciomobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
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

        //Cambio de texto a barra personalizada
        TextView txtTitulo = findViewById(R.id.txtTituloBarra);
        txtTitulo.setText("Mi Perfil");

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
                //TODO:ver nombre de la sesion
                //borrar token
                SharedPreferences preferences = getSharedPreferences("SesionPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.apply();

                //redireccion
                Intent intent = new Intent(MiPerfilActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

                Toast.makeText(MiPerfilActivity.this, "Cerrando Sesión....", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}