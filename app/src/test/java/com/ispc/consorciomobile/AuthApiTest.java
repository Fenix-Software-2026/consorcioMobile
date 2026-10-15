package com.ispc.consorciomobile;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.google.gson.Gson;
import com.ispc.consorciomobile.network.ApiService;
import com.ispc.consorciomobile.network.LoginRequest;
import com.ispc.consorciomobile.network.LoginResponse;

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

public class AuthApiTest {

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
    // TC-L-06: Respuesta HTTP 200 con parseo del token de acceso // <-Nota de nahuu: modifique el id del test para dejar la matriz de excel mas ordenada
    // -------------------------------------------------------------------------
    @Test
    public void TC_L_01_iniciarSesion_red200Ok_parseaAccessToken() throws Exception {
        // Arrange: Simulamos que Django devuelve 200 OK con el access token
        String jsonRespuesta = "{\"access\":\"token_de_acceso_jwt_12345\"}";
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(jsonRespuesta)
                .addHeader("Content-Type", "application/json"));

        // Act: Ejecutamos la llamada mediante Retrofit
        Response<LoginResponse> respuesta = api.iniciarSesion(
                new LoginRequest("residentePrueba1", "Contraseña")
        ).execute();

        // Assert: Validamos que fue exitoso y parseó únicamente el access token
        assertTrue("La respuesta HTTP debe ser 2xx exitosa", respuesta.isSuccessful());
        assertEquals(200, respuesta.code());
        assertNotNull("El cuerpo de la respuesta no debe ser nulo", respuesta.body());
        assertEquals("token_de_acceso_jwt_12345", respuesta.body().getAccess());
        // Nota de Nahuu (x las dudas); hice los test tomando en cuenta el token de access, se que el loginresponse devuelve el refresh tambien pero por cuestiones de tiempo del sprint no llegué a implementar la petición para el refresh y demás. Funcionalidades a ampliar para el futuro jeje

        // Verificamos que le pegó al endpoint correcto de login
        RecordedRequest peticion = server.takeRequest();
        assertEquals("POST", peticion.getMethod());
        assertEquals("/api/login/", peticion.getPath());
    }

    // -------------------------------------------------------------------------
    // TC-L-02: Respuesta HTTP 401 Credenciales Inválidas
    // -------------------------------------------------------------------------
    @Test
    public void TC_L_02_iniciarSesion_red401Unauthorized_rechazaAcceso() throws Exception {
        // Arrange: Simulamos respuesta 401 de credenciales erróneas
        String jsonError = "{\"detail\":\"No active account found with the given credentials\"}";
        server.enqueue(new MockResponse()
                .setResponseCode(401)
                .setBody(jsonError)
                .addHeader("Content-Type", "application/json"));

        // Act
        Response<LoginResponse> respuesta = api.iniciarSesion(
                new LoginRequest("juan", "abdcefghijk")
        ).execute();

        // Assert: No debe ser exitosa y debe devolver código 401
        assertFalse("El status 401 no debe marcarse como exitoso", respuesta.isSuccessful());
        assertEquals(401, respuesta.code());
    }
}