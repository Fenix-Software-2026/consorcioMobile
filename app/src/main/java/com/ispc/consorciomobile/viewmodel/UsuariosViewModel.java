package com.ispc.consorciomobile.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ispc.consorciomobile.model.Residente;
import com.ispc.consorciomobile.network.ApiService;
import com.ispc.consorciomobile.network.RetrofitClient;
import com.ispc.consorciomobile.network.TokenManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsuariosViewModel extends ViewModel {

    private final MutableLiveData<List<Residente>> listaUsuariosLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoadingLiveData = new MutableLiveData<>(false);
    private final MutableLiveData<String> toastMessageLiveData = new MutableLiveData<>();

    private ApiService apiService;

    public UsuariosViewModel() {
        this.apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        cargarUsuarios(); // ¡Apenas nace el ViewModel, va a buscar los usuarios!
        String tokenActual = TokenManager.obtenerToken();
        android.util.Log.d("DEBUG_TOKEN", "Token actual en memoria: " + tokenActual);
    }

    public LiveData<List<Residente>> getListaUsuarios() { return listaUsuariosLiveData; }
    public LiveData<Boolean> getIsLoadingLiveData() { return isLoadingLiveData; }
    public LiveData<String> getToastMessageLiveData() { return toastMessageLiveData; }

    public void cargarUsuarios() {
        isLoadingLiveData.setValue(true);

        apiService.obtenerResidentes().enqueue(new Callback<List<Residente>>() {
            @Override
            public void onResponse(Call<List<Residente>> call, Response<List<Residente>> response) {
                isLoadingLiveData.setValue(false);

                // Imprimimos el código HTTP para ver si respondió 200 OK
                android.util.Log.d("DEBUG_BACKEND", "Código HTTP recibido: " + response.code());

                if (response.isSuccessful() && response.body() != null) {
                    List<Residente> lista = response.body();

                    // ¡Acá imprimimos cuántos usuarios llegaron!
                    android.util.Log.d("DEBUG_BACKEND", "¡Llegaron datos! Cantidad de usuarios: " + lista.size());

                    // Recorremos la lista para ver qué trajo cada uno en el Logcat
                    for (int i = 0; i < lista.size(); i++) {
                        Residente r = lista.get(i);
                        android.util.Log.d("DEBUG_BACKEND", "Usuario [" + i + "]: " + r.getApellido());
                    }

                    // Se los mandamos al LiveData para que la pantalla los dibuje
                    listaUsuariosLiveData.setValue(lista);
                } else {
                    android.util.Log.d("DEBUG_BACKEND", "La respuesta fue exitosa pero el body es NULO o falló.");
                    toastMessageLiveData.setValue("Error al cargar los usuarios.");
                }
            }

            @Override
            public void onFailure(Call<List<Residente>> call, Throwable t) {
                isLoadingLiveData.setValue(false);
                android.util.Log.d("DEBUG_BACKEND", "Fallo total de red: " + t.getMessage());
                toastMessageLiveData.setValue("Error de conexión: " + t.getMessage());
            }
        });
    }

}
