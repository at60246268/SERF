package com.financorp.serf.model.reportes;

import com.financorp.serf.model.enums.TipoReporte;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * PATRÓN PROTOTYPE - Reporte Mensual
 * 
 * Implementación concreta de PlantillaReporte para reportes mensuales.
 * Permite clonar instancias sin instanciación directa costosa.
 * 
 * Principio SOLID: Liskov Substitution - Puede sustituir a PlantillaReporte
 * 
 * @author FinanCorp S.A.
 */
public class ReporteMensual extends PlantillaReporte {
    
    private int mes;
    private int anio;
    
    public ReporteMensual() {
        super(TipoReporte.MENSUAL);
        configurarPeriodo();
    }
    
    /**
     * Constructor con periodo específico
     */
    public ReporteMensual(int mes, int anio) {
        super(TipoReporte.MENSUAL);
        this.mes = mes;
        this.anio = anio;
        configurarPeriodoEspecifico(mes, anio);
    }
    
    @Override
    public PlantillaReporte clone() {
        ReporteMensual clonado = (ReporteMensual) super.clone();
        // Clonar campos mutables si los hubiera
        clonado.fechaGeneracion = LocalDate.now();
        clonado.fechaInicio = LocalDate.of(this.anio, this.mes, 1);
        clonado.fechaFin = this.fechaFin;
        clonado.mes = this.mes;
        clonado.anio = this.anio;
        return clonado;
    }
    
    @Override
    public void configurarPeriodo() {
        LocalDate ahora = LocalDate.now();
        this.mes = ahora.getMonthValue();
        this.anio = ahora.getYear();
        configurarPeriodoEspecifico(mes, anio);
    }
    
    private void configurarPeriodoEspecifico(int mes, int anio) {
        this.fechaInicio = LocalDate.of(anio, mes, 1);
        this.fechaFin = fechaInicio.withDayOfMonth(fechaInicio.lengthOfMonth());
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        this.periodoTexto = fechaInicio.format(formatter);
    }
    
    @Override
    public boolean validarPeriodo() {
        return mes >= 1 && mes <= 12 && anio > 2000;
    }
    
    // Getters y Setters específicos
    
    public int getMes() {
        return mes;
    }
    
    public void setMes(int mes) {
        this.mes = mes;
    }
    
    public int getAnio() {
        return anio;
    }
    
    public void setAnio(int anio) {
        this.anio = anio;
    }
}
