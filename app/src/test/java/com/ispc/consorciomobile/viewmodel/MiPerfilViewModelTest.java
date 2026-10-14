package com.ispc.consorciomobile.viewmodel;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.ispc.consorciomobile.model.CambioContrasenaRequest;
import com.ispc.consorciomobile.model.Residente;
import com.ispc.consorciomobile.network.ApiService;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MiPerfilViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private ApiService mockApiService;

    @Mock
    private Call<Residente> mockCall;

    private MiPerfilViewModel viewModel;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        viewModel = new MiPerfilViewModel(mockApiService);
    }

    @Test
    public void validarYActualizarContrasena_camposVacios_emiteMensajeError() {
        viewModel.validarYActualizarContrasena("", "Pass1234!", "Pass1234!");
        assertEquals("Por favor, completa todos los campos", viewModel.getToastMessageLiveData().getValue());
    }

    @Test
    public void validarYActualizarContrasena_contrasenasNoCoinciden_emiteMensajeError() {
        viewModel.validarYActualizarContrasena("PassActual123!", "NuevaPass123!", "OtraPass123!");
        assertEquals("Las nuevas contraseñas no coinciden", viewModel.getToastMessageLiveData().getValue());
    }

    @Test
    public void validarYActualizarContrasena_contrasenaDebil_emiteMensajeError() {
        viewModel.validarYActualizarContrasena("PassActual123!", "12345678", "12345678");
        assertEquals("La contraseña debe tener al menos 8 caracteres, una mayúscula, una minúscula, un número y un carácter especial",
                viewModel.getToastMessageLiveData().getValue());
    }

    @Test
    public void validarYActualizarContrasena_exitosa_llamaApiServiceYEmiteExito() {
        when(mockApiService.actualizarMiPerfil(any(CambioContrasenaRequest.class))).thenReturn(mockCall);

        viewModel.validarYActualizarContrasena("Actual123!", "NuevaPass123!", "NuevaPass123!");

        ArgumentCaptor<Callback<Residente>> captor = ArgumentCaptor.forClass(Callback.class);
        verify(mockCall).enqueue(captor.capture());

        Response<Residente> responseExitosa = Response.success(new Residente("testuser", "test@email.com", "Juan", "Perez", 1));
        captor.getValue().onResponse(mockCall, responseExitosa);

        assertEquals("Contraseña actualizada con éxito", viewModel.getToastMessageLiveData().getValue());
        assertEquals(Boolean.TRUE, viewModel.getPasswordUpdatedSuccessLiveData().getValue());
        assertEquals(Boolean.FALSE, viewModel.getIsLoadingLiveData().getValue());
    }

    @Test
    public void validarYActualizarContrasena_errorApi_emiteMensajeError() {
        when(mockApiService.actualizarMiPerfil(any(CambioContrasenaRequest.class))).thenReturn(mockCall);

        viewModel.validarYActualizarContrasena("ActualErronea123!", "NuevaPass123!", "NuevaPass123!");

        ArgumentCaptor<Callback<Residente>> captor = ArgumentCaptor.forClass(Callback.class);
        verify(mockCall).enqueue(captor.capture());

        Response<Residente> responseError = Response.error(400, okhttp3.ResponseBody.create(null, "Bad Request"));
        captor.getValue().onResponse(mockCall, responseError);

        assertEquals("Error al actualizar la contraseña.", viewModel.getToastMessageLiveData().getValue());
        assertEquals(Boolean.FALSE, viewModel.getIsLoadingLiveData().getValue());
    }

    @Test
    public void cerrarSesionSegura_limpiaTokenYEmiteCierre() {
        viewModel.cerrarSesionSegura();
        assertEquals("Cerrando Sesión...", viewModel.getToastMessageLiveData().getValue());
        assertEquals(Boolean.TRUE, viewModel.getSessionClosedLiveData().getValue());
    }
}