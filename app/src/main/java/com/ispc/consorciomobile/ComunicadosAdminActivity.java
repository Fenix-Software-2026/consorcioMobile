package com.ispc.consorciomobile;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ispc.consorciomobile.adapter.ComunicadoAdminAdapter;
import com.ispc.consorciomobile.network.ApiService;
import com.ispc.consorciomobile.network.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ComunicadosAdminActivity extends AppCompatActivity
        implements ComunicadoAdminAdapter.OnComunicadoAdminActions {

    private static final String TAG = "COMUNICADOS_ADMIN";
    private EditText etTituloComunicado;
    private EditText etCuerpoComunicado;
    private CheckBox cbUrgente;
    private Button btnPublicar;
    private Button btnCancelarEdicion;
    private RecyclerView recyclerComunicadosAdmin;
    private TextView tvSinComunicadosAdmin;

    // Si tiene valor, el formulario está editando ese comunicado en lugar de crear uno nuevo.
    private Integer idEnEdicion = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comunicados_admin);

        etTituloComunicado = findViewById(R.id.etTituloComunicado);
        etCuerpoComunicado = findViewById(R.id.etCuerpoComunicado);
        cbUrgente = findViewById(R.id.cbUrgente);
        btnPublicar = findViewById(R.id.btnPublicar);
        btnCancelarEdicion = findViewById(R.id.btnCancelarEdicion);

        recyclerComunicadosAdmin = findViewById(R.id.recyclerComunicadosAdmin);
        tvSinComunicadosAdmin = findViewById(R.id.tvSinComunicadosAdmin);
        recyclerComunicadosAdmin.setLayoutManager(new LinearLayoutManager(this));

        // Salimos del panel para volver a la pantalla anterior.
        TextView btnVolver = findViewById(R.id.btnVolverBarra);
        btnVolver.setOnClickListener(view -> finish());

        // Titulo barra
        TextView txtTituloBarra = findViewById(R.id.txtTituloBarra);
        txtTituloBarra.setText("Comunicados");

        // Sale del modo edición sin guardar los cambios.
        btnCancelarEdicion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                limpiarFormulario();
                Toast.makeText(ComunicadosAdminActivity.this,
                        "Edición cancelada", Toast.LENGTH_SHORT).show();
            }
        });

        btnPublicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String titulo = etTituloComunicado.getText().toString().trim();
                String cuerpo = etCuerpoComunicado.getText().toString().trim();
                boolean urgente = cbUrgente.isChecked();

                if (!titulo.isEmpty() && !cuerpo.isEmpty()) {
                    if (idEnEdicion != null) {
                        editarComunicado(idEnEdicion, titulo, cuerpo, urgente);
                    } else {
                        publicarComunicado(titulo, cuerpo, urgente);
                    }
                } else {
                    Toast.makeText(ComunicadosAdminActivity.this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // El adapter nos avisa que el admin quiere modificar este comunicado.
    @Override
    public void onEditar(Comunicado comunicado) {
        idEnEdicion = comunicado.getId();
        etTituloComunicado.setText(comunicado.getTitulo());
        etCuerpoComunicado.setText(comunicado.getContenido());
        cbUrgente.setChecked(comunicado.getEsUrgente());

        // Cambiamos el formulario a modo edición.
        btnPublicar.setText("Guardar cambios");
        btnCancelarEdicion.setVisibility(View.VISIBLE);
        Toast.makeText(this, "Editando comunicado", Toast.LENGTH_SHORT).show();
    }

    // El adapter nos avisa que el admin quiere borrar este comunicado.
    @Override
    public void onBorrar(final Comunicado comunicado) {
        // Confirmación previa para no borrar nada sin querer.
        new AlertDialog.Builder(this)
                .setTitle("Borrar comunicado")
                .setMessage("¿Seguro que querés borrar \"" + comunicado.getTitulo() + "\"?")
                .setPositiveButton("Borrar", (dialog, which) -> borrarComunicado(comunicado))
                .setNegativeButton("Cancelar", null)
                .show();
    }

    // Guarda los cambios del comunicado que se está editando (PUT).
    private void editarComunicado(int id, String titulo, String contenido, boolean urgente) {
        Comunicado comunicadoEditado = new Comunicado();
        comunicadoEditado.setId(id);
        comunicadoEditado.setTitulo(titulo);
        comunicadoEditado.setContenido(contenido);
        comunicadoEditado.setEsUrgente(urgente);

        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        btnPublicar.setEnabled(false);

        apiService.actualizarComunicado(id, comunicadoEditado).enqueue(new Callback<Comunicado>() {
            @Override
            public void onResponse(Call<Comunicado> call, Response<Comunicado> response) {
                btnPublicar.setEnabled(true);
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "Comunicado actualizado con id: " + id);
                    Toast.makeText(ComunicadosAdminActivity.this,
                            "Comunicado actualizado", Toast.LENGTH_SHORT).show();
                    limpiarFormulario();
                    cargarComunicados();
                } else {
                    Log.e(TAG, "Error HTTP: " + response.code());
                    Toast.makeText(ComunicadosAdminActivity.this,
                            "No se pudo actualizar. Error: " + response.code(),
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Comunicado> call, Throwable t) {
                btnPublicar.setEnabled(true);
                Log.e(TAG, "Error de red: " + t.getMessage());
                Toast.makeText(ComunicadosAdminActivity.this,
                        "Fallo la conexión: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    // Borra el comunicado elegido (DELETE). Solo admin puede hacerlo.
    private void borrarComunicado(Comunicado comunicado) {
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);

        apiService.borrarComunicado(comunicado.getId()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "Comunicado borrado con id: " + comunicado.getId());
                    Toast.makeText(ComunicadosAdminActivity.this,
                            "Comunicado borrado", Toast.LENGTH_SHORT).show();
                    cargarComunicados();
                } else {
                    Log.e(TAG, "Error HTTP: " + response.code());
                    Toast.makeText(ComunicadosAdminActivity.this,
                            "No se pudo borrar. Error: " + response.code(),
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(TAG, "Error de red: " + t.getMessage());
                Toast.makeText(ComunicadosAdminActivity.this,
                        "Fallo la conexión: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    // Devuelve el formulario a su estado original (alta de comunicados).
    private void limpiarFormulario() {
        idEnEdicion = null;
        etTituloComunicado.setText("");
        etCuerpoComunicado.setText("");
        cbUrgente.setChecked(false);
        btnPublicar.setText(R.string.btn_publicar_comunicado);
        btnCancelarEdicion.setVisibility(View.GONE);
    }

    // Armamos el comunicado y lo damos de alta. La API solo lo acepta si el
    // token pertenece a un administrador (sino responde 403).
    private void publicarComunicado(String titulo, String contenido, boolean urgente) {
        Comunicado nuevoComunicado = new Comunicado();
        nuevoComunicado.setTitulo(titulo);
        nuevoComunicado.setContenido(contenido);
        nuevoComunicado.setEsUrgente(urgente);

        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        btnPublicar.setEnabled(false);

        apiService.crearComunicado(nuevoComunicado).enqueue(new Callback<Comunicado>() {
            @Override
            public void onResponse(Call<Comunicado> call, Response<Comunicado> response) {
                btnPublicar.setEnabled(true);
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "Comunicado publicado con id: " + response.body().getId());
                    Toast.makeText(ComunicadosAdminActivity.this,
                            "Comunicado publicado", Toast.LENGTH_SHORT).show();

                    // Limpiamos el formulario para el próximo aviso.
                    limpiarFormulario();

                    // Y lo mostramos de una en el historial.
                    cargarComunicados();
                } else {
                    Log.e(TAG, "Error HTTP: " + response.code());
                    Toast.makeText(ComunicadosAdminActivity.this,
                            "No se pudo publicar. Error: " + response.code(),
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Comunicado> call, Throwable t) {
                btnPublicar.setEnabled(true);
                Log.e(TAG, "Error de red: " + t.getMessage());
                Toast.makeText(ComunicadosAdminActivity.this,
                        "Fallo la conexión: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refrescamos el historial cada vez que entramos o volvemos de otra pantalla.
        cargarComunicados();
    }

    private void cargarComunicados() {
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);

        apiService.obtenerComunicados().enqueue(new Callback<List<Comunicado>>() {
            @Override
            public void onResponse(Call<List<Comunicado>> call, Response<List<Comunicado>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Comunicado> comunicados = response.body();
                    Log.d(TAG, "Comunicados recibidos: " + comunicados.size());

                    recyclerComunicadosAdmin.setAdapter(
                            new ComunicadoAdminAdapter(
                                    ComunicadosAdminActivity.this, comunicados,
                                    ComunicadosAdminActivity.this));

                    tvSinComunicadosAdmin.setVisibility(comunicados.isEmpty()
                            ? TextView.VISIBLE : TextView.GONE);
                } else {
                    Log.e(TAG, "Error HTTP: " + response.code());
                    Toast.makeText(ComunicadosAdminActivity.this,
                            "Error HTTP: " + response.code() + " (Verificá el Token JWT)",
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Comunicado>> call, Throwable t) {
                Log.e(TAG, "Error de red: " + t.getMessage());
                Toast.makeText(ComunicadosAdminActivity.this,
                        "Fallo la conexión: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}
