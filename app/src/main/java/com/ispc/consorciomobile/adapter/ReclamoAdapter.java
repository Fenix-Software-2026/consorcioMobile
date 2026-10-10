package com.ispc.consorciomobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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

        holder.tvTitulo.setText(reclamo.getTitulo() != null ? reclamo.getTitulo() : "Sin título");
        holder.tvDescripcion.setText(reclamo.getDescripcion() != null ? reclamo.getDescripcion() : "Sin descripción");
        holder.tvEstado.setText(reclamo.getEstado() != null ? reclamo.getEstado() : "Registrado");
        holder.tvFecha.setText(reclamo.getFechaCreacion() != null ? reclamo.getFechaCreacion() : "");
    }

    @Override
    public int getItemCount() {
        return listaReclamos != null ? listaReclamos.size() : 0;
    }

    public static class ReclamoViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitulo, tvDescripcion, tvEstado, tvUbicacion, tvFecha, tvCategoria;

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