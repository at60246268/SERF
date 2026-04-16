package com.financorp.serf.service;

import com.financorp.serf.model.adapter.GestorPasarelasPago;
import com.financorp.serf.model.adapter.PasarelaPago;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * Servicio para gestión de pagos
 * Utiliza el patrón Adapter para integrar múltiples pasarelas
 */
@Service
public class PagoService {
    
    private final GestorPasarelasPago gestorPasarelas;
    
    public PagoService() {
        this.gestorPasarelas = new GestorPasarelasPago();
    }
    
    /**
     * Procesa un pago utilizando una pasarela específica
     */
    public boolean procesarPago(String tipoPasarela, BigDecimal monto, String detalle) {
        return gestorPasarelas.procesarPago(tipoPasarela, monto.doubleValue(), detalle);
    }
    
    /**
     * Obtiene todas las pasarelas disponibles
     */
    public List<PasarelaPago> obtenerPasarelasDisponibles() {
        return gestorPasarelas.getPasarelasHabilitadas();
    }
    
    /**
     * Obtiene todas las pasarelas (habilitadas y deshabilitadas)
     */
    public List<PasarelaPago> obtenerTodasPasarelas() {
        return gestorPasarelas.getPasarelas();
    }
    
    /**
     * Habilita una pasarela de pago
     */
    public void habilitarPasarela(String tipoPasarela) {
        gestorPasarelas.configurarPasarela(tipoPasarela, true);
    }
    
    /**
     * Deshabilita una pasarela de pago
     */
    public void deshabilitarPasarela(String tipoPasarela) {
        gestorPasarelas.configurarPasarela(tipoPasarela, false);
    }
    
    /**
     * Obtiene el gestor de pasarelas
     */
    public GestorPasarelasPago getGestorPasarelas() {
        return gestorPasarelas;
    }
}
