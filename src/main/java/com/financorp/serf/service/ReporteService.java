package com.financorp.serf.service;

import com.financorp.serf.model.entities.Venta;
import com.financorp.serf.model.enums.Categoria;
import com.financorp.serf.repository.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service para gestión de reportes
 * Proporciona datos procesados para la generación de reportes
 * 
 * Principio SOLID: Single Responsibility - Solo obtiene y procesa datos para reportes
 * 
 * @author FinanCorp S.A.
 */
@Service
public class ReporteService {
    
    private final VentaRepository ventaRepository;
    
    @Autowired
    public ReporteService(VentaRepository ventaRepository) {
        this.ventaRepository = ventaRepository;
    }
    
    /**
     * Obtiene el total de ingresos en un periodo (en EUR)
     */
    public Double obtenerIngresosTotal(LocalDate inicio, LocalDate fin) {
        Double total = ventaRepository.calcularTotalVentasEnPeriodo(inicio, fin);
        return total != null ? total : 0.0;
    }
    
    /**
     * Obtiene ingresos agrupados por categoría
     */
    public Map<Categoria, Double> obtenerIngresosPorCategoria(LocalDate inicio, LocalDate fin) {
        List<Object[]> resultados = ventaRepository.obtenerVentasPorCategoria(inicio, fin);
        Map<Categoria, Double> ingresosPorCategoria = new HashMap<>();
        
        for (Object[] resultado : resultados) {
            Categoria categoria = (Categoria) resultado[0];
            Double monto = (Double) resultado[1];
            ingresosPorCategoria.put(categoria, monto != null ? monto : 0.0);
        }
        
        return ingresosPorCategoria;
    }
    
    /**
     * Obtiene ventas en un periodo
     */
    public List<Venta> obtenerVentasPeriodo(LocalDate inicio, LocalDate fin) {
        return ventaRepository.findByFechaVentaBetween(inicio, fin);
    }
    
    /**
     * Calcula estadísticas generales de un periodo
     */
    public Map<String, Object> calcularEstadisticas(LocalDate inicio, LocalDate fin) {
        Map<String, Object> estadisticas = new HashMap<>();
        
        List<Venta> ventas = obtenerVentasPeriodo(inicio, fin);
        Double ingresoTotal = obtenerIngresosTotal(inicio, fin);
        Map<Categoria, Double> ingresosPorCategoria = obtenerIngresosPorCategoria(inicio, fin);
        
        estadisticas.put("totalVentas", ventas.size());
        estadisticas.put("ingresoTotal", ingresoTotal);
        estadisticas.put("ingresosPorCategoria", ingresosPorCategoria);
        estadisticas.put("promedioVenta", ventas.isEmpty() ? 0.0 : ingresoTotal / ventas.size());
        
        return estadisticas;
    }
}
