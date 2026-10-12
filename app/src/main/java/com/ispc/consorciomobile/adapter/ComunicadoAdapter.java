package com.ispc.consorciomobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.ispc.consorciomobile.Comunicado;
import com.ispc.consorciomobile.R;

import java.util.List;

public class ComunicadoAdapter extends RecyclerView.Adapter<ComunicadoAdapter.ComunicadoViewHolder> {

    private Context context;
    private List<Comunicado> listaComunicados;

    public ComunicadoAdapter(Context context, List<Comunicado> listaComunicados) {
        this.context = context;
        this.listaComunicados = listaComunicados;
    }

    @NonNull
    @Override
    public ComunicadoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_comunicado, parent, false);
        return new ComunicadoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ComunicadoViewHolder holder, int position) {
        Comunicado comunicado = listaComunicados.get(position);

        String titulo = comunicado.getTitulo() != null ? comunicado.getTitulo() : "Sin título";
        if (comunicado.getEsUrgente()) {
            // Mismo resaltado naranja que usaban las tarjetas de ejemplo.
            titulo = "[URGENTE] " + titulo;
            holder.cardComunicado.setCardBackgroundColor(
                    ContextCompat.getColor(context, R.color.naranjaclaro));
            holder.cardComunicado.setStrokeColor(
                    ContextCompat.getColor(context, R.color.naranja));
            holder.cardComunicado.setStrokeWidth(
                    (int) (1 * context.getResources().getDisplayMetrics().density));
        } else {
            holder.cardComunicado.setCardBackgroundColor(
                    ContextCompat.getColor(context, R.color.white));
            holder.cardComunicado.setStrokeWidth(0);
        }

        holder.tvTituloComunicado.setText(titulo);
        holder.tvContenidoComunicado.setText(
                comunicado.getContenido() != null ? comunicado.getContenido() : "Sin contenido");
        holder.tvFechaComunicado.setText("Publicado: " + formatearFecha(comunicado.getFechaPublicacion()));
    }

    @Override
    public int getItemCount() {
        return listaComunicados != null ? listaComunicados.size() : 0;
    }

    // La API devuelve la fecha en formato ISO (2026-09-13T20:00:00Z);
    // acá la mostramos como 13/09/2026, igual que en los ejemplos anteriores.
    private String formatearFecha(String fechaIso) {
        if (fechaIso == null || fechaIso.isEmpty()) {
            return "--/--/----";
        }
        String soloFecha = fechaIso.substring(0, Math.min(10, fechaIso.length()));
        String[] partes = soloFecha.split("-");
        if (partes.length != 3) {
            return fechaIso;
        }
        return partes[2] + "/" + partes[1] + "/" + partes[0];
    }

    public static class ComunicadoViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView cardComunicado;
        TextView tvTituloComunicado, tvContenidoComunicado, tvFechaComunicado;

        public ComunicadoViewHolder(@NonNull View itemView) {
            super(itemView);
            cardComunicado = itemView.findViewById(R.id.cardComunicado);
            tvTituloComunicado = itemView.findViewById(R.id.tvTituloComunicado);
            tvContenidoComunicado = itemView.findViewById(R.id.tvContenidoComunicado);
            tvFechaComunicado = itemView.findViewById(R.id.tvFechaComunicado);
        }
    }
}
