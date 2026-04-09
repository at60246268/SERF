package com.financorp.serf.model.builder;

import com.financorp.serf.model.composite.ComponenteReporte;
import com.financorp.serf.model.reportes.PlantillaReporte;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase Reporte - Producto del patrón Builder
 * Representa un reporte financiero completo
 * 
 * Principio SOLID: Single Responsibility - Solo contiene datos del reporte
 */
public class Reporte {
    
    private String titulo;
    private String periodo;
    private LocalDateTime fechaGeneracion;
    private String encabezado;
    private List<ComponenteReporte> secciones;
    private String conclusiones;
    private String piePagina;
    
    // Propiedades para decoradores
    private boolean tieneMarcaAgua;
    private boolean tieneFirma;
    private String textoMarcaAgua;
    private String textoFirma;
    private String hashFirma;
    
    /**
     * Constructor del paquete - Solo el Builder puede instanciar
     */
    Reporte() {
        this.secciones = new ArrayList<>();
        this.fechaGeneracion = LocalDateTime.now();
        this.tieneMarcaAgua = false;
        this.tieneFirma = false;
    }
    
    /**
     * Renderiza el reporte completo como HTML
     */
    public String renderizar() {
        StringBuilder sb = new StringBuilder();
        
        sb.append("<div class='reporte-container'>");
        
        // Encabezado
        if (encabezado != null) {
            sb.append("<div class='reporte-encabezado'>")
              .append(encabezado)
              .append("</div>");
        }
        
        // Título
        sb.append("<h1 class='reporte-titulo'>").append(titulo).append("</h1>");
        
        // Periodo
        if (periodo != null) {
            sb.append("<p c lass='reporte-periodo'>")
              .append("Periodo: ").append(periodo)
              .append("</p>");
        }
        
        // Fecha de generación
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        sb.append("<p class='reporte-fecha'>")
          .append("Generado: ").append(fechaGeneracion.format(formatter))
          .append("</p>");
        
        sb.append("<hr>");
        
        // Secciones (usando COMPOSITE)
        for (ComponenteReporte seccion : secciones) {
            sb.append(seccion.renderizar());
        }
        
        // Conclusiones
        if (conclusiones != null && !conclusiones.isEmpty()) {
            sb.append("<div class='reporte-conclusiones'>")
              .append("<h2>Conclusiones</h2>")
              .append("<p>").append(conclusiones).append("</p>")
              .append("</div>");
        }
        
        // Pie de página
        if (piePagina != null) {
            sb.append("<div class='reporte-pie'>")
              .append(piePagina)
              .append("</div>");
        }
        
        sb.append("</div>");
        
        return sb.toString();
    }
    
    // Getters y Setters
    
    public String getTitulo() {
        return titulo;
    }
    
    void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    
    public String getPeriodo() {
        return periodo;
    }
    
    void setPeriodo(String periodo) {
        this.periodo = periodo;
    }
    
    public LocalDateTime getFechaGeneracion() {
        return fechaGeneracion;
    }
    
    void setFechaGeneracion(LocalDateTime fechaGeneracion) {
        this.fechaGeneracion = fechaGeneracion;
    }
    
    public String getEncabezado() {
        return encabezado;
    }
    
    void setEncabezado(String encabezado) {
        this.encabezado = encabezado;
    }
    
    public List<ComponenteReporte> getSecciones() {
        return secciones;
    }
    
    void agregarSeccion(ComponenteReporte seccion) {
        this.secciones.add(seccion);
    }
    
    public String getConclusiones() {
        return conclusiones;
    }
    
    void setConclusiones(String conclusiones) {
        this.conclusiones = conclusiones;
    }
    
    public String getPiePagina() {
        return piePagina;
    }
    
    void setPiePagina(String piePagina) {
        this.piePagina = piePagina;
    }
    
    public boolean isTieneMarcaAgua() {
        return tieneMarcaAgua;
    }
    
    public void setTieneMarcaAgua(boolean tieneMarcaAgua) {
        this.tieneMarcaAgua = tieneMarcaAgua;
    }
    
    public boolean isTieneFirma() {
        return tieneFirma;
    }
    
    public void setTieneFirma(boolean tieneFirma) {
        this.tieneFirma = tieneFirma;
    }
    
    public String getTextoMarcaAgua() {
        return textoMarcaAgua;
    }
    
    public void setTextoMarcaAgua(String textoMarcaAgua) {
        this.textoMarcaAgua = textoMarcaAgua;
    }
    
    public String getTextoFirma() {
        return textoFirma;
    }
    
    public void setTextoFirma(String textoFirma) {
        this.textoFirma = textoFirma;
    }
    
    public String getHashFirma() {
        return hashFirma;
    }
    
    public void setHashFirma(String hashFirma) {
        this.hashFirma = hashFirma;
    }
}
