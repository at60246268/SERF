package com.financorp.serf.model.proxy;

import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * PATRÓN PROXY - Proxy de protección para reportes financieros
 *
 * Intercepta todas las solicitudes de acceso a reportes y valida:
 *   - Que el usuario tenga credenciales válidas (RF3)
 *   - Que el rol sea GERENTE o CONTADOR para datos completos (RF4)
 *
 * El cliente llama a este proxy exactamente igual que al servicio real,
 * sin saber que existe una capa de seguridad intermedia.
 *
 * Principio SOLID:
 *   - Single Responsibility: solo controla acceso, no genera reportes
 *   - Open/Closed: nuevas reglas de acceso se agregan sin modificar el real
 */
@Component
public class ReporteProxy implements ServicioReporte {

    private static final Set<RolUsuario> ROLES_ACCESO_COMPLETO =
            Set.of(RolUsuario.GERENTE, RolUsuario.CONTADOR);

    private final ServicioReporte servicioReal;

    // Usuario autenticado en la sesión actual
    private String usuarioActual;
    private RolUsuario rolActual;

    public ReporteProxy() {
        this.servicioReal = new ServicioReporteReal();
    }

    /**
     * RF3: Establece el usuario autenticado con sus credenciales y rol.
     */
    public void autenticar(String usuario, RolUsuario rol) {
        if (usuario == null || usuario.isBlank()) {
            throw new IllegalArgumentException("Credenciales inválidas: usuario no puede ser vacío.");
        }
        this.usuarioActual = usuario;
        this.rolActual = rol;
        System.out.println("[ReporteProxy] Usuario autenticado: " + usuario + " | Rol: " + rol);
    }

    @Override
    public String generarReporte(String tipoReporte) {
        validarAutenticacion();
        System.out.println("[ReporteProxy] Acceso a reporte '" + tipoReporte +
                "' por usuario: " + usuarioActual + " (" + rolActual + ")");
        return servicioReal.generarReporte(tipoReporte);
    }

    /**
     * RF4: Solo GERENTE o CONTADOR pueden acceder a datos financieros completos.
     */
    @Override
    public String accederDatosFinancieros() {
        validarAutenticacion();
        if (!ROLES_ACCESO_COMPLETO.contains(rolActual)) {
            String mensaje = "[ReporteProxy] ACCESO DENEGADO: el rol '" + rolActual +
                    "' no tiene permisos para ver datos financieros completos.";
            System.out.println(mensaje);
            return mensaje;
        }
        System.out.println("[ReporteProxy] Acceso autorizado a datos financieros para: " +
                usuarioActual + " (" + rolActual + ")");
        return servicioReal.accederDatosFinancieros();
    }

    private void validarAutenticacion() {
        if (usuarioActual == null || rolActual == null) {
            throw new SecurityException("Acceso denegado: debe autenticarse antes de acceder a reportes.");
        }
    }

    public String getUsuarioActual() {
        return usuarioActual;
    }

    public RolUsuario getRolActual() {
        return rolActual;
    }
}
