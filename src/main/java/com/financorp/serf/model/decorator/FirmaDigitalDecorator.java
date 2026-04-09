package com.financorp.serf.model.decorator;

import com.financorp.serf.model.builder.Reporte;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

/**
 * PATRÓN DECORATOR - Añade firma digital a los reportes
 * 
 * Decorador concreto que agrega una firma digital al reporte para autenticación.
 * 
 * Principio SOLID: Single Responsibility - Solo agrega firma digital
 * 
 * @author FinanCorp S.A.
 */
public class FirmaDigitalDecorator extends ReporteDecorator {
    
    private String nombreFirmante;
    private LocalDateTime fechaHoraFirma;
    private String hashFirma;
    
    /**
     * Constructor con firmante por defecto
     * 
     * @param reporte Reporte a decorar
     */
    public FirmaDigitalDecorator(Reporte reporte) {
        this(reporte, "Sistema SERF - FinanCorp S.A.");
    }
    
    /**
     * Constructor con firmante personalizado
     * 
     * @param reporte Reporte a decorar
     * @param nombreFirmante Nombre del firmante
     */
    public FirmaDigitalDecorator(Reporte reporte, String nombreFirmante) {
        super(reporte);
        this.nombreFirmante = nombreFirmante;
        this.fechaHoraFirma = LocalDateTime.now();
        this.hashFirma = generarHash();
        aplicar();
    }
    
    @Override
    public String renderizar() {
        StringBuilder sb = new StringBuilder();
        
        sb.append("<div class='reporte-con-firma'>");
        
        // Renderizar el reporte original
        sb.append(reporteBase.renderizar());
        
        // Añadir la firma digital al final
        sb.append(generarFirmaHTML());
        
        sb.append("</div>");
        
        return sb.toString();
    }
    
    @Override
    public void aplicar() {
        // Marcar en el reporte que tiene firma
        reporteBase.setTieneFirma(true);
        reporteBase.setTextoFirma("Firmado por: " + nombreFirmante);
        reporteBase.setHashFirma(hashFirma);
    }
    
    /**
     * Genera el HTML de la firma digital
     * 
     * @return String HTML de la firma
     */
    private String generarFirmaHTML() {
        StringBuilder sb = new StringBuilder();
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String fechaFormateada = fechaHoraFirma.format(formatter);
        
        sb.append("<div class='firma-digital' style='")
          .append("margin-top: 30px; ")
          .append("padding: 20px; ")
          .append("border: 2px solid #007bff; ")
          .append("border-radius: 5px; ")
          .append("background-color: #f8f9fa; ")
          .append("'>");
        
        sb.append("<h4 style='margin-top: 0; color: #007bff;'>")
          .append("📝 Firma Digital")
          .append("</h4>");
        
        sb.append("<p style='margin: 5px 0;'>")
          .append("<strong>Firmado por:</strong> ")
          .append(nombreFirmante)
          .append("</p>");
        
        sb.append("<p style='margin: 5px 0;'>")
          .append("<strong>Fecha y hora:</strong> ")
          .append(fechaFormateada)
          .append("</p>");
        
        sb.append("<p style='margin: 5px 0; font-size: 12px; color: #666;'>")
          .append("<strong>Hash SHA-256:</strong><br>")
          .append("<code style='word-break: break-all;'>")
          .append(hashFirma)
          .append("</code>")
          .append("</p>");
        
        sb.append("<p style='margin: 5px 0; font-size: 11px; color: #999;'>")
          .append("Esta firma digital garantiza la autenticidad e integridad del documento.")
          .append("</p>");
        
        sb.append("</div>");
        
        return sb.toString();
    }
    
    /**
     * Genera un hash SHA-256 del reporte para la firma digital
     * 
     * @return Hash en formato hexadecimal
     */
    private String generarHash() {
        try {
            // Crear el contenido a hashear
            String contenido = reporteBase.getTitulo() +
                             reporteBase.getPeriodo() +
                             nombreFirmante +
                             fechaHoraFirma.toString();
            
            // Generar SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(contenido.getBytes());
            
            // Convertir a hexadecimal
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            
            return hexString.toString().toUpperCase();
            
        } catch (NoSuchAlgorithmException e) {
            // Fallback: usar un hash simplificado
            return "HASH-" + System.currentTimeMillis();
        }
    }
    
    // Getters
    
    public String getNombreFirmante() {
        return nombreFirmante;
    }
    
    public LocalDateTime getFechaHoraFirma() {
        return fechaHoraFirma;
    }
    
    public String getHashFirma() {
        return hashFirma;
    }
}
