package com.ispc.consorciomobile;


import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import android.content.Intent;

import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

public class
DashboardResidenteActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;

    private TextView tvSaludo;

    private TextView tvCantidadReclamos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_residente);

        tvSaludo = findViewById(R.id.tvSaludo);
        cargarPerfilResidente();

        tvCantidadReclamos = findViewById(R.id.tvCantidadReclamos);
        cargarReclamosActivos();

        drawerLayout = findViewById(R.id.drawerLayout);

        ImageButton btnMenu = findViewById(R.id.btnMenu);
        NavigationView navigationView = findViewById(R.id.navigationView);

        // Abre el menú lateral al tocar el botón hamburguesa.
        btnMenu.setOnClickListener(view ->
                drawerLayout.openDrawer(GravityCompat.START)
        );

        // Controla las opciones seleccionadas del menú.
        navigationView.setNavigationItemSelectedListener(item -> {

            if (item.getItemId() == R.id.nav_inicio) {
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }

            if (item.getItemId() == R.id.nav_reclamos) {
                Intent intent = new Intent(
                        DashboardResidenteActivity.this,
                        MisReclamosActivity.class
                );

                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(intent);
                return true;
            }

            if (item.getItemId() == R.id.nav_perfil) {
                Intent intent = new Intent(DashboardResidenteActivity.this, MiPerfilActivity.class);

                // Cierra el menú lateral con una animación suave
                drawerLayout.closeDrawer(GravityCompat.START);

                // Abre tu pantalla
                startActivity(intent);
                return true;
            }

            if (item.getItemId() == R.id.nav_comunicados) {
                Intent intent = new Intent(DashboardResidenteActivity.this, ComunicadosResidenteActivity.class);
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(intent);
                return true;
            }

            if (item.getItemId() == R.id.nav_contacto) {
                Intent intent = new Intent(DashboardResidenteActivity.this, ContactActivity.class);
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(intent);
                return true;
            }

            return false;
        });
    }

    private void cargarPerfilResidente() {
        ApiService apiService = RetrofitClient
                .getRetrofitInstance(this)
                .create(ApiService.class);

        apiService.obtenerMiPerfil().enqueue(new Callback<PerfilResidente>() {
            @Override
            public void onResponse(
                    Call<PerfilResidente> call,
                    Response<PerfilResidente> response
            ) {
                if (response.isSuccessful() && response.body() != null) {
                    PerfilResidente perfil = response.body();
                    String nombre = perfil.getNombre();

                    if (nombre == null || nombre.trim().isEmpty()) {
                        nombre = perfil.getUsername();
                    }

                    if (nombre == null || nombre.trim().isEmpty()) {
                        nombre = "residente";
                    }

                    tvSaludo.setText("¡Bienvenido, " + nombre + "!");
                } else {
                    Toast.makeText(
                            DashboardResidenteActivity.this,
                            "No se pudo cargar el perfil. Código: " + response.code(),
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }

            @Override
            public void onFailure(Call<PerfilResidente> call, Throwable t) {
                Toast.makeText(
                        DashboardResidenteActivity.this,
                        "No se pudo conectar con el servidor.",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }

    private void cargarReclamosActivos() {
        ApiService apiService = RetrofitClient
                .getRetrofitInstance(this)
                .create(ApiService.class);

        apiService.obtenerReclamos().enqueue(new Callback<List<Reclamo>>() {
            @Override
            public void onResponse(
                    Call<List<Reclamo>> call,
                    Response<List<Reclamo>> response
            ) {
                if (response.isSuccessful() && response.body() != null) {
                    int cantidadActivos = 0;

                    for (Reclamo reclamo : response.body()) {
                        String estado = reclamo.getEstado();

                        if ("pendiente".equalsIgnoreCase(estado)
                                || "en_proceso".equalsIgnoreCase(estado)) {
                            cantidadActivos++;
                        }
                    }

                    tvCantidadReclamos.setText(String.valueOf(cantidadActivos));
                } else {
                    tvCantidadReclamos.setText("--");

                    Toast.makeText(
                            DashboardResidenteActivity.this,
                            "No se pudieron cargar los reclamos. Código: "
                                    + response.code(),
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }

            @Override
            public void onFailure(Call<List<Reclamo>> call, Throwable t) {
                tvCantidadReclamos.setText("--");

                Toast.makeText(
                        DashboardResidenteActivity.this,
                        "No se pudo conectar con el servidor.",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }
    @Override
    public void onBackPressed() {
        // Si el menú está abierto, Atrás solamente lo cierra.
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
