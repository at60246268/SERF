package com.financorp.serf.model.reportes;

import com.financorp.serf.model.enums.TipoReporte;
import java.time.LocalDate;

/**
 * PATRÓN PROTOTYPE - Reporte Anual
 * 
 * Implementación concreta de PlantillaReporte para reportes anuales.
 * 
 * Principio SOLID: Liskov Substitution - Intercambiable con PlantillaReporte
 * 
 * @author FinanCorp S.A.
 */
public class ReporteAnual extends PlantillaReporte {
    
    private int anio;
    
    public ReporteAnual() {
        super(TipoReporte.ANUAL);
        configurarPeriodo();
    }
    
    public ReporteAnual(int anio) {
        super(TipoReporte.ANUAL);
        this.anio = anio;
        configurarPeriodoEspecifico(anio);
    }
    
    @Override
    public PlantillaReporte clone() {
        ReporteAnual clonado = (ReporteAnual) super.clone();
        clonado.fechaGeneracion = LocalDate.now();
        clonado.fechaInicio = this.fechaInicio;
        clonado.fechaFin = this.fechaFin;
        clonado.anio = this.anio;
        return clonado;
    }
    
    @Override
    public void configurarPeriodo() {
        this.anio = LocalDate.now().getYear();
        configurarPeriodoEspecifico(anio);
    }
    
    private void configurarPeriodoEspecifico(int anio) {
        this.fechaInicio = LocalDate.of(anio, 1, 1);
        this.fechaFin = LocalDate.of(anio, 12, 31);
        this.periodoTexto = "Año " + anio;
    }
    
    @Override
    public boolean validarPeriodo() {
        return anio > 2000 && anio <= LocalDate.now().getYear();
    }
    
    // Getters y Setters
    
    public int getAnio() {
        return anio;
    }
    
    public void setAnio(int anio) {
        this.anio = anio;
    }
}
