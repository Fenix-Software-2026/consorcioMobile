package com.ispc.consorciomobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

        //referencias de los textos
        EditText etContrasenaActual = findViewById(R.id.etContrasenaActual);
        EditText etNuevaContrasena = findViewById(R.id.etNuevaContrasena);
        EditText etRepetirContrasena = findViewById(R.id.etRepetirContrasena);

        //Btn actualizar contraseña
        Button btnActualizar = findViewById(R.id.btnActualizarContrasena);
        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String passActual = etContrasenaActual.getText().toString().trim();
                String passNueva = etNuevaContrasena.getText().toString().trim();
                String passRepetir = etRepetirContrasena.getText().toString().trim();
                //Regex min 8 caract con min, mayus numeros y caracter
                String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!_.-])(?=\\S+$).{8,}$";

                //TODO:Programar validacion
                //campos vacios
                if(passActual.isEmpty() || passNueva.isEmpty()){
                    Toast.makeText(MiPerfilActivity.this,"Por favor, completa todos los campos",Toast.LENGTH_SHORT).show();
                    return;
                }
                //coincidencia de pass
                if(!passNueva.equals(passRepetir)){
                    Toast.makeText(MiPerfilActivity.this,"Las nuevas contraseñas no coinciden",Toast.LENGTH_SHORT).show();
                    return;
                }
                //ciber seguridad
                if(!passNueva.matches((PASSWORD_REGEX))){
                    Toast.makeText(MiPerfilActivity.this,"La contraseña debe tener al menos 8 caracteres, una mayúscula, una minúscula, un número y un carácter especial",Toast.LENGTH_SHORT).show();
                    return;
                }

                // TODO:conectar API
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