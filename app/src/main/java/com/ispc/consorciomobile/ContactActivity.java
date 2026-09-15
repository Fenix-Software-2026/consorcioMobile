package com.ispc.consorciomobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

public class ContactActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        TextView textoTelefono = findViewById(R.id.textoTelefono);

        textoTelefono.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:+543515550123"));
            startActivity(intent);
        });

        TextView textoCorreo = findViewById(R.id.textoCorreo);

        textoCorreo.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:contacto@consorciomobile.com"));
            startActivity(intent);
        });
    }
}