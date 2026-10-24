package com.ispc.consorciomobile.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ispc.consorciomobile.model.CambioContrasenaRequest;
import com.ispc.consorciomobile.model.Residente;
import com.ispc.consorciomobile.network.ApiService;
import com.ispc.consorciomobile.network.RetrofitClient;
import com.ispc.consorciomobile.network.TokenManager;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MiPerfilViewModel extends ViewModel {

    private static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!_.-])(?=\\S+$).{8,}$";

    private final MutableLiveData<String> usernameLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoadingLiveData = new MutableLiveData<>(false);
    private final MutableLiveData<String> toastMessageLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> passwordUpdatedSuccessLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> sessionClosedLiveData = new MutableLiveData<>();

    private ApiService apiService;

    public MiPerfilViewModel() {
        this.apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
    }

    public MiPerfilViewModel(ApiService apiService) {
        this.apiService = apiService;
    }

    public LiveData<String> getUsernameLiveData() { return usernameLiveData; }
    public LiveData<Boolean> getIsLoadingLiveData() { return isLoadingLiveData; }
    public LiveData<String> getToastMessageLiveData() { return toastMessageLiveData; }
    public LiveData<Boolean> getPasswordUpdatedSuccessLiveData() { return passwordUpdatedSuccessLiveData; }
    public LiveData<Boolean> getSessionClosedLiveData() { return sessionClosedLiveData; }

    public void cargarDatosUsuario() {
        String username = TokenManager.obtenerUsername();
        if (username != null && !username.isEmpty()) {
            usernameLiveData.setValue(username);
        }
    }

    public void validarYActualizarContrasena(String passActual, String passNueva, String passRepetir) {
        if (passActual == null || passActual.trim().isEmpty() ||
                passNueva == null || passNueva.trim().isEmpty() ||
                passRepetir == null || passRepetir.trim().isEmpty()) {
            toastMessageLiveData.setValue("Por favor, completa todos los campos");
            return;
        }

        if (!passNueva.equals(passRepetir)) {
            toastMessageLiveData.setValue("Las nuevas contraseñas no coinciden");
            return;
        }

        if (!passNueva.matches(PASSWORD_REGEX)) {
            toastMessageLiveData.setValue("La contraseña debe tener al menos 8 caracteres, una mayúscula, una minúscula, un número y un carácter especial");
            return;
        }

        isLoadingLiveData.setValue(true);
        CambioContrasenaRequest cambioRequest = new CambioContrasenaRequest(passActual, passNueva);

        apiService.actualizarMiPerfil(cambioRequest).enqueue(new Callback<Residente>() {
            @Override
            public void onResponse(Call<Residente> call, Response<Residente> response) {
                isLoadingLiveData.setValue(false);
                if (response.isSuccessful()) {
                    toastMessageLiveData.setValue("Contraseña actualizada con éxito");
                    passwordUpdatedSuccessLiveData.setValue(true);
                } else {
                    try {
                        // Leemos el JSON de error de Retrofit
                        String errorJsonString = response.errorBody().string();
                        JSONObject errorJson = new JSONObject(errorJsonString);
                        StringBuilder mensajeError = new StringBuilder();

                        // Buscamos si el error es de la contraseña actual
                        if (errorJson.has("current_password")) {
                            mensajeError.append(errorJson.getJSONArray("current_password").getString(0));
                        }
                        // Buscamos si el error es de la nueva contraseña
                        else if (errorJson.has("new_password")) {
                            mensajeError.append(errorJson.getJSONArray("new_password").getString(0));
                        }
                        // Por si manda otro error inesperado
                        else {
                            mensajeError.append("Verifica los datos ingresados.");
                        }

                        // Enviamos el mensaje dinámico a tu LiveData
                        toastMessageLiveData.setValue(mensajeError.toString());

                    } catch (Exception e) {
                        // Fallback en caso de que el JSON no se pueda leer
                        toastMessageLiveData.setValue("Error al actualizar la contraseña.");
                    }
                }
            }

            @Override
            public void onFailure(Call<Residente> call, Throwable t) {
                isLoadingLiveData.setValue(false);
                toastMessageLiveData.setValue("Error de conexión: " + t.getMessage());
            }
        });
    }

    public void cerrarSesionSegura() {
        TokenManager.limpiarToken();
        toastMessageLiveData.setValue("Cerrando Sesión...");
        sessionClosedLiveData.setValue(true);
    }
}
