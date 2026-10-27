package com.ispc.consorciomobile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.ispc.consorciomobile.adapter.ComunicadoAdapter;

public class DetalleComunicadoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_comunicado);

        TextView txtTitulo = findViewById(R.id.txtTituloBarra);
        txtTitulo.setText("Detalle Comunicado");

        TextView tvPrioridad = findViewById(R.id.tvDetallePrioridad);
        TextView tvTitulo = findViewById(R.id.tvDetalleTitulo);
        TextView tvFecha = findViewById(R.id.tvDetalleFecha);
        TextView tvContenido = findViewById(R.id.tvDetalleContenido);

        // Recibimos los datos del comunicado desde la lista (igual que en los reclamos).
        String titulo = getIntent().getStringExtra("titulo");
        String contenido = getIntent().getStringExtra("contenido");
        String fecha = getIntent().getStringExtra("fecha");
        boolean urgente = getIntent().getBooleanExtra("urgente", false);

        if (tvTitulo != null) tvTitulo.setText(titulo != null ? titulo : "Sin título");
        if (tvContenido != null)
            tvContenido.setText(contenido != null ? contenido : "Sin contenido");
        if (tvFecha != null)
            tvFecha.setText("Publicado: " + ComunicadoAdapter.formatearFecha(fecha));

        // Si el comunicado es urgente, resaltamos la etiqueta con el mismo naranja de la lista.
        if (tvPrioridad != null) {
            if (urgente) {
                tvPrioridad.setText("URGENTE");
                tvPrioridad.setBackgroundColor(getResources().getColor(R.color.naranja));
                tvPrioridad.setTextColor(getResources().getColor(R.color.white));
            } else {
                tvPrioridad.setText("GENERAL");
            }
        }

        TextView btnVolver = findViewById(R.id.btnVolverBarra);
        btnVolver.setOnClickListener(view -> finish());
    }
}
