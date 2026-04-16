package com.financorp.serf.controller;

import com.financorp.serf.facade.ReporteFinancieroFacade;
import com.financorp.serf.model.builder.Reporte;
import com.financorp.serf.model.enums.TipoReporte;
import com.financorp.serf.model.proxy.ReporteProxy;
import com.financorp.serf.model.proxy.RolUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;

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
    private final ReporteProxy reporteProxy;
    
    @Autowired
    public ReporteController(ReporteFinancieroFacade reporteFacade) {
        this.reporteFacade = reporteFacade;
        this.reporteProxy = new ReporteProxy();
    }
    
    /**
     * Muestra página de selección de reporte
     */
    @GetMapping
    public String mostrarSeleccion(HttpSession session, Model model) {
        // Obtener rol del usuario de la sesión (si no existe, usar INVITADO por defecto)
        RolUsuario rolUsuario = (RolUsuario) session.getAttribute("rolUsuario");
        if (rolUsuario == null) {
            rolUsuario = RolUsuario.INVITADO;
            session.setAttribute("rolUsuario", rolUsuario);
        }
        
        model.addAttribute("tiposReporte", TipoReporte.values());
        model.addAttribute("rolActual", rolUsuario);
        return "reportes/seleccion";
    }
    
    /**
     * Genera un reporte usando el PATRÓN FACADE y PROXY
     * 
     * PROXY valida acceso basado en roles
     * FACADE coordina los demás patrones:
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
                                HttpSession session,
                                Model model,
                                RedirectAttributes redirect) {
        try {
            // Obtener rol del usuario
            RolUsuario rolUsuario = (RolUsuario) session.getAttribute("rolUsuario");
            if (rolUsuario == null) {
                rolUsuario = RolUsuario.INVITADO;
            }
            
            // Usar PROXY para verificar acceso (Proxy Pattern)
            if (rolUsuario != RolUsuario.GERENTE && rolUsuario != RolUsuario.CONTADOR) {
                redirect.addFlashAttribute("error", 
                    "Acceso denegado. Solo usuarios con rol de Gerente o Contador pueden acceder a reportes completos. Tu rol: " + rolUsuario);
                return "redirect:/reportes";
            }
            
            // Llama al FACADE que coordina todos los patrones
            Reporte reporte = reporteFacade.generarReporteCompleto(
                tipoReporte,
                conMarcaAgua,
                conFirma
            );
            
            // Enviar reporte a la vista
            model.addAttribute("reporte", reporte);
            model.addAttribute("contenidoHTML", reporte.renderizar());
            model.addAttribute("tipoReporte", tipoReporte);
            model.addAttribute("tieneMarcaAgua", conMarcaAgua);
            model.addAttribute("tieneFirma", conFirma);
            model.addAttribute("rolUsuario", rolUsuario);
            
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
    
    /**
     * Cambia el rol del usuario en la sesión (para demostración del Proxy Pattern)
     */
    @PostMapping("/cambiar-rol")
    public String cambiarRol(@RequestParam RolUsuario rol, 
                            HttpSession session,
                            RedirectAttributes redirect) {
        session.setAttribute("rolUsuario", rol);
        redirect.addFlashAttribute("success", "Rol cambiado a: " + rol);
        return "redirect:/reportes";
    }
}
