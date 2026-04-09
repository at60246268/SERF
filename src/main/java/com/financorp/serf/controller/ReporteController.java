package com.financorp.serf.controller;

import com.financorp.serf.facade.ReporteFinancieroFacade;
import com.financorp.serf.model.builder.Reporte;
import com.financorp.serf.model.enums.TipoReporte;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller para generación de reportes
 * 
 * Principio SOLID: Single Responsibility - Solo maneja reportes
 * 
 * Usa PATRÓN FACADE: ReporteFinancieroFacade
 * La fachada coordina todos los demás patrones internamente
 * 
 * @author FinanCorp S.A.
 */
@Controller
@RequestMapping("/reportes")
public class ReporteController {
    
    private final ReporteFinancieroFacade reporteFacade;
    
    @Autowired
    public ReporteController(ReporteFinancieroFacade reporteFacade) {
        this.reporteFacade = reporteFacade;
    }
    
    /**
     * Muestra página de selección de reporte
     */
    @GetMapping
    public String mostrarSeleccion(Model model) {
        model.addAttribute("tiposReporte", TipoReporte.values());
        return "reportes/seleccion";
    }
    
    /**
     * Genera un reporte usando el PATRÓN FACADE
     * 
     * La fachada internamente coordina:
     * 1. SINGLETON - ConfiguracionGlobal
     * 2. PROTOTYPE - Clonación de plantillas
     * 3. BUILDER - Construcción del reporte
     * 4. COMPOSITE - Estructura jerárquica
     * 5. DECORATOR - Marca de agua y firma
     */
    @PostMapping("/generar")
    public String generarReporte(@RequestParam TipoReporte tipoReporte,
                                @RequestParam(required = false, defaultValue = "false") boolean conMarcaAgua,
                                @RequestParam(required = false, defaultValue = "false") boolean conFirma,
                                Model model,
                                RedirectAttributes redirect) {
        try {
            // Llama al FACADE que coordina todos los patrones
            Reporte reporte = reporteFacade.generarReporteCompleto(
                tipoReporte,
                conMarcaAgua,
                conFirma
            );
            
            // Enviar reporte a la vista
            model.addAttribute("reporte", reporte);
            model.addAttribute("contenidoHTML", reporte.renderizar());
            
            // Información adicional para la vista
            model.addAttribute("tipoReporte", tipoReporte);
            model.addAttribute("tieneMarcaAgua", conMarcaAgua);
            model.addAttribute("tieneFirma", conFirma);
            
            return "reportes/visualizacion";
            
        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Error al generar reporte: " + e.getMessage());
            return "redirect:/reportes";
        }
    }
    
    /**
     * Genera reporte rápido (sin opciones)
     */
    @GetMapping("/generar/{tipo}")
    public String generarReporteRapido(@PathVariable TipoReporte tipo, 
                                      Model model, 
                                      RedirectAttributes redirect) {
        try {
            Reporte reporte = reporteFacade.generarReporteSimple(tipo);
            
            model.addAttribute("reporte", reporte);
            model.addAttribute("contenidoHTML", reporte.renderizar());
            model.addAttribute("tipoReporte", tipo);
            
            return "reportes/visualizacion";
        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Error al generar reporte: " + e.getMessage());
            return "redirect:/reportes";
        }
    }
}
