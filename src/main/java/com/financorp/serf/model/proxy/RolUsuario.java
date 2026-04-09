package com.financorp.serf.model.proxy;

/**
 * PATRÓN PROXY - Enumeración de roles del sistema
 *
 * Define los roles válidos para el control de acceso a reportes financieros.
 * Solo GERENTE y CONTADOR pueden acceder a reportes completos (RF4).
 */
public enum RolUsuario {
    GERENTE,
    CONTADOR,
    VENDEDOR,
    COMPRAS,
    INVITADO
}
