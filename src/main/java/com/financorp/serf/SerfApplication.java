package com.financorp.serf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal de la aplicación SERF
 * Sistema Empresarial de Gestión de Reportes Financieros
 * 
 * @author FinanCorp S.A.
 * @version 1.0.0
 */
@SpringBootApplication
public class SerfApplication {

    public static void main(String[] args) {
        SpringApplication.run(SerfApplication.class, args);
        System.out.println("╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║   SERF - Sistema Empresarial de Reportes Financieros    ║");
        System.out.println("║   Aplicación iniciada exitosamente                       ║");
        System.out.println("║   URL: http://localhost:8080                             ║");
        System.out.println("║   H2 Console: http://localhost:8080/h2-console           ║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝");
    }
}
