package com.ispc.consorciomobile.viewmodel;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.ispc.consorciomobile.model.CambioContrasenaRequest;
import com.ispc.consorciomobile.model.Residente;
import com.ispc.consorciomobile.network.ApiService;

import okhttp3.ResponseBody;

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
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests unitarios del módulo Perfil de Usuario.
 *
 * Todos los casos siguen el patrón AAA (Arrange / Act / Assert) con los bloques
 * comentados, y se cubren valores límite de la regla de contraseña: 7 y 8
 * caracteres, que es donde cambia la decisión del validador.
 */
public class MiPerfilViewModelTest {

    private static final String MENSAJE_CONTRASENA_DEBIL =
            "La contraseña debe tener al menos 8 caracteres, una mayúscula, "
                    + "una minúscula, un número y un carácter especial";

    // Mensaje genérico cuando el cuerpo del error no se puede interpretar.
    private static final String MENSAJE_ERROR_GENERICO = "Error al actualizar la contraseña.";

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

    // -------------------------------------------------------------------------
    // TC-PER-01: campos obligatorios vacíos
    // -------------------------------------------------------------------------
    @Test
    public void validarYActualizarContrasena_camposVacios_emiteMensajeError() {
        // Arrange
        String passActual = "";
        String passNueva = "Pass1234!";
        String passRepetir = "Pass1234!";

        // Act
        viewModel.validarYActualizarContrasena(passActual, passNueva, passRepetir);

        // Assert
        assertEquals("Por favor, completa todos los campos",
                viewModel.getToastMessageLiveData().getValue());
    }

    // -------------------------------------------------------------------------
    // TC-PER-02: las contraseñas nuevas no coinciden
    // -------------------------------------------------------------------------
    @Test
    public void validarYActualizarContrasena_contrasenasNoCoinciden_emiteMensajeError() {
        // Arrange
        String passActual = "PassActual123!";
        String passNueva = "NuevaPass123!";
        String passRepetir = "OtraPass123!";

        // Act
        viewModel.validarYActualizarContrasena(passActual, passNueva, passRepetir);

        // Assert
        assertEquals("Las nuevas contraseñas no coinciden",
                viewModel.getToastMessageLiveData().getValue());
    }

    // -------------------------------------------------------------------------
    // TC-PER-03: contraseña sin mayúscula ni carácter especial
    // -------------------------------------------------------------------------
    @Test
    public void validarYActualizarContrasena_sinMayusculaNiEspecial_emiteMensajeError() {
        // Arrange: 8 caracteres, pero solo números
        String passActual = "PassActual123!";
        String passNueva = "12345678";
        String passRepetir = "12345678";

        // Act
        viewModel.validarYActualizarContrasena(passActual, passNueva, passRepetir);

        // Assert
        assertEquals(MENSAJE_CONTRASENA_DEBIL,
                viewModel.getToastMessageLiveData().getValue());
    }

    // -------------------------------------------------------------------------
    // TC-PER-04: valor límite inferior — 7 caracteres
    // Cumple mayúscula, minúscula, número y símbolo, así que lo único que
    // puede rechazarla es la longitud.
    // -------------------------------------------------------------------------
    @Test
    public void validarYActualizarContrasena_sieteCaracteres_emiteMensajeError() {
        // Arrange
        String passActual = "PassActual123!";
        String passNueva = "Pa1!abc";
        String passRepetir = "Pa1!abc";

        // Act
        viewModel.validarYActualizarContrasena(passActual, passNueva, passRepetir);

        // Assert
        assertEquals(MENSAJE_CONTRASENA_DEBIL,
                viewModel.getToastMessageLiveData().getValue());
    }

    // -------------------------------------------------------------------------
    // TC-PER-05: valor límite superior — 8 caracteres (mínimo aceptado)
    // -------------------------------------------------------------------------
    @Test
    public void validarYActualizarContrasena_ochoCaracteres_esAceptadaYLLamaALaApi() {
        // Arrange: misma composición que el caso anterior, con un carácter más
        when(mockApiService.actualizarMiPerfil(any(CambioContrasenaRequest.class)))
                .thenReturn(mockCall);

        // Act
        viewModel.validarYActualizarContrasena("PassActual123!", "Pa1!abcd", "Pa1!abcd");

        // Assert: pasó la validación, así que llamó a la API y no hay mensaje de error
        verify(mockCall).enqueue(any());
        assertEquals(Boolean.TRUE, viewModel.getIsLoadingLiveData().getValue());
        assertNull(viewModel.getToastMessageLiveData().getValue());
    }

    // -------------------------------------------------------------------------
    // TC-PER-06: cambio de contraseña exitoso
    // -------------------------------------------------------------------------
    @Test
    public void validarYActualizarContrasena_exitosa_llamaApiServiceYEmiteExito() {
        // Arrange
        when(mockApiService.actualizarMiPerfil(any(CambioContrasenaRequest.class)))
                .thenReturn(mockCall);

        // Act
        viewModel.validarYActualizarContrasena("Actual123!", "NuevaPass123!", "NuevaPass123!");

        ArgumentCaptor<Callback<Residente>> captor = ArgumentCaptor.forClass(Callback.class);
        verify(mockCall).enqueue(captor.capture());

        Response<Residente> responseExitosa =
                Response.success(new Residente("testuser", "test@email.com", "Juan", "Perez", 1));
        captor.getValue().onResponse(mockCall, responseExitosa);

        // Assert
        assertEquals("Contraseña actualizada con éxito",
                viewModel.getToastMessageLiveData().getValue());
        assertEquals(Boolean.TRUE, viewModel.getPasswordUpdatedSuccessLiveData().getValue());
        assertEquals(Boolean.FALSE, viewModel.getIsLoadingLiveData().getValue());
    }

    // -------------------------------------------------------------------------
    // TC-PER-07: la API responde un error que no es JSON
    //
    // Nota: el mapeo del cuerpo real de DRF ({"current_password": [...]}) no se
    // puede testear acá porque en los tests JVM `org.json` es el stub de
    // android.jar. Haría falta agregar la dependencia `org.json:json` o usar
    // Robolectric.
    // -------------------------------------------------------------------------
    @Test
    public void validarYActualizarContrasena_respuestaNoParseable_muestraMensajeGenerico() {
        // Arrange
        when(mockApiService.actualizarMiPerfil(any(CambioContrasenaRequest.class)))
                .thenReturn(mockCall);

        // Act
        viewModel.validarYActualizarContrasena("ActualErronea123!", "NuevaPass123!", "NuevaPass123!");

        ArgumentCaptor<Callback<Residente>> captor = ArgumentCaptor.forClass(Callback.class);
        verify(mockCall).enqueue(captor.capture());

        Response<Residente> responseError =
                Response.error(400, ResponseBody.create(null, "Bad Request"));
        captor.getValue().onResponse(mockCall, responseError);

        // Assert
        assertEquals(MENSAJE_ERROR_GENERICO,
                viewModel.getToastMessageLiveData().getValue());
        assertEquals(Boolean.FALSE, viewModel.getIsLoadingLiveData().getValue());
    }

    // -------------------------------------------------------------------------
    // TC-PER-08: cierre de sesión
    // -------------------------------------------------------------------------
    @Test
    public void cerrarSesionSegura_limpiaTokenYEmiteCierre() {
        // Arrange: el ViewModel ya está creado en setUp()

        // Act
        viewModel.cerrarSesionSegura();

        // Assert
        assertEquals("Cerrando Sesión...",
                viewModel.getToastMessageLiveData().getValue());
        assertEquals(Boolean.TRUE, viewModel.getSessionClosedLiveData().getValue());
    }
}
