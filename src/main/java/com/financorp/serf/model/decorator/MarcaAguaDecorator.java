package com.financorp.serf.model.decorator;

import com.financorp.serf.model.builder.Reporte;

/**
 * PATRÓN DECORATOR - Añade marca de agua a los reportes
 * 
 * Decorador concreto que agrega una marca de agua de seguridad al reporte.
 * 
 * Principio SOLID: Single Responsibility - Solo agrega marca de agua
 * 
 * @author FinanCorp S.A.
 */
public class MarcaAguaDecorator extends ReporteDecorator {
    
    private String textoMarcaAgua;
    private String posicion; // "centro", "diagonal", "superior", "inferior"
    
    /**
     * Constructor con texto por defecto
     * 
     * @param reporte Reporte a decorar
     */
    public MarcaAguaDecorator(Reporte reporte) {
        this(reporte, "CONFIDENCIAL - FinanCorp S.A.", "diagonal");
    }
    
    /**
     * Constructor con texto personalizado
     * 
     * @param reporte Reporte a decorar
     * @param textoMarcaAgua Texto de la marca de agua
     * @param posicion Posición de la marca ("centro", "diagonal", etc.)
     */
    public MarcaAguaDecorator(Reporte reporte, String textoMarcaAgua, String posicion) {
        super(reporte);
        this.textoMarcaAgua = textoMarcaAgua;
        this.posicion = posicion;
        aplicar();
    }
    
    @Override
    public String renderizar() {
        StringBuilder sb = new StringBuilder();
        
        // Contenedor con marca de agua
        sb.append("<div class='reporte-con-marca-agua' style='position: relative;'>");
        
        // Renderizar el reporte original
        sb.append(reporteBase.renderizar());
        
        // Añadir la marca de agua como overlay
        sb.append(generarMarcaAguaHTML());
        
        sb.append("</div>");
        
        return sb.toString();
    }
    
    @Override
    public void aplicar() {
        // Marcar en el reporte que tiene marca de agua
        reporteBase.setTieneMarcaAgua(true);
        reporteBase.setTextoMarcaAgua(textoMarcaAgua);
    }
    
    /**
     * Genera el HTML de la marca de agua
     * 
     * @return String HTML de la marca de agua
     */
    private String generarMarcaAguaHTML() {
        StringBuilder sb = new StringBuilder();
        
        // Estilos CSS inline según la posición
        String estilos = obtenerEstilosPorPosicion();
        
        sb.append("<div class='marca-agua' style='")
          .append(estilos)
          .append("'>")
          .append(textoMarcaAgua)
          .append("</div>");
        
        return sb.toString();
    }
    
    /**
     * Obtiene los estilos CSS según la posición configurada
     * 
     * @return String con estilos CSS
     */
    private String obtenerEstilosPorPosicion() {
        String estilosBase = "position: absolute; " +
                           "color: rgba(200, 200, 200, 0.3); " +
                           "font-size: 48px; " +
                           "font-weight: bold; " +
                           "pointer-events: none; " +
                           "z-index: 1000; ";
        
        switch (posicion.toLowerCase()) {
            case "centro":
                return estilosBase +
                       "top: 50%; left: 50%; " +
                       "transform: translate(-50%, -50%); ";
                       
            case "diagonal":
                return estilosBase +
                       "top: 50%; left: 50%; " +
                       "transform: translate(-50%, -50%) rotate(-45deg); ";
                       
            case "superior":
                return estilosBase +
                       "top: 20px; left: 50%; " +
                       "transform: translateX(-50%); ";
                       
            case "inferior":
                return estilosBase +
                       "bottom: 20px; left: 50%; " +
                       "transform: translateX(-50%); ";
                       
            default:
                return estilosBase +
                       "top: 50%; left: 50%; " +
                       "transform: translate(-50%, -50%) rotate(-45deg); ";
        }
    }
    
    // Getters y Setters
    
    public String getTextoMarcaAgua() {
        return textoMarcaAgua;
    }
    
    public void setTextoMarcaAgua(String textoMarcaAgua) {
        this.textoMarcaAgua = textoMarcaAgua;
        aplicar();
    }
    
    public String getPosicion() {
        return posicion;
    }
    
    public void setPosicion(String posicion) {
        this.posicion = posicion;
    }
}
