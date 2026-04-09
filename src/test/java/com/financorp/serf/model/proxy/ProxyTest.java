package com.financorp.serf.model.proxy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias — Patrón Proxy (RF3, RF4)
 *
 * Verifica que el proxy controle el acceso a reportes financieros
 * validando credenciales y restringiendo según el rol del usuario.
 */
@DisplayName("Patrón Proxy - Control de Acceso a Reportes")
class ProxyTest {

    private ReporteProxy proxy;

    @BeforeEach
    void setUp() {
        proxy = new ReporteProxy();
    }

    // ─── RF3: Validación de credenciales ─────────────────────────────────

    @Test
    @DisplayName("RF3 - Usuario autenticado puede generar reporte")
    void usuarioAutenticadoGeneraReporte() {
        proxy.autenticar("jlopez", RolUsuario.VENDEDOR);
        String resultado = proxy.generarReporte("MENSUAL");
        assertNotNull(resultado);
        assertTrue(resultado.contains("MENSUAL"),
                "El reporte debe incluir el tipo solicitado");
    }

    @Test
    @DisplayName("RF3 - Sin autenticación lanza SecurityException")
    void sinAutenticacionLanzaExcepcion() {
        assertThrows(SecurityException.class,
                () -> proxy.generarReporte("ANUAL"),
                "Debe lanzar SecurityException si no hay sesión activa");
    }

    @Test
    @DisplayName("RF3 - Credenciales vacías no se aceptan")
    void credencialesVaciasRechazadas() {
        assertThrows(IllegalArgumentException.class,
                () -> proxy.autenticar("", RolUsuario.GERENTE),
                "Usuario vacío debe lanzar IllegalArgumentException");
    }

    // ─── RF4: Solo Gerente y Contador acceden a datos completos ──────────

    @Test
    @DisplayName("RF4 - Gerente accede a datos financieros completos")
    void gerenteAccedeDatosFinancieros() {
        proxy.autenticar("agarcia", RolUsuario.GERENTE);
        String datos = proxy.accederDatosFinancieros();
        assertTrue(datos.contains("Utilidad neta"),
                "Gerente debe recibir datos sensibles completos");
    }

    @Test
    @DisplayName("RF4 - Contador accede a datos financieros completos")
    void contadorAccedeDatosFinancieros() {
        proxy.autenticar("mflores", RolUsuario.CONTADOR);
        String datos = proxy.accederDatosFinancieros();
        assertTrue(datos.contains("Utilidad neta"),
                "Contador debe recibir datos sensibles completos");
    }

    @Test
    @DisplayName("RF4 - Vendedor no puede acceder a datos financieros")
    void vendedorDenegadoEnDatosFinancieros() {
        proxy.autenticar("cperez", RolUsuario.VENDEDOR);
        String datos = proxy.accederDatosFinancieros();
        assertTrue(datos.contains("ACCESO DENEGADO"),
                "Vendedor debe recibir mensaje de acceso denegado");
    }

    @Test
    @DisplayName("RF4 - Compras no puede acceder a datos financieros")
    void comprasDenegadoEnDatosFinancieros() {
        proxy.autenticar("lramos", RolUsuario.COMPRAS);
        String datos = proxy.accederDatosFinancieros();
        assertTrue(datos.contains("ACCESO DENEGADO"));
    }

    @Test
    @DisplayName("RF4 - Invitado no puede acceder a datos financieros")
    void invitadoDenegadoEnDatosFinancieros() {
        proxy.autenticar("invitado01", RolUsuario.INVITADO);
        String datos = proxy.accederDatosFinancieros();
        assertTrue(datos.contains("ACCESO DENEGADO"));
    }
}
