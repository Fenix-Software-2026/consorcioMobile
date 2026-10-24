package com.ispc.consorciomobile.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ispc.consorciomobile.model.Residente;
import com.ispc.consorciomobile.network.ApiService;
import com.ispc.consorciomobile.network.RetrofitClient;
import com.ispc.consorciomobile.network.TokenManager;

import java.util.ArrayList;
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
        String tokenActual = TokenManager.obtenerToken();
        android.util.Log.d("DEBUG_TOKEN", "Token actual en memoria: " + tokenActual);
    }

    public LiveData<List<Residente>> getListaUsuarios() { return listaUsuariosLiveData; }
    public LiveData<Boolean> getIsLoadingLiveData() { return isLoadingLiveData; }
    public LiveData<String> getToastMessageLiveData() { return toastMessageLiveData; }

    public void cargarUsuarios() {
        isLoadingLiveData.setValue(true); // Prendemos el cartel de "Cargando..."

           apiService.obtenerResidentes().enqueue(new Callback<List<Residente>>() {
            @Override
            public void onResponse(Call<List<Residente>> call, Response<List<Residente>> response) {
                isLoadingLiveData.setValue(false); // Apagamos el "Cargando..."
                if (response.isSuccessful() && response.body() != null) {
                    List<Residente> listaCompleta = response.body();
                    List<Residente> listaFiltrada = new ArrayList<>();

                    for (Residente r : listaCompleta) {
                        // Solo dejamos pasar a los que tienen una unidad asignada
                        if (r.getUnidad()!= null) {
                            listaFiltrada.add(r);
                        }
                    }
                    //  Guardamos la lista que trajo la API en el LiveData
                    listaUsuariosLiveData.setValue(listaFiltrada);


                } else {
                    toastMessageLiveData.setValue("Error al cargar los usuarios.");
                }
            }

            @Override
            public void onFailure(Call<List<Residente>> call, Throwable t) {
                isLoadingLiveData.setValue(false);
                toastMessageLiveData.setValue("Error de conexión: " + t.getMessage());
            }
        });
    }

    // Baja definitiva del residente. Al eliminarlo, su unidad queda libre.
    public void eliminarUsuario(int idUsuario) {
        apiService.borrarResidente(idUsuario).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    toastMessageLiveData.setValue("Residente eliminado");
                    cargarUsuarios(); // Refrescamos la lista con el padrón actualizado
                } else {
                    toastMessageLiveData.setValue("No se pudo eliminar. Código: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                toastMessageLiveData.setValue("Error de conexión: " + t.getMessage());
            }
        });
    }

}
