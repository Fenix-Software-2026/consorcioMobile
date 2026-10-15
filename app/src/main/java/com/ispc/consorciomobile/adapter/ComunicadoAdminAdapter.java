package com.ispc.consorciomobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.ispc.consorciomobile.Comunicado;
import com.ispc.consorciomobile.R;

import java.util.List;

public class ComunicadoAdminAdapter extends RecyclerView.Adapter<ComunicadoAdminAdapter.ComunicadoAdminViewHolder> {

    // La pantalla admin implementa esta interfaz para atender los botones de cada fila.
    public interface OnComunicadoAdminActions {
        void onEditar(Comunicado comunicado);
        void onBorrar(Comunicado comunicado);
    }

    private Context context;
    private List<Comunicado> listaComunicados;
    private OnComunicadoAdminActions acciones;

    public ComunicadoAdminAdapter(Context context, List<Comunicado> listaComunicados,
                                  OnComunicadoAdminActions acciones) {
        this.context = context;
        this.listaComunicados = listaComunicados;
        this.acciones = acciones;
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

        // Los botones avisan a la activity qué comunicado quieren editar o borrar.
        holder.btnEditarComunicado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acciones.onEditar(comunicado);
            }
        });
        holder.btnBorrarComunicado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acciones.onBorrar(comunicado);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaComunicados != null ? listaComunicados.size() : 0;
    }

    public static class ComunicadoAdminViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layoutComunicadoAdmin;
        TextView tvTituloComunicadoAdmin, tvContenidoComunicadoAdmin, tvFechaComunicadoAdmin;
        Button btnEditarComunicado, btnBorrarComunicado;

        public ComunicadoAdminViewHolder(@NonNull View itemView) {
            super(itemView);
            layoutComunicadoAdmin = itemView.findViewById(R.id.layoutComunicadoAdmin);
            tvTituloComunicadoAdmin = itemView.findViewById(R.id.tvTituloComunicadoAdmin);
            tvContenidoComunicadoAdmin = itemView.findViewById(R.id.tvContenidoComunicadoAdmin);
            tvFechaComunicadoAdmin = itemView.findViewById(R.id.tvFechaComunicadoAdmin);
            btnEditarComunicado = itemView.findViewById(R.id.btnEditarComunicado);
            btnBorrarComunicado = itemView.findViewById(R.id.btnBorrarComunicado);
        }
    }
}
