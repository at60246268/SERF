package com.financorp.serf.model.reportes;

import com.financorp.serf.model.enums.TipoReporte;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * PATRÓN PROTOTYPE - Plantilla base de reportes
 * 
 * Permite clonar reportes sin instanciarlos directamente desde cero.
 * Todas las subclases deben implementar el método clone().
 * 
 * Principios SOLID aplicados:
 * - Single Responsibility: Solo define la estructura de reportes
 * - Open/Closed: Abierto para extensión (nuevos tipos), cerrado para modificación
 * - Liskov Substitution: Todas las plantillas son intercambiables
 * 
 * @author FinanCorp S.A.
 * @version 1.0.0
 */
public abstract class PlantillaReporte implements Cloneable {
    
    protected TipoReporte tipoReporte;
    protected String titulo;
    protected String descripcion;
    protected LocalDate fechaGeneracion;
    protected LocalDate fechaInicio;
    protected LocalDate fechaFin;
    protected String periodoTexto;
    
    /**
     * Constructor protegido para evitar instanciación directa
     * Principio: Dependency Inversion
     */
    protected PlantillaReporte(TipoReporte tipoReporte) {
        this.tipoReporte = tipoReporte;
        this.titulo = tipoReporte.getTitulo();
        this.descripcion = tipoReporte.getDescripcion();
        this.fechaGeneracion = LocalDate.now();
    }
    
    /**
     * Método clone para crear copias del reporte
     * Implementación del patrón Prototype
     * 
     * @return Copia profunda de la plantilla
     */
    @Override
    public PlantillaReporte clone() {
        try {
            PlantillaReporte clonado = (PlantillaReporte) super.clone();
            // Clonar fechas (inmutables en Java 8+)
            clonado.fechaGeneracion = LocalDate.now();
            return clonado;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("Cloneable no está siendo soportado", e);
        }
    }
    
    /**
     * Configura el periodo del reporte
     * Template Method para personalización en subclases
     */
    public abstract void configurarPeriodo();
    
    /**
     * Valida que el periodo sea correcto
     */
    public abstract boolean validarPeriodo();
    
    /**
     * Genera el título completo del reporte
     */
    public String getTituloCompleto() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        return titulo + " - " + fechaInicio.format(formatter);
    }
    
    // Getters y Setters
    
    public TipoReporte getTipoReporte() {
        return tipoReporte;
    }
    
    public void setTipoReporte(TipoReporte tipoReporte) {
        this.tipoReporte = tipoReporte;
    }
    
    public String getTitulo() {
        return titulo;
    }
    
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public LocalDate getFechaGeneracion() {
        return fechaGeneracion;
    }
    
    public void setFechaGeneracion(LocalDate fechaGeneracion) {
        this.fechaGeneracion = fechaGeneracion;
    }
    
    public LocalDate getFechaInicio() {
        return fechaInicio;
    }
    
    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }
    
    public LocalDate getFechaFin() {
        return fechaFin;
    }
    
    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }
    
    public String getPeriodoTexto() {
        return periodoTexto;
    }
    
    public void setPeriodoTexto(String periodoTexto) {
        this.periodoTexto = periodoTexto;
    }
}
