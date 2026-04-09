package com.financorp.serf.model.reportes;

import com.financorp.serf.model.enums.TipoReporte;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * PATRÓN PROTOTYPE - Reporte Trimestral
 * 
 * Implementación concreta de PlantillaReporte para reportes trimestrales.
 * 
 * Principio SOLID: Liskov Substitution - Intercambiable con PlantillaReporte
 * 
 * @author FinanCorp S.A.
 */
public class ReporteTrimestral extends PlantillaReporte {
    
    private int trimestre; // 1, 2, 3, 4
    private int anio;
    
    public ReporteTrimestral() {
        super(TipoReporte.TRIMESTRAL);
        configurarPeriodo();
    }
    
    public ReporteTrimestral(int trimestre, int anio) {
        super(TipoReporte.TRIMESTRAL);
        this.trimestre = trimestre;
        this.anio = anio;
        configurarPeriodoEspecifico(trimestre, anio);
    }
    
    @Override
    public PlantillaReporte clone() {
        ReporteTrimestral clonado = (ReporteTrimestral) super.clone();
        clonado.fechaGeneracion = LocalDate.now();
        clonado.fechaInicio = this.fechaInicio;
        clonado.fechaFin = this.fechaFin;
        clonado.trimestre = this.trimestre;
        clonado.anio = this.anio;
        return clonado;
    }
    
    @Override
    public void configurarPeriodo() {
        LocalDate ahora = LocalDate.now();
        this.anio = ahora.getYear();
        this.trimestre = (ahora.getMonthValue() - 1) / 3 + 1;
        configurarPeriodoEspecifico(trimestre, anio);
    }
    
    private void configurarPeriodoEspecifico(int trimestre, int anio) {
        int mesInicio = (trimestre - 1) * 3 + 1;
        int mesFin = mesInicio + 2;
        
        this.fechaInicio = LocalDate.of(anio, mesInicio, 1);
        this.fechaFin = LocalDate.of(anio, mesFin, 1)
                .withDayOfMonth(LocalDate.of(anio, mesFin, 1).lengthOfMonth());
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM");
        String mesInicioTexto = fechaInicio.format(formatter);
        String mesFinTexto = fechaFin.format(formatter);
        
        this.periodoTexto = "Q" + trimestre + " " + anio + " (" + mesInicioTexto + " - " + mesFinTexto + ")";
    }
    
    @Override
    public boolean validarPeriodo() {
        return trimestre >= 1 && trimestre <= 4 && anio > 2000;
    }
    
    // Getters y Setters
    
    public int getTrimestre() {
        return trimestre;
    }
    
    public void setTrimestre(int trimestre) {
        this.trimestre = trimestre;
    }
    
    public int getAnio() {
        return anio;
    }
    
    public void setAnio(int anio) {
        this.anio = anio;
    }
}
