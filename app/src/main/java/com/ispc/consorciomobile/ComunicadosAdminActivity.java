package com.ispc.consorciomobile;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ComunicadosAdminActivity extends AppCompatActivity {

    private EditText etTituloComunicado;
    private EditText etCuerpoComunicado;
    private Button btnPublicar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comunicados_admin);

        etTituloComunicado = findViewById(R.id.etTituloComunicado);
        etCuerpoComunicado = findViewById(R.id.etCuerpoComunicado);
        btnPublicar = findViewById(R.id.btnPublicar);

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
}