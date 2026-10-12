package com.ispc.consorciomobile;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.gson.Gson;

import org.junit.Test;

// Tests del parseo Gson del modelo Comunicado: verifican que los campos
// de la API (fecha_publicacion, es_urgente, usuario) mapeen a los getters.
// Trazable a TC-COM-002 (el flag es_urgente es lo que dispara el resaltado naranja).
public class ComunicadoModeloTest {

    private final Gson gson = new Gson();

    @Test
    public void gsonMapeaCamposCompletos() {
        String json = "{\"id\":1,\"titulo\":\"Corte de Gas\",\"contenido\":\"Corte total\","
                + "\"fecha_publicacion\":\"2026-09-13T20:00:00Z\",\"es_urgente\":true,\"usuario\":7}";

        Comunicado comunicado = gson.fromJson(json, Comunicado.class);

        assertEquals(1, comunicado.getId());
        assertEquals("Corte de Gas", comunicado.getTitulo());
        assertEquals("Corte total", comunicado.getContenido());
        assertEquals("2026-09-13T20:00:00Z", comunicado.getFechaPublicacion());
        assertTrue(comunicado.getEsUrgente());
        assertEquals(Integer.valueOf(7), comunicado.getUsuario());
    }

    @Test
    public void gsonUsuarioNulo() {
        String json = "{\"id\":2,\"titulo\":\"Reunion\",\"es_urgente\":false,\"usuario\":null}";

        Comunicado comunicado = gson.fromJson(json, Comunicado.class);

        assertNull(comunicado.getUsuario());
    }

    @Test
    public void gsonEurgenteAusenteQuedaEnFalse() {
        // Si el JSON no trae es_urgente, el boolean por defecto es false (no se resalta).
        String json = "{\"id\":3,\"titulo\":\"Aviso\"}";

        Comunicado comunicado = gson.fromJson(json, Comunicado.class);

        assertFalse(comunicado.getEsUrgente());
    }

    @Test
    public void gsonFechaPublicacionMapeaElNombreApi() {
        // La API usa snake_case; el SerializedName debe mapearlo al campo camelCase.
        String json = "{\"fecha_publicacion\":\"2026-10-01T12:00:00Z\"}";

        Comunicado comunicado = gson.fromJson(json, Comunicado.class);

        assertEquals("2026-10-01T12:00:00Z", comunicado.getFechaPublicacion());
    }
}
