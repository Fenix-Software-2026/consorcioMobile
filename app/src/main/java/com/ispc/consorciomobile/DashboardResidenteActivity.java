package com.ispc.consorciomobile;


import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import android.content.Intent;

public class DashboardResidenteActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_residente);

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

            return false;
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