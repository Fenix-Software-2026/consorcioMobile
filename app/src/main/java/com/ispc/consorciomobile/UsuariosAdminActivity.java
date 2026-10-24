package com.ispc.consorciomobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

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
        viewModel.cargarUsuarios(); // Dispara el GET

        btnVolver = findViewById(R.id.btnVolverBarra);
        btnVolver.setOnClickListener(view -> finish());
        TextView txtTituloBarra = findViewById(R.id.txtTituloBarra);
        txtTituloBarra.setText(R.string.titulo_usuarios);

        btnNuevoUsuario = findViewById(R.id.btnNuevoUsuario);
        btnNuevoUsuario.setOnClickListener(view -> abrirFormulario(0));



    }

    private void abrirFormulario(int idUsuario) {
        Intent intent = new Intent(UsuariosAdminActivity.this, AltaResidente.class);
        if (idUsuario > 0) {
            intent.putExtra(AltaResidente.EXTRA_ID_USUARIO, idUsuario);
        }
        startActivity(intent);
    }

    @Override
    public void onEditar(Residente usuario) {

    }

    @Override
    public void onEliminar(Residente usuario) {

    }
}