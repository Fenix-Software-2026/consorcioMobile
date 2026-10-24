package com.ispc.consorciomobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.ispc.consorciomobile.R;
import com.ispc.consorciomobile.model.Residente;
import java.util.ArrayList;
import java.util.List;

public class UsuarioAdapter extends RecyclerView.Adapter<UsuarioAdapter.UsuarioViewHolder> {

    public void actualizar(List<Residente> nuevosUsuarios) {
        this.usuarios.clear();
        if (nuevosUsuarios != null) {
            this.usuarios.addAll(nuevosUsuarios);
        }
        notifyDataSetChanged(); // <--- ¡Esto es lo que obliga a la pantalla a dibujarlos!
    }

    public interface OnUsuarioAcciones {
        void onEditar(Residente usuario);
        void onEliminar(Residente usuario);
    }
    private final Context context;
    private final List<Residente> usuarios = new ArrayList<>();
    private final OnUsuarioAcciones acciones;


    public UsuarioAdapter(Context context, List<Residente> usuarios, OnUsuarioAcciones acciones) {
        this.context = context;
        this.acciones = acciones;
        if (usuarios != null) {
            this.usuarios.addAll(usuarios);
        }
    }
    @NonNull
    @Override
    public UsuarioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_usuario, parent, false);
        return new UsuarioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsuarioAdapter.UsuarioViewHolder holder, int position) {
        // 1. Obtenemos el residente que corresponde a esta posición exacta de la lista
        Residente usuario = usuarios.get(position);

        // 2. Llenamos los componentes visuales con los datos del usuario
        holder.tvNombreUsuarioItem.setText(usuario.getNombre() + usuario.getApellido());
        holder.tvUsernameUsuarioItem.setText("@" + usuario.getUsername());
        holder.tvEmailUsuarioItem.setText(usuario.getEmail());
        holder.tvUnidadUsuarioItem.setText("Unidad #" + usuario.getUnidad());

        // 3. Configuramos el botón EDITAR para que avise a la Activity
        holder.btnEditarUsuario.setOnClickListener(v -> {
            if (acciones != null) {
                acciones.onEditar(usuario);
            }
        });

        // 4. Configuramos el botón ELIMINAR para que avise a la Activity
        holder.btnEliminarUsuario.setOnClickListener(v -> {
            if (acciones != null) {
                acciones.onEliminar(usuario);
            }
        });
    }

    @Override
    public int getItemCount() {
        return usuarios != null ? usuarios.size() : 0;
    }



    public static class UsuarioViewHolder extends RecyclerView.ViewHolder{

        //vistas
        TextView tvNombreUsuarioItem;
        TextView tvUsernameUsuarioItem;
        TextView tvEmailUsuarioItem;
        TextView tvUnidadUsuarioItem;
        Button btnEditarUsuario;
        Button btnEliminarUsuario;

        public UsuarioViewHolder(@NonNull View itemView){
            super(itemView);
            tvNombreUsuarioItem = itemView.findViewById(R.id.tvNombreUsuarioItem);
            tvUsernameUsuarioItem = itemView.findViewById(R.id.tvUsernameUsuarioItem);
            tvEmailUsuarioItem = itemView.findViewById(R.id.tvEmailUsuarioItem);
            tvUnidadUsuarioItem = itemView.findViewById(R.id.tvUnidadUsuarioItem);
            btnEditarUsuario = itemView.findViewById(R.id.btnEditarUsuario);
            btnEliminarUsuario = itemView.findViewById(R.id.btnEliminarUsuario);
        }
    }
}
