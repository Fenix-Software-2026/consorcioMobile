package com.ispc.consorciomobile;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ispc.consorciomobile.adapter.UsuarioAdapter;
import com.ispc.consorciomobile.model.Residente;
import com.ispc.consorciomobile.viewmodel.UsuariosViewModel;

import java.util.ArrayList;

public class UsuariosAdminActivity extends AppCompatActivity implements UsuarioAdapter.OnUsuarioAcciones {
    private TextView btnVolver;
    private Button btnNuevoUsuario;
    private UsuariosViewModel viewModel; // (O el nombre exacto de tu clase ViewModel)
    private UsuarioAdapter adapter;
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuarios_admin);

        RecyclerView recyclerView = findViewById(R.id.recyclerUsuarios);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new UsuarioAdapter(this, new ArrayList<>(), this);
        recyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(UsuariosViewModel.class);

        viewModel.getListaUsuarios().observe(this, residentes -> {
            adapter.actualizar(residentes); // ¡Esto inyecta los datos reales en la lista!
        });

        // Avisamos el resultado de la baja (o el error) con un Toast.
        viewModel.getToastMessageLiveData().observe(this, mensaje -> {
            if (mensaje != null && !mensaje.isEmpty()) {
                Toast.makeText(UsuariosAdminActivity.this, mensaje, Toast.LENGTH_SHORT).show();
            }
        });

        btnVolver = findViewById(R.id.btnVolverBarra);
        btnVolver.setOnClickListener(view -> finish());
        TextView txtTituloBarra = findViewById(R.id.txtTituloBarra);
        txtTituloBarra.setText(R.string.titulo_usuarios);

        btnNuevoUsuario = findViewById(R.id.btnNuevoUsuario);
        btnNuevoUsuario.setOnClickListener(view -> abrirFormulario(null));



    }

    @Override
    protected void onResume() {
        super.onResume();
        // Al volver del formulario (alta o edición) refrescamos la lista.
        viewModel.cargarUsuarios();
    }

    // null = alta de un usuario nuevo; con datos = edición.
    private void abrirFormulario(Residente usuario) {
        Intent intent = new Intent(UsuariosAdminActivity.this, AltaResidente.class);
        if (usuario != null) {
            intent.putExtra(AltaResidente.EXTRA_ID_USUARIO, usuario.getId());
            intent.putExtra(AltaResidente.EXTRA_USERNAME, usuario.getUsername());
            intent.putExtra(AltaResidente.EXTRA_NOMBRE, usuario.getNombre());
            intent.putExtra(AltaResidente.EXTRA_APELLIDO, usuario.getApellido());
            intent.putExtra(AltaResidente.EXTRA_EMAIL, usuario.getEmail());
        }
        startActivity(intent);
    }

    @Override
    public void onEditar(Residente usuario) {
        abrirFormulario(usuario);
    }

    @Override
    public void onEliminar(Residente usuario) {
        // Confirmación previa: la baja es definitiva.
        new AlertDialog.Builder(this)
                .setTitle(R.string.dialogo_eliminar_titulo)
                .setMessage(getString(R.string.dialogo_eliminar_mensaje, nombreCompleto(usuario)))
                .setPositiveButton(R.string.dialogo_eliminar_confirmar,
                        (dialog, which) -> viewModel.eliminarUsuario(usuario.getId()))
                .setNegativeButton(R.string.dialogo_eliminar_cancelar, null)
                .show();
    }

    // Nombre y apellido para mostrar en el diálogo, sin "null" si faltan.
    private String nombreCompleto(Residente usuario) {
        String nombre = usuario.getNombre() == null ? "" : usuario.getNombre().trim();
        String apellido = usuario.getApellido() == null ? "" : usuario.getApellido().trim();
        String completo = (nombre + " " + apellido).trim();
        return completo.isEmpty() ? usuario.getUsername() : completo;
    }
}