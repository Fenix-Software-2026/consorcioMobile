package com.ispc.consorciomobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.ispc.consorciomobile.Comunicado;
import com.ispc.consorciomobile.R;

import java.util.List;

public class ComunicadoAdminAdapter extends RecyclerView.Adapter<ComunicadoAdminAdapter.ComunicadoAdminViewHolder> {

    private Context context;
    private List<Comunicado> listaComunicados;

    public ComunicadoAdminAdapter(Context context, List<Comunicado> listaComunicados) {
        this.context = context;
        this.listaComunicados = listaComunicados;
    }

    @NonNull
    @Override
    public ComunicadoAdminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_comunicado_admin, parent, false);
        return new ComunicadoAdminViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ComunicadoAdminViewHolder holder, int position) {
        Comunicado comunicado = listaComunicados.get(position);

        String titulo = comunicado.getTitulo() != null ? comunicado.getTitulo() : "Sin título";
        if (comunicado.getEsUrgente()) {
            // Mismo resaltado naranja que usaban las tarjetas de ejemplo del panel.
            titulo = "[URGENTE] " + titulo;
            holder.layoutComunicadoAdmin.setBackgroundColor(
                    ContextCompat.getColor(context, R.color.naranjaclaro));
        } else {
            holder.layoutComunicadoAdmin.setBackgroundColor(
                    ContextCompat.getColor(context, R.color.gris_fondo_input));
        }

        holder.tvTituloComunicadoAdmin.setText(titulo);
        holder.tvContenidoComunicadoAdmin.setText(
                comunicado.getContenido() != null ? comunicado.getContenido() : "Sin contenido");
        holder.tvFechaComunicadoAdmin.setText("Publicado: "
                + ComunicadoAdapter.formatearFecha(comunicado.getFechaPublicacion()));
    }

    @Override
    public int getItemCount() {
        return listaComunicados != null ? listaComunicados.size() : 0;
    }

    public static class ComunicadoAdminViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layoutComunicadoAdmin;
        TextView tvTituloComunicadoAdmin, tvContenidoComunicadoAdmin, tvFechaComunicadoAdmin;

        public ComunicadoAdminViewHolder(@NonNull View itemView) {
            super(itemView);
            layoutComunicadoAdmin = itemView.findViewById(R.id.layoutComunicadoAdmin);
            tvTituloComunicadoAdmin = itemView.findViewById(R.id.tvTituloComunicadoAdmin);
            tvContenidoComunicadoAdmin = itemView.findViewById(R.id.tvContenidoComunicadoAdmin);
            tvFechaComunicadoAdmin = itemView.findViewById(R.id.tvFechaComunicadoAdmin);
        }
    }
}
