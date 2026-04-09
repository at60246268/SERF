package com.financorp.serf.model.adapter;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * PATRÓN ADAPTER - Gestor de pasarelas de pago (RF2)
 *
 * Registra todas las pasarelas disponibles y permite al administrador
 * habilitar o deshabilitar cada una desde un panel de configuración.
 * El cliente pide la pasarela por nombre y siempre recibe PasarelaPago.
 */
@Component
public class GestorPasarelasPago {

    private final List<PasarelaPago> pasarelas = new ArrayList<>();

    public GestorPasarelasPago() {
        // Registro de pasarelas disponibles (RF1)
        pasarelas.add(new PayPalAdapter(true));
        pasarelas.add(new YapeAdapter("999000111", true));
        pasarelas.add(new PlinAdapter(true));
    }

    /** Devuelve todas las pasarelas registradas. */
    public List<PasarelaPago> getPasarelas() {
        return pasarelas;
    }

    /** Devuelve solo las pasarelas habilitadas. */
    public List<PasarelaPago> getPasarelasHabilitadas() {
        return pasarelas.stream().filter(PasarelaPago::estaHabilitada).toList();
    }

    /** RF2: Habilita o deshabilita una pasarela por nombre. */
    public void configurarPasarela(String nombre, boolean habilitar) {
        pasarelas.stream()
                .filter(p -> p.getNombre().equalsIgnoreCase(nombre))
                .findFirst()
                .ifPresent(p -> {
                    if (p instanceof PayPalAdapter pa) pa.setHabilitada(habilitar);
                    else if (p instanceof YapeAdapter ya) ya.setHabilitada(habilitar);
                    else if (p instanceof PlinAdapter pla) pla.setHabilitada(habilitar);
                });
    }

    /** Procesa un pago usando la pasarela indicada. */
    public boolean procesarPago(String nombrePasarela, double monto, String detalle) {
        Optional<PasarelaPago> pasarela = pasarelas.stream()
                .filter(p -> p.getNombre().equalsIgnoreCase(nombrePasarela))
                .findFirst();
        if (pasarela.isEmpty()) {
            System.out.println("[GestorPasarelas] Pasarela no encontrada: " + nombrePasarela);
            return false;
        }
        return pasarela.get().procesarPago(monto, detalle);
    }
}
