package com.ispc.consorciomobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MisReclamosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_reclamos);

        Button btn_reclamos_volver = findViewById(R.id.btn_reclamos_volver);

        Button btn_crear_reclamo_activity = findViewById(R.id.btn_crear_reclamo_activity);

        btn_reclamos_volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MisReclamosActivity.this,  DashboardResidenteActivity.class
                );
                startActivity(intent);
            }});

            btn_crear_reclamo_activity.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v){
            Intent intent = new Intent(MisReclamosActivity.this, DashboardResidenteActivity.class);
            startActivity(intent);
        }});
}
}


