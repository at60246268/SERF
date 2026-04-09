package com.financorp.serf.model.config;

import com.financorp.serf.model.enums.Moneda;
import java.util.HashMap;
import java.util.Map;

/**
 * PATRÓN SINGLETON - Configuración Global del Sistema
 * 
 * Garantiza una única instancia de configuración en toda la aplicación.
 * Thread-safe usando Double-Check Locking.
 * 
 * Principios SOLID aplicados:
 * - Single Responsibility: Solo gestiona configuración global
 * - Open/Closed: Extensible para nuevas configuraciones
 * 
 * @author FinanCorp S.A.
 * @version 1.0.0
 */
public class ConfiguracionGlobal {
    
    // Instancia única (volatile para thread-safety)
    private static volatile ConfiguracionGlobal instance;
    
    // Configuración de moneda corporativa
    private Moneda monedaCorporativa;
    
    // Tasas de cambio respecto a la moneda corporativa (EUR)
    private Map<Moneda, Double> tasasCambio;
    
    // Formato de fecha corporativo
    private String formatoFecha;
    
    // Configuración de reportes
    private String encabezadoReportes;
    private String pieReportes;
    
    /**
     * Constructor privado - Evita instanciación directa
     * Principio SOLID: Dependency Inversion - Control total de la instancia
     */
    private ConfiguracionGlobal() {
        inicializarConfiguracion();
    }
    
    /**
     * Obtiene la instancia única (Thread-safe con Double-Check Locking)
     * 
     * @return Instancia única de ConfiguracionGlobal
     */
    public static ConfiguracionGlobal getInstance() {
        if (instance == null) {
            synchronized (ConfiguracionGlobal.class) {
                if (instance == null) {
                    instance = new ConfiguracionGlobal();
                }
            }
        }
        return instance;
    }
    
    /**
     * Inicializa la configuración con valores por defecto
     * Principio SOLID: Single Responsibility
     */
    private void inicializarConfiguracion() {
        // Moneda corporativa: EUR
        this.monedaCorporativa = Moneda.EUR;
        
        // Inicializar tasas de cambio (valores aproximados)
        this.tasasCambio = new HashMap<>();
        this.tasasCambio.put(Moneda.EUR, 1.0);        // Base
        this.tasasCambio.put(Moneda.USD, 0.92);       // 1 USD = 0.92 EUR
        this.tasasCambio.put(Moneda.CNY, 0.13);       // 1 CNY = 0.13 EUR
        this.tasasCambio.put(Moneda.PEN, 0.25);       // 1 PEN = 0.25 EUR
        this.tasasCambio.put(Moneda.CLP, 0.0011);     // 1 CLP = 0.0011 EUR
        this.tasasCambio.put(Moneda.MXN, 0.050);      // 1 MXN = 0.050 EUR
        this.tasasCambio.put(Moneda.ARS, 0.0010);     // 1 ARS = 0.0010 EUR
        this.tasasCambio.put(Moneda.GBP, 1.17);       // 1 GBP = 1.17 EUR
        
        // Formato de fecha
        this.formatoFecha = "dd/MM/yyyy";
        
        // Encabezados y pies de reportes
        this.encabezadoReportes = "FINANCORP S.A. - Corporación Multinacional";
        this.pieReportes = "Documento Confidencial - Uso Interno";
    }
    
    /**
     * Convierte un monto de una moneda a otra
     * Método central usado en todo el sistema
     * 
     * @param monto Monto a convertir
     * @param monedaOrigen Moneda origen
     * @param monedaDestino Moneda destino
     * @return Monto convertido
     */
    public double convertirMoneda(double monto, Moneda monedaOrigen, Moneda monedaDestino) {
        if (monto < 0) {
            throw new IllegalArgumentException("El monto no puede ser negativo");
        }
        
        if (monedaOrigen == monedaDestino) {
            return monto;
        }
        
        // Convertir primero a EUR (moneda base)
        double montoEnEUR = monto * tasasCambio.get(monedaOrigen);
        
        // Luego convertir de EUR a la moneda destino
        double tasaDestino = tasasCambio.get(monedaDestino);
        return montoEnEUR / tasaDestino;
    }
    
    /**
     * Convierte un monto a la moneda corporativa (EUR)
     * Método más usado en el sistema
     * 
     * @param monto Monto a convertir
     * @param monedaOrigen Moneda origen
     * @return Monto en EUR
     */
    public double convertirAMonedaCorporativa(double monto, Moneda monedaOrigen) {
        return convertirMoneda(monto, monedaOrigen, monedaCorporativa);
    }
    
    /**
     * Obtiene la tasa de cambio de una moneda respecto al EUR
     * 
     * @param moneda Moneda a consultar
     * @return Tasa de cambio
     */
    public double obtenerTasaCambio(Moneda moneda) {
        return tasasCambio.getOrDefault(moneda, 1.0);
    }
    
    /**
     * Actualiza la tasa de cambio de una moneda
     * Principio SOLID: Open/Closed - Permite extensión sin modificación
     * 
     * @param moneda Moneda a actualizar
     * @param tasa Nueva tasa de cambio
     */
    public synchronized void actualizarTasaCambio(Moneda moneda, double tasa) {
        if (tasa <= 0) {
            throw new IllegalArgumentException("La tasa de cambio debe ser positiva");
        }
        this.tasasCambio.put(moneda, tasa);
    }
    
    // Getters y Setters
    
    public Moneda getMonedaCorporativa() {
        return monedaCorporativa;
    }
    
    public synchronized void setMonedaCorporativa(Moneda monedaCorporativa) {
        this.monedaCorporativa = monedaCorporativa;
    }
    
    public String getFormatoFecha() {
        return formatoFecha;
    }
    
    public synchronized void setFormatoFecha(String formatoFecha) {
        this.formatoFecha = formatoFecha;
    }
    
    public String getEncabezadoReportes() {
        return encabezadoReportes;
    }
    
    public synchronized void setEncabezadoReportes(String encabezadoReportes) {
        this.encabezadoReportes = encabezadoReportes;
    }
    
    public String getPieReportes() {
        return pieReportes;
    }
    
    public synchronized void setPieReportes(String pieReportes) {
        this.pieReportes = pieReportes;
    }
    
    public Map<Moneda, Double> getTasasCambio() {
        return new HashMap<>(tasasCambio); // Retorna copia para inmutabilidad
    }
    
    /**
     * Evita la clonación de la instancia Singleton
     */
    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException("No se puede clonar un Singleton");
    }
}
