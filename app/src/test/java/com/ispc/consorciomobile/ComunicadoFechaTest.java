package com.ispc.consorciomobile;

import static org.junit.Assert.assertEquals;

import com.ispc.consorciomobile.adapter.ComunicadoAdapter;

import org.junit.Test;

// Tests de formatearFecha, que es la logica que usa el adapter
// para mostrar la fecha ISO de la API como dd/MM/aaaa.
// Trazable a TC-COM-001 (la lista debe renderizar la fecha de publicacion).
public class ComunicadoFechaTest {

    @Test
    public void formatearFechaIsoCompleta() {
        assertEquals("13/09/2026", ComunicadoAdapter.formatearFecha("2026-09-13T20:00:00Z"));
    }

    @Test
    public void formatearFechaSinHora() {
        assertEquals("01/12/2026", ComunicadoAdapter.formatearFecha("2026-12-01"));
    }

    @Test
    public void formatearFechaNull() {
        assertEquals("--/--/----", ComunicadoAdapter.formatearFecha(null));
    }

    @Test
    public void formatearFechaVacia() {
        assertEquals("--/--/----", ComunicadoAdapter.formatearFecha(""));
    }

    @Test
    public void formatearFechaFormatoInvalido() {
        // Sin guiones no se puede partir; la devolvemos como esta.
        assertEquals("13/09/2026", ComunicadoAdapter.formatearFecha("13/09/2026"));
    }

    @Test
    public void formatearFechaAnioMes() {
        // Fecha incompleta (2 partes): tambien se devuelve tal cual.
        assertEquals("2026-09", ComunicadoAdapter.formatearFecha("2026-09"));
    }
}
