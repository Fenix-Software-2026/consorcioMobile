package com.ispc.consorciomobile.viewmodel;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.ispc.consorciomobile.network.AuthRepository;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

public class LoginViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private AuthRepository mockAuthRepository;

    private LoginViewModel viewModel;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        viewModel = new LoginViewModel(mockAuthRepository);
    }

    // -------------------------------------------------------------------------
    // TC-L-01: Iniciar sesión exitoso con credenciales válidas
    // -------------------------------------------------------------------------
    @Test
    public void TC_L_01_iniciarSesion_credencialesValidas_emiteLoginExitoso() {
        // Arrange
        String usuario = "residentePrueba1";
        String password = "Contraseña";
        ArgumentCaptor<AuthRepository.LoginCallback> captor =
                ArgumentCaptor.forClass(AuthRepository.LoginCallback.class);

        // Act
        viewModel.iniciarSesion(usuario, password);

        // Verify interacción con el repositorio mockeado
        verify(mockAuthRepository).login(eq(usuario), eq(password), captor.capture());

        // Simular respuesta exitosa
        captor.getValue().onSuccess("residente", usuario);

        // Assert: verificar LiveData
        assertEquals(Boolean.TRUE, viewModel.getLoginExitosoLiveData().getValue());
        assertEquals("residente", viewModel.getRolLiveData().getValue());
        assertEquals(Boolean.FALSE, viewModel.getIsLoadingLiveData().getValue());
    }

    // -------------------------------------------------------------------------
    // TC-L-02: Iniciar sesión fallido con credenciales inválidas
    // -------------------------------------------------------------------------
    @Test
    public void TC_L_02_iniciarSesion_credencialesInvalidas_emiteMensajeError() {
        // Arrange
        String usuario = "juan";
        String password = "abdcefghijk";
        ArgumentCaptor<AuthRepository.LoginCallback> captor =
                ArgumentCaptor.forClass(AuthRepository.LoginCallback.class);

        // Act
        viewModel.iniciarSesion(usuario, password);

        // Verify interacción
        verify(mockAuthRepository).login(eq(usuario), eq(password), captor.capture());

        // Simular respuesta de error de credenciales
        captor.getValue().onErrorCredenciales();

        // Assert: verificar que emite el mensaje exacto
        assertEquals("Usuario o contraseña incorrectos", viewModel.getErrorLiveData().getValue());
        assertEquals(Boolean.FALSE, viewModel.getIsLoadingLiveData().getValue());
    }
}