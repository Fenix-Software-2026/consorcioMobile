package com.ispc.consorciomobile;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AltaResidente extends AppCompatActivity {

    public static final String EXTRA_ID_USUARIO = "extra_id_usuario";
    public static final String EXTRA_USERNAME = "extra_username";
    public static final String EXTRA_NOMBRE = "extra_nombre";
    public static final String EXTRA_APELLIDO = "extra_apellido";
    public static final String EXTRA_EMAIL = "extra_email";

    // Si llega un id, la pantalla funciona como edición en lugar de alta.
    private int idEnEdicion = -1;
    // Unidad que ya tiene el usuario que se está editando.
    private Integer unidadActual = null;

    private EditText etUsername, etEmail, etFirstName, etLastName;
    private Spinner etUnidad;
    private Button btnGuardar;
    private ProgressBar progressFormulario;
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
        progressFormulario = findViewById(R.id.progressFormulario);
        btnVolver = findViewById(R.id.btnVolverBarra);

        // Si llega un id, es edición: precargamos los datos que mandó la lista.
        idEnEdicion = getIntent().getIntExtra(EXTRA_ID_USUARIO, -1);
        if (idEnEdicion > 0) {
            etUsername.setText(getIntent().getStringExtra(EXTRA_USERNAME));
            etFirstName.setText(getIntent().getStringExtra(EXTRA_NOMBRE));
            etLastName.setText(getIntent().getStringExtra(EXTRA_APELLIDO));
            etEmail.setText(getIntent().getStringExtra(EXTRA_EMAIL));
        }

        cargarUnidades();

        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // En alta crea (POST); en edición guarda los cambios (PATCH).
        btnGuardar.setOnClickListener(v -> {
            if (idEnEdicion > 0) {
                actualizarResidente();
            } else {
                registrarResidente();
            }
        });

        TextView txtTitulo = findViewById(R.id.txtTituloBarra);
        txtTitulo.setText(idEnEdicion > 0 ? "Editar usuario" : "Usuarios");

        if (idEnEdicion > 0) {
            btnGuardar.setText("Guardar cambios");
            TextView txtFormulario = findViewById(R.id.txtTituloFormulario);
            txtFormulario.setText("Editar inquilino");
        }
    }

    private void cargarUnidades() {
        // Las unidades y los residentes se obtienen del backend para excluir las ocupadas.
        ApiService apiService = RetrofitClient
                .getRetrofitInstance()
                .create(ApiService.class);

        mostrarCargando(true);
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
                    if (residente.getUnidad() == null) {
                        continue;
                    }
                    // Al editar, la unidad del propio usuario debe seguir disponible.
                    if (residente.getId() == idEnEdicion) {
                        unidadActual = residente.getUnidad();
                        continue;
                    }
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
        mostrarCargando(false);

        // En edición dejamos seleccionada la unidad que ya tiene el usuario.
        if (unidadActual != null) {
            for (int i = 0; i < unidades.size(); i++) {
                if (unidades.get(i).getId() == unidadActual) {
                    etUnidad.setSelection(i + 1); // La posición 0 es "Seleccionar unidad"
                    break;
                }
            }
        }

        if (unidades.isEmpty()) {
            Toast.makeText(this,
                    "No hay unidades disponibles",
                    Toast.LENGTH_LONG).show();
        }
    }

    // Muestra el progreso y bloquea el botón mientras se habla con la API.
    private void mostrarCargando(boolean cargando) {
        progressFormulario.setVisibility(cargando ? View.VISIBLE : View.GONE);
        btnGuardar.setEnabled(!cargando && !unidades.isEmpty());
    }

    private void mostrarErrorCarga(String mensaje) {
        mostrarCargando(false);
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
        mostrarCargando(true);
        apiService.crearResidente(residente).enqueue(new Callback<Residente>() {
            @Override
            public void onResponse(Call<Residente> call, Response<Residente> response) {
                mostrarCargando(false);
                if (response.isSuccessful()) {
                    // HTTP 2xx: el backend creó el residente y le envía las
                    // credenciales provisorias por correo.
                    mostrarConfirmacionAlta(email);
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
                mostrarCargando(false);
                Toast.makeText(AltaResidente.this,
                        "Error de conexión: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    // Aviso de alta: confirma la operación e informa que las credenciales van por correo.
    private void mostrarConfirmacionAlta(String email) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.dialogo_alta_titulo)
                .setMessage(getString(R.string.dialogo_alta_mensaje, email))
                .setPositiveButton(R.string.dialogo_alta_aceptar, (dialog, which) -> finish())
                .setCancelable(false)
                .show();
    }

    // Guarda los cambios del residente que se está editando (PATCH).
    private void actualizarResidente() {
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

        // Se envía el ID real de la unidad, no la posición del Spinner.
        int unidad = unidades.get(unidadSeleccionada - 1).getId();

        // PATCH parcial: solo los campos que puede editar el administrador.
        Map<String, Object> cuerpo = new HashMap<>();
        cuerpo.put("username", username);
        cuerpo.put("nombre", firstName);
        cuerpo.put("apellido", lastName);
        cuerpo.put("email", email);
        cuerpo.put("unidad", unidad);

        ApiService apiService = RetrofitClient
                .getRetrofitInstance()
                .create(ApiService.class);

        // Evitar envíos duplicados mientras la API procesa la solicitud.
        mostrarCargando(true);
        apiService.actualizarResidente(idEnEdicion, cuerpo).enqueue(new Callback<Residente>() {
            @Override
            public void onResponse(Call<Residente> call, Response<Residente> response) {
                mostrarCargando(false);
                if (response.isSuccessful()) {
                    Toast.makeText(AltaResidente.this,
                            "Cambios guardados",
                            Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    String detalle = obtenerDetalleError(response);
                    Toast.makeText(AltaResidente.this,
                            "No se pudieron guardar los cambios (" + response.code()
                                    + "): " + detalle,
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Residente> call, Throwable t) {
                mostrarCargando(false);
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
            return "revisá los datos ingresados";
        }
    }
}