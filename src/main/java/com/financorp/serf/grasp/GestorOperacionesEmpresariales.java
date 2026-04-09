package com.financorp.serf.grasp;

import com.financorp.serf.model.adapter.GestorPasarelasPago;
import com.financorp.serf.model.command.HistorialPedidos;
import com.financorp.serf.model.command.Pedido;
import com.financorp.serf.model.memento.CaretakerPedido;
import com.financorp.serf.model.observer.GestorInventario;
import com.financorp.serf.model.proxy.ReporteProxy;
import com.financorp.serf.model.proxy.RolUsuario;
import com.financorp.serf.model.strategy.CalculadoraPrecio;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * PATRONES GRASP — GestorOperacionesEmpresariales
 *
 * Esta clase aplica y documenta explícitamente los 9 patrones GRASP
 * (General Responsibility Assignment Software Patterns) de Craig Larman
 * en el contexto del sistema TechSolutions.
 *
 * ═══════════════════════════════════════════════════════════════════════
 * GRASP 1 — INFORMATION EXPERT (Experto en Información)
 *   Principio: Asignar responsabilidad a la clase que posee la información.
 *   Aplicación: GestorInventario conoce los observadores → notifica él mismo.
 *                HistorialPedidos conoce la pila → él deshace.
 *                CalculadoraPrecio conoce la estrategia → ella calcula.
 *
 * GRASP 2 — CREATOR (Creador)
 *   Principio: Asignar a B la responsabilidad de crear A si B usa/contiene A.
 *   Aplicación: CatalogoProductos crea IteradorProductosPaginado.
 *                GestorPasarelasPago crea PayPalAdapter, YapeAdapter, PlinAdapter.
 *                CaretakerPedido crea PedidoMemento a partir del Pedido.
 *
 * GRASP 3 — CONTROLLER (Controlador)
 *   Principio: Primer objeto no-UI que recibe y coordina operaciones del sistema.
 *   Aplicación: GestorOperacionesEmpresariales actúa como coordinador GRASP
 *                de las operaciones de negocio (pago, pedido, precio, inventario).
 *                Los Spring Controllers (ProductoController, VentaController) son
 *                controladores de interfaz de usuario.
 *
 * GRASP 4 — LOW COUPLING (Bajo Acoplamiento)
 *   Principio: Minimizar dependencias entre clases.
 *   Aplicación: PasarelaPago, EstrategiaPrecio, ObservadorStock y ComandoPedido
 *                son interfaces; el cliente solo depende de la abstracción,
 *                nunca de PayPalApi, PrecioConDescuento o NotificadorGerente directamente.
 *
 * GRASP 5 — HIGH COHESION (Alta Cohesión)
 *   Principio: Mantener clases enfocadas en una sola responsabilidad.
 *   Aplicación: ReporteProxy solo controla accesos.
 *                HistorialPedidos solo gestiona el historial.
 *                CaretakerPedido solo custodia mementos.
 *                Ninguna clase mezcla responsabilidades de dominio distintas.
 *
 * GRASP 6 — POLYMORPHISM (Polimorfismo)
 *   Principio: Usar polimorfismo para manejar variaciones de comportamiento.
 *   Aplicación: EstrategiaPrecio: PrecioEstandar, PrecioConDescuento, PrecioDinamico.
 *                PasarelaPago: PayPalAdapter, YapeAdapter, PlinAdapter.
 *                ObservadorStock: NotificadorGerente, NotificadorCompras.
 *                El sistema funciona igual sin importar qué implementación se use.
 *
 * GRASP 7 — PURE FABRICATION (Fabricación Pura)
 *   Principio: Crear clase artificial sin equivalente en el dominio, solo para lograr
 *               bajo acoplamiento y alta cohesión.
 *   Aplicación: HistorialPedidos — no existe en el dominio real, creada para
 *                gestionar el historial de comandos.
 *                GestorPasarelasPago — no es una entidad de negocio, creada para
 *                registrar y administrar los adaptadores de pago.
 *                CaretakerPedido — fabricación pura para custodiar mementos.
 *
 * GRASP 8 — INDIRECTION (Indirección)
 *   Principio: Asignar responsabilidad a un objeto intermediario para desacoplar.
 *   Aplicación: ReporteProxy intermedia entre el cliente y ServicioReporteReal.
 *                GestorPasarelasPago intermedia entre el cliente y cada API de pago.
 *                ReporteFinancieroFacade intermedia entre el controller y los patrones.
 *
 * GRASP 9 — PROTECTED VARIATIONS (Variaciones Protegidas)
 *   Principio: Identificar puntos de variación y crear una interfaz estable alrededor.
 *   Aplicación: EstrategiaPrecio protege al sistema de cambios en políticas de precio.
 *                PasarelaPago protege al sistema cuando se agregue una nueva pasarela.
 *                ObservadorStock protege al sistema cuando se agregue un nuevo receptor.
 *                ComandoPedido protege al historial de cambios en los tipos de pedidos.
 * ═══════════════════════════════════════════════════════════════════════
 *
 * Principios SOLID aplicados en esta clase:
 *   - Single Responsibility: solo coordina operaciones de negocio
 *   - Dependency Inversion: depende de abstracciones (interfaces/componentes Spring)
 */
@Service
public class GestorOperacionesEmpresariales {

    // GRASP Controller: recibe y coordina operaciones del sistema
    private final GestorPasarelasPago gestorPagos;           // GRASP Creator, Indirection
    private final ReporteProxy reporteProxy;                 // GRASP Indirection
    private final GestorInventario gestorInventario;         // GRASP Information Expert
    private final HistorialPedidos historialPedidos;         // GRASP Pure Fabrication
    private final CaretakerPedido caretakerPedido;           // GRASP Pure Fabrication
    private final CalculadoraPrecio calculadoraPrecio;       // GRASP Information Expert

    // GRASP Information Expert: esta clase conoce el mapa de mínimos de stock
    private final Map<String, Integer> minimoStockPorProducto = new HashMap<>();

    public GestorOperacionesEmpresariales(
            GestorPasarelasPago gestorPagos,
            ReporteProxy reporteProxy,
            GestorInventario gestorInventario,
            HistorialPedidos historialPedidos,
            CaretakerPedido caretakerPedido,
            CalculadoraPrecio calculadoraPrecio) {
        this.gestorPagos = gestorPagos;
        this.reporteProxy = reporteProxy;
        this.gestorInventario = gestorInventario;
        this.historialPedidos = historialPedidos;
        this.caretakerPedido = caretakerPedido;
        this.calculadoraPrecio = calculadoraPrecio;
    }

    /**
     * GRASP Controller — RF1/RF2: Procesa un pago con la pasarela indicada.
     * GRASP Indirection: delega a GestorPasarelasPago sin conocer la API real.
     */
    public boolean procesarPago(String pasarela, double monto, String detalle) {
        return gestorPagos.procesarPago(pasarela, monto, detalle);
    }

    /**
     * GRASP Controller — RF3/RF4: Accede a reporte validando rol.
     * GRASP Indirection: el proxy protege la variación de seguridad.
     */
    public String accederReporte(String usuario, RolUsuario rol, String tipoReporte) {
        reporteProxy.autenticar(usuario, rol);
        return reporteProxy.generarReporte(tipoReporte);
    }

    /**
     * GRASP Controller — RF5/RF6: Actualiza stock y dispara alertas si necesario.
     * GRASP Information Expert: minimoStockPorProducto es mantenido aquí.
     * RF6: El mínimo es configurable por producto.
     */
    public void actualizarStock(String nombreProducto, int nuevoStock) {
        int minimo = minimoStockPorProducto.getOrDefault(nombreProducto, 10);
        gestorInventario.actualizarStock(nombreProducto, nuevoStock, minimo);
    }

    /**
     * RF6: Configura el mínimo de stock para un producto específico.
     * GRASP Information Expert: este gestor almacena la configuración por producto.
     */
    public void configurarMinimoStock(String nombreProducto, int minimoStock) {
        minimoStockPorProducto.put(nombreProducto, minimoStock);
    }

    /**
     * GRASP Controller — RF9/RF10: Calcula precio con la estrategia activa.
     * GRASP Protected Variations: EstrategiaPrecio protege de cambios de política.
     */
    public double calcularPrecio(double precioBase) {
        return calculadoraPrecio.calcularPrecio(precioBase);
    }

    public HistorialPedidos getHistorialPedidos() { return historialPedidos; }
    public CaretakerPedido getCaretakerPedido()   { return caretakerPedido; }
    public CalculadoraPrecio getCalculadoraPrecio() { return calculadoraPrecio; }
    public GestorPasarelasPago getGestorPagos()   { return gestorPagos; }
}
