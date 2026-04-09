package com.financorp.serf.facade;

import com.financorp.serf.model.builder.Reporte;
import com.financorp.serf.model.builder.ReporteBuilder;
import com.financorp.serf.model.composite.ComponenteReporte;
import com.financorp.serf.model.composite.ElementoReporte;
import com.financorp.serf.model.composite.SeccionReporte;
import com.financorp.serf.model.config.ConfiguracionGlobal;
import com.financorp.serf.model.decorator.FirmaDigitalDecorator;
import com.financorp.serf.model.decorator.MarcaAguaDecorator;
import com.financorp.serf.model.decorator.ReporteDecorator;
import com.financorp.serf.model.enums.Categoria;
import com.financorp.serf.model.enums.TipoReporte;
import com.financorp.serf.model.reportes.PlantillaReporte;
import com.financorp.serf.model.reportes.ReporteAnual;
import com.financorp.serf.model.reportes.ReporteMensual;
import com.financorp.serf.model.reportes.ReporteTrimestral;
import com.financorp.serf.service.ReporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.Locale;
import java.util.Map;

/**
 * PATRÓN FACADE - Fachada para generación de reportes financieros
 * 
 * Esta clase coordina todos los patrones de diseño para generar reportes completos.
 * Oculta la complejidad de usar múltiples patrones y proporciona una interfaz simple.
 * 
 * Patrones coordinados:
 * 1. SINGLETON - ConfiguracionGlobal (configuración y tasas de cambio)
 * 2. PROTOTYPE - PlantillaReporte (clonación de plantillas)
 * 3. BUILDER - ReporteBuilder (construcción paso a paso)
 * 4. COMPOSITE - ComponenteReporte (estructura jerárquica)
 * 5. DECORATOR - MarcaAguaDecorator y FirmaDigitalDecorator (funcionalidades adicionales)
 * 
 * Principios SOLID aplicados:
 * - Single Responsibility: Solo coordina la generación de reportes
 * - Open/Closed: Extensible para nuevos tipos de reportes sin modificar
 * - Dependency Inversion: Depende de abstracciones (services)
 * 
 * @author FinanCorp S.A.
 * @version 1.0.0
 */
@Component
public class ReporteFinancieroFacade {
    
    private final ReporteService reporteService;
    private final ConfiguracionGlobal config;
    private final NumberFormat formatoMoneda;
    
    @Autowired
    public ReporteFinancieroFacade(ReporteService reporteService) {
        this.reporteService = reporteService;
        this.config = ConfiguracionGlobal.getInstance(); // PATRÓN SINGLETON
        this.formatoMoneda = NumberFormat.getCurrencyInstance(Locale.GERMANY); // EUR
    }
    
    /**
     * MÉTODO PRINCIPAL DE LA FACHADA
     * Genera un reporte completo coordinando todos los patrones de diseño
     * 
     * @param tipoReporte Tipo de reporte a generar
     * @param conMarcaAgua Si se debe incluir marca de agua
     * @param conFirma Si se debe incluir firma digital
     * @return Reporte completo listo para visualizar
     */
    public Reporte generarReporteCompleto(TipoReporte tipoReporte, 
                                         boolean conMarcaAgua, 
                                         boolean conFirma) {
        
        // PASO 1: SINGLETON - Obtener configuración global
        ConfiguracionGlobal config = ConfiguracionGlobal.getInstance();
        
        // PASO 2: PROTOTYPE - Clonar plantilla según tipo de reporte
        PlantillaReporte plantilla = clonarPlantilla(tipoReporte);
        
        // PASO 3: Obtener datos del periodo
        LocalDate inicio = plantilla.getFechaInicio();
        LocalDate fin = plantilla.getFechaFin();
        Map<String, Object> estadisticas = reporteService.calcularEstadisticas(inicio, fin);
        
        // PASO 4: BUILDER - Construir reporte paso a paso
        ReporteBuilder builder = ReporteBuilder.desdePlantilla(plantilla);
        
        Reporte reporte = builder
            .conEncabezado(config.getEncabezadoReportes())
            .conSeccion(crearSeccionResumenEjecutivo(estadisticas))
            .conSeccion(crearSeccionIngresos(estadisticas))
            .conSeccion(crearSeccionAnalisis(estadisticas))
            .conConclusiones(generarConclusiones(estadisticas))
            .conPiePagina(config.getPieReportes())
            .construir();
        
        // PASO 5: DECORATOR - Aplicar decoradores si se solicitan
        if (conMarcaAgua) {
            new MarcaAguaDecorator(reporte);
        }
        
        if (conFirma) {
            new FirmaDigitalDecorator(reporte);
        }
        
        return reporte;
    }
    
    /**
     * PASO 2: PATRÓN PROTOTYPE - Clona la plantilla según el tipo
     * Evita instanciación directa costosa
     */
    private PlantillaReporte clonarPlantilla(TipoReporte tipo) {
        PlantillaReporte plantillaBase;
        
        switch (tipo) {
            case MENSUAL:
                plantillaBase = new ReporteMensual();
                break;
            case TRIMESTRAL:
                plantillaBase = new ReporteTrimestral();
                break;
            case ANUAL:
                plantillaBase = new ReporteAnual();
                break;
            default:
                plantillaBase = new ReporteMensual();
        }
        
        // Clonar la plantilla (PROTOTYPE)
        return plantillaBase.clone();
    }
    
    /**
     * PASO 4: PATRÓN COMPOSITE - Crea sección de resumen ejecutivo
     * Estructura jerárquica de componentes
     */
    private ComponenteReporte crearSeccionResumenEjecutivo(Map<String, Object> estadisticas) {
        SeccionReporte seccion = new SeccionReporte("📊 Resumen Ejecutivo", 1);
        
        // Elementos individuales (hojas del composite)
        Double ingresoTotal = (Double) estadisticas.get("ingresoTotal");
        Integer totalVentas = (Integer) estadisticas.get("totalVentas");
        Double promedioVenta = (Double) estadisticas.get("promedioVenta");
        
        seccion.agregar(new ElementoReporte(
            "Total Ingresos",
            formatoMoneda.format(ingresoTotal),
            ElementoReporte.TipoElemento.DATO
        ));
        
        seccion.agregar(new ElementoReporte(
            "Número de Ventas",
            totalVentas.toString(),
            ElementoReporte.TipoElemento.DATO
        ));
        
        seccion.agregar(new ElementoReporte(
            "Promedio por Venta",
            formatoMoneda.format(promedioVenta),
            ElementoReporte.TipoElemento.DATO
        ));
        
        return seccion;
    }
    
    /**
     * PATRÓN COMPOSITE - Crea sección de ingresos con subsecciones
     */
    @SuppressWarnings("unchecked")
    private ComponenteReporte crearSeccionIngresos(Map<String, Object> estadisticas) {
        SeccionReporte seccion = new SeccionReporte("💰 Análisis de Ingresos", 1);
        
        // Subsección: Ingresos por categoría
        SeccionReporte subseccion = new SeccionReporte("Ingresos por Categoría de Producto", 2);
        
        Map<Categoria, Double> ingresosPorCategoria = 
            (Map<Categoria, Double>) estadisticas.get("ingresosPorCategoria");
        
        if (ingresosPorCategoria != null && !ingresosPorCategoria.isEmpty()) {
            StringBuilder tabla = new StringBuilder();
            tabla.append("<table class='table table-striped'>");
            tabla.append("<thead><tr><th>Categoría</th><th>Ingresos</th><th>%</th></tr></thead>");
            tabla.append("<tbody>");
            
            Double totalIngresos = (Double) estadisticas.get("ingresoTotal");
            
            for (Map.Entry<Categoria, Double> entry : ingresosPorCategoria.entrySet()) {
                double porcentaje = (entry.getValue() / totalIngresos) * 100;
                tabla.append("<tr>")
                     .append("<td>").append(entry.getKey().getDescripcion()).append("</td>")
                     .append("<td>").append(formatoMoneda.format(entry.getValue())).append("</td>")
                     .append("<td>").append(String.format("%.1f%%", porcentaje)).append("</td>")
                     .append("</tr>");
            }
            
            tabla.append("</tbody></table>");
            
            subseccion.agregar(new ElementoReporte(
                "",
                tabla.toString(),
                ElementoReporte.TipoElemento.TABLA
            ));
        } else {
            subseccion.agregar(new ElementoReporte(
                "Sin datos",
                "No hay ventas registradas en este periodo.",
                ElementoReporte.TipoElemento.PARRAFO
            ));
        }
        
        seccion.agregar(subseccion);
        
        return seccion;
    }
    
    /**
     * PATRÓN COMPOSITE - Crea sección de análisis
     */
    private ComponenteReporte crearSeccionAnalisis(Map<String, Object> estadisticas) {
        SeccionReporte seccion = new SeccionReporte("📈 Análisis de Desempeño", 1);
        
        Double ingresoTotal = (Double) estadisticas.get("ingresoTotal");
        Integer totalVentas = (Integer) estadisticas.get("totalVentas");
        
        String analisis;
        if (ingresoTotal > 100000) {
            analisis = "El periodo muestra un desempeño excelente con ingresos superiores a €100,000. " +
                      "Se recomienda mantener la estrategia comercial actual.";
        } else if (ingresoTotal > 50000) {
            analisis = "El periodo presenta un desempeño satisfactorio. " +
                      "Se sugiere identificar oportunidades de crecimiento en categorías con menor facturación.";
        } else {
            analisis = "El periodo requiere atención. Se recomienda revisar estrategias de ventas " +
                      "y considerar promociones para incrementar el volumen de ventas.";
        }
        
        seccion.agregar(new ElementoReporte(
            "Evaluación del Periodo",
            analisis,
            ElementoReporte.TipoElemento.PARRAFO
        ));
        
        return seccion;
    }
    
    /**
     * Genera conclusiones basadas en las estadísticas
     */
    private String generarConclusiones(Map<String, Object> estadisticas) {
        Double ingresoTotal = (Double) estadisticas.get("ingresoTotal");
        Integer totalVentas = (Integer) estadisticas.get("totalVentas");
        
        return String.format(
            "Durante el periodo analizado, se registraron %d transacciones de venta " +
            "con un ingreso total de %s en moneda corporativa (EUR). " +
            "El análisis detallado por categorías y el desempeño general proporcionan " +
            "información valiosa para la toma de decisiones estratégicas.",
            totalVentas,
            formatoMoneda.format(ingresoTotal)
        );
    }
    
    /**
     * Método simplificado para generar reporte sin decoradores
     */
    public Reporte generarReporteSimple(TipoReporte tipoReporte) {
        return generarReporteCompleto(tipoReporte, false, false);
    }
    
    /**
     * Método para generar reporte con todas las características
     */
    public Reporte generarReporteCompletoConSeguridad(TipoReporte tipoReporte) {
        return generarReporteCompleto(tipoReporte, true, true);
    }
}
