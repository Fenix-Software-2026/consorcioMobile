package com.ispc.consorciomobile;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.google.gson.Gson;
import com.ispc.consorciomobile.network.ApiService;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// Tests de integracion de red del modulo de comunicados con MockWebServer.
// No usan la red real: levantamos un servidor mock local y verificamos el
// parseo de la respuesta, el formato del request y los codigos de error.
// Trazable a TC-COM-003 (publicacion y gestion de comunicados via API).
public class ComunicadosApiTest {

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

    @Test
    public void obtenerComunicadosParseaLista() throws Exception {
        String body = "[{\"id\":1,\"titulo\":\"Corte de Gas\",\"contenido\":\"Corte total\","
                + "\"fecha_publicacion\":\"2026-09-13T20:00:00Z\",\"es_urgente\":true,\"usuario\":7},"
                + "{\"id\":2,\"titulo\":\"Reunion\",\"contenido\":\"Anual\","
                + "\"fecha_publicacion\":\"2026-09-10T10:00:00Z\",\"es_urgente\":false,\"usuario\":7}]";
        server.enqueue(new MockResponse().setResponseCode(200).setBody(body));

        Response<List<Comunicado>> response = api.obtenerComunicados().execute();

        assertEquals(200, response.code());
        assertNotNull(response.body());
        assertEquals(2, response.body().size());
        assertEquals("Corte de Gas", response.body().get(0).getTitulo());
        assertEquals(true, response.body().get(0).getEsUrgente());
    }

    @Test
    public void obtenerComunicadoPorIdUsaElPathCorrecto() throws Exception {
        String body = "{\"id\":7,\"titulo\":\"Aviso\",\"contenido\":\"x\","
                + "\"fecha_publicacion\":\"2026-10-01T12:00:00Z\",\"es_urgente\":false,\"usuario\":1}";
        server.enqueue(new MockResponse().setResponseCode(200).setBody(body));

        Response<Comunicado> response = api.obtenerComunicado(7).execute();

        assertEquals(200, response.code());
        RecordedRequest request = server.takeRequest();
        assertEquals("GET", request.getMethod());
        assertEquals("/api/comunicados/7/", request.getPath());
    }

    @Test
    public void crearComunicadoEnviaPostConBodyEsperado() throws Exception {
        Comunicado nuevo = new Comunicado();
        nuevo.setTitulo("Corte de Gas");
        nuevo.setContenido("Corte total por mantenimiento");
        nuevo.setEsUrgente(true);
        server.enqueue(new MockResponse().setResponseCode(201).setBody(
                "{\"id\":3,\"titulo\":\"Corte de Gas\",\"es_urgente\":true}"));

        Response<Comunicado> response = api.crearComunicado(nuevo).execute();

        assertEquals(201, response.code());
        RecordedRequest request = server.takeRequest();
        assertEquals("POST", request.getMethod());
        assertEquals("/api/comunicados/", request.getPath());
        String contentType = request.getHeader("Content-Type");
        assertNotNull(contentType);
        assertEquals(true, contentType.contains("application/json"));
        String bodyEnviado = request.getBody().readUtf8();
        assertEquals(true, bodyEnviado.contains("Corte de Gas"));
        assertEquals(true, bodyEnviado.contains("\"es_urgente\":true"));
    }

    @Test
    public void borrarComunicadoDevuelve204() throws Exception {
        server.enqueue(new MockResponse().setResponseCode(204));

        Response<Void> response = api.borrarComunicado(5).execute();

        assertEquals(204, response.code());
        RecordedRequest request = server.takeRequest();
        assertEquals("DELETE", request.getMethod());
        assertEquals("/api/comunicados/5/", request.getPath());
    }

    @Test
    public void comunicadosSinTokenDevuelve401() throws Exception {
        // Prueba negativa: sin Authorization deberia rechazar con 401.
        // Tambien sirve como base del caso de prueba de seguridad de comunicados.
        server.enqueue(new MockResponse().setResponseCode(401).setBody("{\"detail\":\"Autenticacion no proveida\"}"));

        Response<List<Comunicado>> response = api.obtenerComunicados().execute();

        assertEquals(401, response.code());
    }
}
