package com.ispc.consorciomobile;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.ispc.consorciomobile.model.Reclamo;
import com.ispc.consorciomobile.network.ApiService;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ReclamoRepositoryTest {

    private MockWebServer mockWebServer;
    private ApiService apiService;

    @Before
    public void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        // URL base inyectable hacia el servidor mock local
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(mockWebServer.url("/"))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);
    }

    @After
    public void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    public void obtenerReclamos_respuesta200_convierteJsonCorrectamente() throws Exception {
        // Arrange
        String jsonResponse = "[{\"id\": 1, \"titulo\": \"Fuga de agua\", \"estado\": \"pendiente\"}]";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(jsonResponse));

        // Act
        Call<List<Reclamo>> call = apiService.obtenerReclamos();
        Response<List<Reclamo>> response = call.execute();

        // Assert
        assertTrue(response.isSuccessful());
        assertNotNull(response.body());
        assertEquals(1, response.body().size());
        assertEquals("Fuga de agua", response.body().get(0).getTitulo());
    }

    @Test
    public void obtenerReclamos_respuesta500_manejaErrorSinRomperse() throws Exception {
        // Arrange
        mockWebServer.enqueue(new MockResponse().setResponseCode(500));

        // Act
        Call<List<Reclamo>> call = apiService.obtenerReclamos();
        Response<List<Reclamo>> response = call.execute();

        // Assert
        assertFalse(response.isSuccessful());
        assertEquals(500, response.code());
    }
}