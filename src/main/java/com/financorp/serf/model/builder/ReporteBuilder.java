package com.financorp.serf.model.builder;

import com.financorp.serf.model.composite.ComponenteReporte;
import com.financorp.serf.model.config.ConfiguracionGlobal;
import com.financorp.serf.model.reportes.PlantillaReporte;

/**
 * PATRÓN BUILDER - Constructor de Reportes Complejos
 * 
 * Permite construir reportes paso a paso con una API fluida y legible.
 * Separa la construcción del objeto de su representación.
 * 
 * Principios SOLID aplicados:
 * - Single Responsibility: Solo construye reportes
 * - Open/Closed: Extensible para nuevos tipos de construcción
 * - Interface Segregation: API específica y clara
 * - Dependency Inversion: Usa abstracciones (ComponenteReporte)
 * 
 * @author FinanCorp S.A.
 * @version 1.0.0
 */
public class ReporteBuilder {
    
    private Reporte reporte;
    private ConfiguracionGlobal config;
    
    /**
     * Constructor privado - Se instancia mediante método estático
     */
    private ReporteBuilder() {
        this.reporte = new Reporte();
        this.config = ConfiguracionGlobal.getInstance(); // Usa SINGLETON
    }
    
    /**
     * Método estático para iniciar la construcción
     * API fluida: ReporteBuilder.nuevo()
     * 
     * @return Nueva instancia del builder
     */
    public static ReporteBuilder nuevo() {
        return new ReporteBuilder();
    }
    
    /**
     * Método estático para construir desde una plantilla (PROTOTYPE)
     * 
     * @param plantilla Plantilla clonada
     * @return Builder con datos de la plantilla
     */
    public static ReporteBuilder desdePlantilla(PlantillaReporte plantilla) {
        ReporteBuilder builder = new ReporteBuilder();
        builder.reporte.setTitulo(plantilla.getTituloCompleto());
        builder.reporte.setPeriodo(plantilla.getPeriodoTexto());
        return builder;
    }
    
    /**
     * Establece el título del reporte
     * 
     * @param titulo Título del reporte
     * @return this para encadenamiento
     */
    public ReporteBuilder conTitulo(String titulo) {
        this.reporte.setTitulo(titulo);
        return this;
    }
    
    /**
     * Establece el periodo del reporte
     * 
     * @param periodo Texto del periodo
     * @return this para encadenamiento
     */
    public ReporteBuilder conPeriodo(String periodo) {
        this.reporte.setPeriodo(periodo);
        return this;
    }
    
    /**
     * Establece el encabezado usando la configuración global (SINGLETON)
     * 
     * @return this para encadenamiento
     */
    public ReporteBuilder conEncabezado() {
        this.reporte.setEncabezado(config.getEncabezadoReportes());
        return this;
    }
    
    /**
     * Establece un encabezado personalizado
     * 
     * @param encabezado Texto del encabezado
     * @return this para encadenamiento
     */
    public ReporteBuilder conEncabezado(String encabezado) {
        this.reporte.setEncabezado(encabezado);
        return this;
    }
    
    /**
     * Añade una sección al reporte (COMPOSITE)
     * Principio: Dependency Inversion - Depende de la abstracción ComponenteReporte
     * 
     * @param seccion Componente de reporte (Seccion o Elemento)
     * @return this para encadenamiento
     */
    public ReporteBuilder conSeccion(ComponenteReporte seccion) {
        this.reporte.agregarSeccion(seccion);
        return this;
    }
    
    /**
     * Añade múltiples secciones al reporte
     * 
     * @param secciones Array de componentes
     * @return this para encadenamiento
     */
    public ReporteBuilder conSecciones(ComponenteReporte... secciones) {
        for (ComponenteReporte seccion : secciones) {
            this.reporte.agregarSeccion(seccion);
        }
        return this;
    }
    
    /**
     * Establece las conclusiones del reporte
     * 
     * @param conclusiones Texto de conclusiones
     * @return this para encadenamiento
     */
    public ReporteBuilder conConclusiones(String conclusiones) {
        this.reporte.setConclusiones(conclusiones);
        return this;
    }
    
    /**
     * Establece el pie de página usando configuración global (SINGLETON)
     * 
     * @return this para encadenamiento
     */
    public ReporteBuilder conPiePagina() {
        this.reporte.setPiePagina(config.getPieReportes());
        return this;
    }
    
    /**
     * Establece un pie de página personalizado
     * 
     * @param piePagina Texto del pie
     * @return this para encadenamiento
     */
    public ReporteBuilder conPiePagina(String piePagina) {
        this.reporte.setPiePagina(piePagina);
        return this;
    }
    
    /**
     * Construye y retorna el reporte final
     * Validaciones antes de construir
     * 
     * @return Reporte construido
     * @throws IllegalStateException si faltan datos obligatorios
     */
    public Reporte construir() {
        validarReporte();
        return this.reporte;
    }
    
    /**
     * Valida que el reporte tenga los datos mínimos necesarios
     * Principio: Encapsulamiento de validación
     */
    private void validarReporte() {
        if (reporte.getTitulo() == null || reporte.getTitulo().isEmpty()) {
            throw new IllegalStateException("El reporte debe tener un título");
        }
        if (reporte.getSecciones().isEmpty()) {
            throw new IllegalStateException("El reporte debe tener al menos una sección");
        }
    }
    
    /**
     * Reinicia el builder para construir un nuevo reporte
     * 
     * @return this para encadenamiento
     */
    public ReporteBuilder reiniciar() {
        this.reporte = new Reporte();
        return this;
    }
}
