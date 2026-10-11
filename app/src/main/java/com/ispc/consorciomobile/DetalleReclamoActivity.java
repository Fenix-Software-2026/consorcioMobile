package com.ispc.consorciomobile;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DetalleReclamoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_reclamo);
        TextView txtTitulo = findViewById(R.id.txtTituloBarra);
        txtTitulo.setText("Detalle Reclamo");

        TextView tvTitulo = findViewById(R.id.tvDetalleTitulo);
        TextView tvDescripcion = findViewById(R.id.tvDetalleDescripcion);
        TextView tvCategoria = findViewById(R.id.tvDetalleCategoria);
        TextView tvEstado = findViewById(R.id.tvDetalleEstado);
        TextView tvFecha = findViewById(R.id.tvDetalleFecha);

        String titulo = getIntent().getStringExtra("titulo");
        String descripcion = getIntent().getStringExtra("descripcion");
        String categoria = getIntent().getStringExtra("categoria");
        String estado = getIntent().getStringExtra("estado");
        String fecha = getIntent().getStringExtra("fecha");

        if (tvTitulo != null) tvTitulo.setText(titulo != null ? titulo : "Sin título");
        if (tvDescripcion != null)
            tvDescripcion.setText(descripcion != null ? descripcion : "Sin descripción");
        if (tvCategoria != null)
            tvCategoria.setText("Categoría: " + (categoria != null ? categoria : "-"));
        if (tvEstado != null) tvEstado.setText(estado != null ? estado.toUpperCase() : "PENDIENTE");
        if (tvFecha != null) tvFecha.setText("Creado el: " + (fecha != null ? fecha : "-"));
    }
}
