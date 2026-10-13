package com.ispc.consorciomobile;

import com.ispc.consorciomobile.model.Reclamo;

import java.util.List;

/**
 * Calcula los reclamos activos de la lista recibida.
 */
public final class ContadorReclamos {

    private ContadorReclamos() {
        // Esta clase se utiliza mediante su método estático.
    }

    public static int contarActivos(List<Reclamo> reclamos) {
        int cantidadActivos = 0;

        for (Reclamo reclamo : reclamos) {
            String estado = reclamo.getEstado();

            if ("pendiente".equalsIgnoreCase(estado)
                    || "en_proceso".equalsIgnoreCase(estado)) {
                cantidadActivos++;
            }
        }

        return cantidadActivos;
    }
}