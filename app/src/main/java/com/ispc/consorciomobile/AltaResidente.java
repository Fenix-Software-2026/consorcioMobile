package com.ispc.consorciomobile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import com.ispc.consorciomobile.model.Residente;
import com.ispc.consorciomobile.model.Unidad;
import com.ispc.consorciomobile.network.ApiService;
import com.ispc.consorciomobile.network.RetrofitClient;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AltaResidente extends AppCompatActivity {

    private EditText etUsername, etEmail, etFirstName, etLastName;
    private Spinner etUnidad;
    private Button btnGuardar;
    TextView btnVolver;
    private final List<Unidad> unidades = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alta_residente);

        // Relacionar cada control del XML con una variable Java.
        etUsername = findViewById(R.id.etUsuarioInquilino);
        etFirstName = findViewById(R.id.etNombreInquilino);
        etEmail = findViewById(R.id.etEmailInquilino);
        etLastName = findViewById(R.id.etApellidoInquilino);
        etUnidad = findViewById(R.id.spUnidadFuncional);
        btnGuardar = findViewById(R.id.btnCrearInquilino);
        btnVolver = findViewById(R.id.btnVolverBarra);

        cargarUnidades();

        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnGuardar.setOnClickListener(v -> registrarResidente());


        TextView txtTitulo = findViewById(R.id.txtTituloBarra);
        txtTitulo.setText("Usuarios");
    }

    private void cargarUnidades() {
        // Las unidades y los residentes se obtienen del backend para excluir las ocupadas.
        ApiService apiService = RetrofitClient
                .getRetrofitInstance()
                .create(ApiService.class);

        btnGuardar.setEnabled(false);
        apiService.obtenerUnidades().enqueue(new Callback<List<Unidad>>() {
            @Override
            public void onResponse(Call<List<Unidad>> call, Response<List<Unidad>> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    mostrarErrorCarga("No se pudieron cargar las unidades. Código: "
                            + response.code());
                    return;
                }

                cargarResidentesYFiltrarUnidades(apiService, response.body());
            }

            @Override
            public void onFailure(Call<List<Unidad>> call, Throwable t) {
                mostrarErrorCarga("Error al cargar unidades: " + t.getMessage());
            }
        });
    }

    private void cargarResidentesYFiltrarUnidades(
            ApiService apiService,
            List<Unidad> todasLasUnidades) {
        apiService.obtenerResidentes().enqueue(new Callback<List<Residente>>() {
            @Override
            public void onResponse(
                    Call<List<Residente>> call,
                    Response<List<Residente>> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    mostrarErrorCarga("No se pudieron consultar los residentes. Código: "
                            + response.code());
                    return;
                }

                // Guardar los IDs que ya están asignados a un residente.
                Set<Integer> unidadesOcupadas = new HashSet<>();
                for (Residente residente : response.body()) {
                    unidadesOcupadas.add(residente.getUnidad());
                }

                unidades.clear();
                for (Unidad unidad : todasLasUnidades) {
                    if (!unidadesOcupadas.contains(unidad.getId())) {
                        unidades.add(unidad);
                    }
                }

                mostrarUnidadesDisponibles();
            }

            @Override
            public void onFailure(Call<List<Residente>> call, Throwable t) {
                mostrarErrorCarga("Error al consultar residentes: " + t.getMessage());
            }
        });
    }

    private void mostrarUnidadesDisponibles() {
        List<String> nombres = new ArrayList<>();
        nombres.add("Seleccionar unidad");
        for (Unidad unidad : unidades) {
            nombres.add(unidad.toString());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                AltaResidente.this,
                android.R.layout.simple_spinner_item,
                nombres
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        etUnidad.setAdapter(adapter);
        btnGuardar.setEnabled(!unidades.isEmpty());

        if (unidades.isEmpty()) {
            Toast.makeText(this,
                    "No hay unidades disponibles",
                    Toast.LENGTH_LONG).show();
        }
    }

    private void mostrarErrorCarga(String mensaje) {
        btnGuardar.setEnabled(false);
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
    }

    private void registrarResidente() {
        // Leer los valores ingresados y quitar espacios innecesarios.
        String username = etUsername.getText().toString().trim();
        String firstName = etFirstName.getText().toString().trim();
        String lastName = etLastName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        int unidadSeleccionada = etUnidad.getSelectedItemPosition();

        // La posición 0 corresponde a "Seleccionar unidad".
        if (username.isEmpty() || firstName.isEmpty() || lastName.isEmpty()
                || email.isEmpty() || unidadSeleccionada == 0) {
            Toast.makeText(this, "Completá todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Se envía el ID real de la unidad libre, no la posición del Spinner.
        int unidad = unidades.get(unidadSeleccionada - 1).getId();

        // Crear el objeto que Retrofit convertirá en el cuerpo del POST.
        Residente residente = new Residente(
                username,
                email,
                firstName,
                lastName,
                unidad
        );

        // Obtener la interfaz con los endpoints definidos para la API.
        ApiService apiService = RetrofitClient
                .getRetrofitInstance()
                .create(ApiService.class);

        // Evitar envíos duplicados mientras la API procesa la solicitud.
        btnGuardar.setEnabled(false);
        apiService.crearResidente(residente).enqueue(new Callback<Residente>() {
            @Override
            public void onResponse(Call<Residente> call, Response<Residente> response) {
                btnGuardar.setEnabled(true);
                if (response.isSuccessful()) {
                    // HTTP 2xx: el backend creó el residente correctamente.
                    Toast.makeText(AltaResidente.this,
                            "Residente creado correctamente",
                            Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    // El servidor respondió, pero rechazó la solicitud.
                    String detalle = obtenerDetalleError(response);
                    Toast.makeText(AltaResidente.this,
                            "No se pudo crear el residente (" + response.code()
                                    + "): " + detalle,
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Residente> call, Throwable t) {
                // No hubo respuesta: revisar conexión, URL o servidor.
                btnGuardar.setEnabled(true);
                Toast.makeText(AltaResidente.this,
                        "Error de conexión: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private String obtenerDetalleError(Response<Residente> response) {
        if (response.errorBody() == null) {
            return "revisá los datos ingresados";
        }

        try {
            // El serializer devuelve errores agrupados por campo, por ejemplo:
            // {"unidad": ["Ya existe un/a usuario con este/a unidad."]}
            JSONObject errorJson = new JSONObject(response.errorBody().string());
            StringBuilder detalle = new StringBuilder();

            Iterator<String> campos = errorJson.keys();
            while (campos.hasNext()) {
                String campo = campos.next();
                JSONArray mensajes = errorJson.optJSONArray(campo);
                if (mensajes != null && mensajes.length() > 0) {
                    if (detalle.length() > 0) {
                        detalle.append(" ");
                    }
                    detalle.append(campo)
                            .append(": ")
                            .append(mensajes.optString(0));
                }
            }

            return detalle.length() > 0
                    ? detalle.toString()
                    : "revisá los datos ingresados";
        } catch (Exception exception) {
            // Si la respuesta no tiene el formato JSON esperado, mostramos un mensaje general.
            return "revisá los datos ingresados";
        }
    }
}