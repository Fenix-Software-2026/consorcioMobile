package com.ispc.consorciomobile;


import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.ispc.consorciomobile.model.Reclamo;
import com.ispc.consorciomobile.network.ApiService;
import com.ispc.consorciomobile.network.RetrofitClient;

import android.content.Intent;

import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

import android.view.View;

public class
DashboardResidenteActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;

    private TextView tvSaludo;

    private TextView tvCantidadReclamos;

    private TextView tvCantidadComunicados;

    private TextView tvTituloComunicado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_residente);

        tvSaludo = findViewById(R.id.tvSaludo);
        cargarPerfilResidente();

        tvCantidadReclamos = findViewById(R.id.tvCantidadReclamos);
        cargarReclamosActivos();

        tvCantidadComunicados = findViewById(R.id.tvCantidadComunicados);
        tvTituloComunicado = findViewById(R.id.tvTituloComunicado);

        View cardUltimoComunicado = findViewById(R.id.cardUltimoComunicado);

        cardUltimoComunicado.setOnClickListener(view -> {
            Intent intent = new Intent(
                    DashboardResidenteActivity.this,
                    ComunicadosResidenteActivity.class
            );
            startActivity(intent);
        });

        cargarCantidadComunicados();

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
                .getRetrofitInstance()
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
                .getRetrofitInstance()
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

    private void cargarCantidadComunicados() {
        ApiService apiService = RetrofitClient
                .getRetrofitInstance()
                .create(ApiService.class);

        apiService.obtenerComunicados().enqueue(new Callback<List<Comunicado>>() {
            @Override
            public void onResponse(
                    Call<List<Comunicado>> call,
                    Response<List<Comunicado>> response
            ) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Comunicado> comunicados = response.body();

                    // Actualiza el número de la tarjeta de resumen.
                    tvCantidadComunicados.setText(
                            String.valueOf(comunicados.size())
                    );

                    // Si la lista está vacía, no hay un comunicado para mostrar.
                    if (comunicados.isEmpty()) {
                        tvTituloComunicado.setText(
                                "No hay comunicados recientes"
                        );
                        return;
                    }

                    // Busca el comunicado con la fecha de publicación más reciente.
                    Comunicado ultimo = comunicados.get(0);

                    for (Comunicado comunicado : comunicados) {
                        String fecha = comunicado.getFechaPublicacion();
                        String fechaUltimo = ultimo.getFechaPublicacion();

                        if (fecha != null
                                && (fechaUltimo == null
                                || fecha.compareTo(fechaUltimo) > 0)) {
                            ultimo = comunicado;
                        }
                    }

                    // Muestra un resumen del contenido del último comunicado.
                    tvTituloComunicado.setText(
                            resumirContenido(ultimo.getTitulo())
                    );

                } else {
                    tvCantidadComunicados.setText("--");
                    tvTituloComunicado.setText(
                            "No se pudieron cargar los comunicados"
                    );

                    Toast.makeText(
                            DashboardResidenteActivity.this,
                            "Error al cargar comunicados. Código: "
                                    + response.code(),
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }

            @Override
            public void onFailure(Call<List<Comunicado>> call, Throwable t) {
                tvCantidadComunicados.setText("--");
                tvTituloComunicado.setText(
                        "No se pudo conectar con el servidor"
                );
            }
        });

    }

    private String resumirContenido(String contenido) {
        if (contenido == null || contenido.trim().isEmpty()) {
            return "Este comunicado no tiene contenido.";
        }

        // Unifica espacios y saltos de línea.
        String texto = contenido.trim().replaceAll("\\s+", " ");
        String[] palabras = texto.split(" ");

        if (palabras.length <= 10) {
            return texto;
        }

        StringBuilder resumen = new StringBuilder();

        for (int i = 0; i < 10; i++) {
            if (i > 0) {
                resumen.append(" ");
            }

            resumen.append(palabras[i]);
        }

        return resumen.append("...").toString();
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
