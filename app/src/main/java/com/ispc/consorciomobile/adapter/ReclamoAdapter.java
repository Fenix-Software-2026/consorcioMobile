package com.ispc.consorciomobile.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ispc.consorciomobile.DetalleReclamoActivity;
import com.ispc.consorciomobile.R;
import com.ispc.consorciomobile.model.Reclamo;

import java.util.List;

public class ReclamoAdapter extends RecyclerView.Adapter<ReclamoAdapter.ReclamoViewHolder> {

    private Context context;
    private List<Reclamo> listaReclamos;

    public ReclamoAdapter(Context context, List<Reclamo> listaReclamos) {
        this.context = context;
        this.listaReclamos = listaReclamos;
    }

    @NonNull
    @Override
    public ReclamoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_reclamo, parent, false);
        return new ReclamoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReclamoViewHolder holder, int position) {
        Reclamo reclamo = listaReclamos.get(position);

        if (holder.tvTitulo != null) {
            holder.tvTitulo.setText(reclamo.getTitulo() != null ? reclamo.getTitulo() : "Sin título");
        }
        if (holder.tvDescripcion != null) {
            holder.tvDescripcion.setText(reclamo.getDescripcion() != null ? reclamo.getDescripcion() : "Sin descripción");
        }
        if (holder.tvEstado != null) {
            holder.tvEstado.setText(reclamo.getEstado() != null ? reclamo.getEstado() : "Registrado");
        }
        if (holder.tvFecha != null) {
            holder.tvFecha.setText(reclamo.getFechaCreacion() != null ? reclamo.getFechaCreacion() : "");
        }
        if (holder.tvCategoria != null) {
            holder.tvCategoria.setText(reclamo.getCategoria() != null ? reclamo.getCategoria() : "General");
        }

// Evento de clic en la tarjeta para abrir el detalle
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetalleReclamoActivity.class);
                intent.putExtra("titulo", reclamo.getTitulo());
                intent.putExtra("descripcion", reclamo.getDescripcion());
                intent.putExtra("categoria", reclamo.getCategoria());
                intent.putExtra("estado", reclamo.getEstado());
                intent.putExtra("fecha", reclamo.getFechaCreacion());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaReclamos != null ? listaReclamos.size() : 0;
    }

    public static class ReclamoViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitulo, tvDescripcion, tvEstado, tvFecha, tvCategoria;

        public ReclamoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitulo = itemView.findViewById(R.id.tvTituloReclamo);
            tvDescripcion = itemView.findViewById(R.id.tvDescripcionReclamo);
            tvEstado = itemView.findViewById(R.id.tvEstadoReclamo);
            tvFecha = itemView.findViewById(R.id.tvFechaReclamo);
            tvCategoria = itemView.findViewById(R.id.tvCategoriaReclamo);
        }
    }
}