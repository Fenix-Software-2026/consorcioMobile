package com.ispc.consorciomobile.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ispc.consorciomobile.ValidadorLogin;
import com.ispc.consorciomobile.data.AuthRepository;

public class LoginViewModel extends ViewModel {

    private final AuthRepository authRepository;

    private final MutableLiveData<Boolean> loginExitosoLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> rolLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoadingLiveData = new MutableLiveData<>(false);

    public LoginViewModel() {
        this(new AuthRepository());
    }

    public LoginViewModel(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public LiveData<Boolean> getLoginExitosoLiveData() { return loginExitosoLiveData; }
    public LiveData<String> getErrorLiveData() { return errorLiveData; }
    public LiveData<String> getRolLiveData() { return rolLiveData; }
    public LiveData<Boolean> getIsLoadingLiveData() { return isLoadingLiveData; }

    public void iniciarSesion(String username, String password) {
        if (!ValidadorLogin.validarFormulario(username, password)) {
            errorLiveData.setValue("Completá usuario y contraseña");
            return;
        }

        isLoadingLiveData.setValue(true);

        authRepository.login(username.trim(), password, new AuthRepository.LoginCallback() {
            @Override
            public void onSuccess(String rol, String usernameToken) {
                isLoadingLiveData.setValue(false);
                rolLiveData.setValue(rol);
                loginExitosoLiveData.setValue(true);
            }

            @Override
            public void onErrorCredenciales() {
                isLoadingLiveData.setValue(false);
                errorLiveData.setValue("Usuario o contraseña incorrectos");
            }

            @Override
            public void onErrorTokenInvalido() {
                isLoadingLiveData.setValue(false);
                errorLiveData.setValue("El token recibido no es válido");
            }

            @Override
            public void onErrorConexion() {
                isLoadingLiveData.setValue(false);
                errorLiveData.setValue("No se pudo conectar con el servidor");
            }
        });
    }
}