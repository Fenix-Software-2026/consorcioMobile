package com.ispc.consorciomobile;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.google.gson.Gson;
import com.ispc.consorciomobile.model.CambioContrasenaRequest;
import com.ispc.consorciomobile.model.Residente;
import com.ispc.consorciomobile.network.ApiService;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * AUT-NET del módulo Perfil de Usuario.
 *
 * Verifica, con un servidor HTTP simulado (MockWebServer), cómo se resuelve la
 * llamada al endpoint PATCH api/usuario/mi_perfil/ cuando el backend responde
 * con un error. Cubre el caso TC-PER-08 de la planilla de casos de prueba.
 */
public class ApiServicePerfilTest {

    private MockWebServer server;
    private ApiService api;

    @Before
    public void setUp() throws Exception {
        server = new MockWebServer();
        server.start();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(server.url("/"))
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .client(new OkHttpClient.Builder()
                        .connectTimeout(2, TimeUnit.SECONDS)
                        .readTimeout(2, TimeUnit.SECONDS)
                        .build())
                .build();

        api = retrofit.create(ApiService.class);
    }

    @After
    public void tearDown() throws Exception {
        server.shutdown();
    }

    // -------------------------------------------------------------------------
    // TC-PER-08: respuesta HTTP 500 al cambiar la contraseña
    // -------------------------------------------------------------------------
    @Test
    public void actualizarMiPerfil_respuesta500_noEsExitosaYNoLanzaExcepcion() throws Exception {
        // Arrange: el backend falla con un 500 y un detalle de error
        String jsonError = "{\"detail\": \"Error interno del servidor\"}";
        server.enqueue(new MockResponse()
                .setResponseCode(500)
                .setBody(jsonError)
                .addHeader("Content-Type", "application/json"));

        // Act: se ejecuta el cambio de contraseña con datos válidos
        Response<Residente> respuesta = api.actualizarMiPerfil(
                new CambioContrasenaRequest("Actual123!", "NuevaPass123!")
        ).execute();

        // Assert: la llamada se resuelve como no exitosa, sin excepción
        assertFalse("Un HTTP 500 no debe marcarse como exitoso", respuesta.isSuccessful());
        assertEquals("El código de respuesta debe ser 500", 500, respuesta.code());
        assertNull("Un error no debe devolver cuerpo de Residente", respuesta.body());
        assertNotNull("El cuerpo de error debe quedar disponible", respuesta.errorBody());

        // Assert: se llamó al endpoint y al método correctos
        RecordedRequest peticion = server.takeRequest();
        assertEquals("PATCH", peticion.getMethod());
        assertEquals("/api/usuario/mi_perfil/", peticion.getPath());
    }
}
