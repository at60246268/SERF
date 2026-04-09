# EJERCICIO 01: Fundamentos de los Patrones de Diseño en SERF

## 📊 Cuadro Sinóptico - Sistema SERF (FinanCorp S.A.)

```
                  SISTEMA SERF - REPORTES FINANCIEROS
                               │
        ┌──────────────────────┼──────────────────────┐
        │                      │                      │
   PATRONES                ARQUITECTURA          TECNOLOGÍAS
   CREACIONALES            MVC + SPRING             STACK
        │                      │                      │
        ├─ Singleton           ├─ Model (JPA)        ├─ Java 21
        ├─ Prototype           ├─ View (Thymeleaf)   ├─ Spring Boot 3.2
        └─ Builder             └─ Controller         ├─ H2 Database
                                                     └─ Maven
   PATRONES                DOMINIO DE              PRINCIPIOS
   ESTRUCTURALES           NEGOCIO                  SOLID
        │                      │                      │
        ├─ Composite           ├─ Productos          ├─ SRP ✓
        ├─ Decorator           ├─ Ventas             ├─ OCP ✓
        └─ Facade              ├─ Filiales           ├─ LSP ✓
                              ├─ Reportes            ├─ ISP ✓
                              └─ Monedas             └─ DIP ✓
```

---

## 🎯 Contexto del Proyecto SERF

### ¿QUÉ ES SERF?

```
╔═══════════════════════════════════════════════════════════════╗
║     SISTEMA EMPRESARIAL DE REPORTES FINANCIEROS (SERF)       ║
║                    FinanCorp S.A.                             ║
╚═══════════════════════════════════════════════════════════════╝

PROPÓSITO:
Consolidar información financiera de múltiples filiales 
internacionales con diferentes monedas locales

ALCANCE:
✓ Gestión de productos tecnológicos importados desde China
✓ Registro de ventas en múltiples monedas (PEN, EUR, CLP, USD)
✓ Generación de reportes financieros consolidados
✓ Conversión automática a moneda corporativa (EUR)
✓ Seguridad en reportes (marca de agua, firma digital)

FILIALES:
• Perú (PEN) - Filial Lima
• España (EUR) - Sede Central Madrid  
• Chile (CLP) - Filial Santiago
```

---

## 🏗️ Patrones Implementados en SERF

### 1. PATRONES CREACIONALES

#### 1.1 SINGLETON - ConfiguracionGlobal

**📍 Ubicación:** `com.financorp.serf.model.config.ConfiguracionGlobal`

**🎯 Problema que Resuelve:**
En SERF necesitamos tasas de cambio de monedas consistentes en toda la aplicación. Si cada clase creara su propia instancia de configuración, tendríamos:
- ❌ Tasas de cambio inconsistentes
- ❌ Múltiples objetos en memoria
- ❌ Imposibilidad de actualizar tasas centralizadamente

**✅ Solución Implementada:**
```java
public class ConfiguracionGlobal {
    private static volatile ConfiguracionGlobal instance;
    private Map<Moneda, Double> tasasCambio;
    
    // Constructor privado
    private ConfiguracionGlobal() {
        inicializarConfiguracion();
    }
    
    // Double-Check Locking (thread-safe)
    public static ConfiguracionGlobal getInstance() {
        if (instance == null) {
            synchronized (ConfiguracionGlobal.class) {
                if (instance == null) {
                    instance = new ConfiguracionGlobal();
                }
            }
        }
        return instance;
    }
    
    // Conversión centralizada
    public double convertirAMonedaCorporativa(double monto, Moneda origen) {
        return monto * tasasCambio.get(origen);
    }
}
```

**📊 Uso Real en SERF:**
```java
// En ProductoService.java
ConfiguracionGlobal config = ConfiguracionGlobal.getInstance();
double precioEUR = config.convertirAMonedaCorporativa(
    producto.getPrecioVenta(),
    producto.getMonedaVenta()
);
```

**💡 Beneficio:** Una sola fuente de verdad para tasas de cambio en todo SERF

---

#### 1.2 PROTOTYPE - PlantillaReporte

**📍 Ubicación:** `com.financorp.serf.model.reportes.*`

**🎯 Problema que Resuelve:**
SERF genera reportes mensuales, trimestrales y anuales. Crear cada reporte desde cero sería costoso porque tienen:
- Estructura compleja preconfigurada
- Secciones estándar (ingresos, gastos, análisis)
- Configuraciones corporativas

**✅ Solución Implementada:**
```java
public abstract class PlantillaReporte implements Cloneable {
    protected TipoReporte tipoReporte;
    protected String titulo;
    protected LocalDate fechaInicio;
    protected LocalDate fechaFin;
    
    @Override
    public PlantillaReporte clone() {
        try {
            PlantillaReporte clonado = (PlantillaReporte) super.clone();
            clonado.fechaGeneracion = LocalDate.now();
            return clonado;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}

// Plantillas concretas
public class ReporteMensual extends PlantillaReporte { }
public class ReporteTrimestral extends PlantillaReporte { }
public class ReporteAnual extends PlantillaReporte { }
```

**📊 Uso Real en SERF:**
```java
// En ReporteFinancieroFacade.java
PlantillaReporte plantilla = new ReporteMensual();
PlantillaReporte reporteEnero = plantilla.clone();
PlantillaReporte reporteFebrero = plantilla.clone();
PlantillaReporte reporteMarzo = plantilla.clone();
```

**💡 Beneficio:** Clonar plantillas es más rápido que construirlas desde cero

---

#### 1.3 BUILDER - ReporteBuilder

**📍 Ubicación:** `com.financorp.serf.model.builder.ReporteBuilder`

**🎯 Problema que Resuelve:**
Los reportes en SERF tienen muchos componentes opcionales:
- Título, subtítulo, encabezado, pie de página
- Múltiples secciones jerárquicas
- Conclusiones, autores, firmas
Un constructor con todos estos parámetros sería ilegible

**✅ Solución Implementada:**
```java
public class ReporteBuilder {
    private Reporte reporte;
    
    private ReporteBuilder() {
        this.reporte = new Reporte();
    }
    
    public static ReporteBuilder nuevo() {
        return new ReporteBuilder();
    }
    
    public static ReporteBuilder desdePlantilla(PlantillaReporte plantilla) {
        ReporteBuilder builder = new ReporteBuilder();
        // Inicializar desde plantilla
        return builder;
    }
    
    // API FLUIDA
    public ReporteBuilder conTitulo(String titulo) {
        this.reporte.setTitulo(titulo);
        return this;
    }
    
    public ReporteBuilder conSeccion(ComponenteReporte seccion) {
        this.reporte.agregarSeccion(seccion);
        return this;
    }
    
    public Reporte construir() {
        return this.reporte;
    }
}
```

**📊 Uso Real en SERF:**
```java
// En ReporteFinancieroFacade.java
Reporte reporte = ReporteBuilder.desdePlantilla(plantilla)
    .conEncabezado(config.getEncabezadoReportes())
    .conSeccion(seccionIngresos)
    .conSeccion(seccionGastos)
    .conSeccion(seccionAnalisis)
    .conConclusiones("Balance positivo del trimestre")
    .conPiePagina(config.getPieReportes())
    .construir();
```

**💡 Beneficio:** Código legible y autodocumentado, fácil de mantener

---

### 2. PATRONES ESTRUCTURALES

#### 2.1 COMPOSITE - Estructura de Reportes

**📍 Ubicación:** `com.financorp.serf.model.composite.*`

**🎯 Problema que Resuelve:**
Los reportes financieros tienen estructura jerárquica:
```
Reporte Trimestral Q1
├── Sección: Análisis de Ingresos
│   ├── Subsección: Ventas Perú
│   │   ├── Elemento: Tabla de productos
│   │   └── Elemento: Gráfico de tendencia
│   └── Subsección: Ventas España
│       └── Elemento: Comparativa mensual
└── Sección: Análisis de Gastos
    └── Elemento: Resumen ejecutivo
```

Necesitamos tratar uniformemente elementos individuales y secciones compuestas.

**✅ Solución Implementada:**
```java
// Interfaz base (Component)
public interface ComponenteReporte {
    String renderizar();
    String getTitulo();
}

// Interfaz para composición (Composite interface - ISP)
public interface ComponenteCompuesto extends ComponenteReporte {
    void agregar(ComponenteReporte componente);
    boolean eliminar(ComponenteReporte componente);
    List<ComponenteReporte> getHijos();
    void limpiar();
}

// Hoja (Leaf) - Elemento simple
public class ElementoReporte implements ComponenteReporte {
    private String titulo;
    private String contenido;
    
    @Override
    public String renderizar() {
        return "<div class='elemento'>" + contenido + "</div>";
    }
}

// Nodo compuesto (Composite) - Sección con hijos
public class SeccionReporte implements ComponenteCompuesto {
    private String titulo;
    private List<ComponenteReporte> hijos = new ArrayList<>();
    
    @Override
    public void agregar(ComponenteReporte componente) {
        if (componente != null) {
            hijos.add(componente);
        }
    }
    
    @Override
    public String renderizar() {
        StringBuilder sb = new StringBuilder();
        sb.append("<section><h2>").append(titulo).append("</h2>");
        for (ComponenteReporte hijo : hijos) {
            sb.append(hijo.renderizar()); // Recursivo
        }
        sb.append("</section>");
        return sb.toString();
    }
}
```

**📊 Uso Real en SERF:**
```java
// Construir estructura jerárquica
SeccionReporte seccionIngresos = new SeccionReporte("Análisis de Ingresos");

SeccionReporte subseccionPeru = new SeccionReporte("Ventas Perú");
subseccionPeru.agregar(new ElementoReporte("Tabla", tablaHTML));
subseccionPeru.agregar(new ElementoReporte("Gráfico", graficoHTML));

seccionIngresos.agregar(subseccionPeru);

// Renderizar todo el árbol con un solo método
String html = seccionIngresos.renderizar();
```

**💡 Beneficio:** Estructura flexible y renderizado recursivo simplificado

---

#### 2.2 DECORATOR - Seguridad de Reportes

**📍 Ubicación:** `com.financorp.serf.model.decorator.*`

**🎯 Problema que Resuelve:**
Los reportes necesitan funcionalidades adicionales opcionales:
- Marca de agua "CONFIDENCIAL"
- Firma digital del auditor
- Estas funcionalidades deben poder combinarse dinámicamente

**✅ Solución Implementada:**
```java
// Decorador base
public abstract class ReporteDecorator {
    protected Reporte reporteBase;
    
    public ReporteDecorator(Reporte reporte) {
        this.reporteBase = reporte;
    }
    
    public abstract String renderizar();
    public abstract void aplicar();
}

// Decorador concreto: Marca de Agua
public class MarcaAguaDecorator extends ReporteDecorator {
    private String textoMarcaAgua;
    
    public MarcaAguaDecorator(Reporte reporte) {
        super(reporte);
        this.textoMarcaAgua = "CONFIDENCIAL - FinanCorp S.A.";
    }
    
    @Override
    public String renderizar() {
        StringBuilder sb = new StringBuilder();
        sb.append("<div class='reporte-con-marca-agua'>");
        sb.append(reporteBase.renderizar()); // Reporte original
        sb.append("<div class='marca-agua'>")
          .append(textoMarcaAgua)
          .append("</div>");
        sb.append("</div>");
        return sb.toString();
    }
}

// Decorador concreto: Firma Digital
public class FirmaDigitalDecorator extends ReporteDecorator {
    private String nombreAuditor;
    
    @Override
    public String renderizar() {
        StringBuilder sb = new StringBuilder();
        sb.append(reporteBase.renderizar());
        sb.append("<div class='firma-digital'>")
          .append("Firmado digitalmente por: ")
          .append(nombreAuditor)
          .append("</div>");
        return sb.toString();
    }
}
```

**📊 Uso Real en SERF:**
```java
// En ReporteFinancieroFacade.java
Reporte reporte = construirReporte();

// Aplicar marca de agua
if (esConfidencial) {
    reporte = new MarcaAguaDecorator(reporte);
}

// Aplicar firma digital
if (requiereFirma) {
    reporte = new FirmaDigitalDecorator(reporte, "Juan Pérez - Auditor");
}

// Los decoradores se pueden apilar
String html = reporte.renderizar();
```

**💡 Beneficio:** Extensión dinámica sin modificar la clase Reporte original (OCP)

---

#### 2.3 FACADE - ReporteFinancieroFacade

**📍 Ubicación:** `com.financorp.serf.facade.ReporteFinancieroFacade`

**🎯 Problema que Resuelve:**
Generar un reporte completo requiere coordinar 6 patrones:
1. Obtener ConfiguracionGlobal (SINGLETON)
2. Clonar PlantillaReporte (PROTOTYPE)
3. Usar ReporteBuilder (BUILDER)
4. Construir estructura jerárquica (COMPOSITE)
5. Aplicar decoradores de seguridad (DECORATOR)
6. Todo esto es complejo para un cliente

**✅ Solución Implementada:**
```java
@Component
public class ReporteFinancieroFacade {
    
    @Autowired
    private ReporteService reporteService;
    
    private ConfiguracionGlobal config;
    
    public ReporteFinancieroFacade() {
        this.config = ConfiguracionGlobal.getInstance(); // SINGLETON
    }
    
    /**
     * MÉTODO FACADE PRINCIPAL
     * Coordina todos los patrones para generar un reporte completo
     */
    public Reporte generarReporteMensual(
            Long filialId, 
            int mes, 
            int anio,
            boolean conMarcaAgua,
            boolean conFirma) {
        
        // 1. PROTOTYPE: Clonar plantilla
        PlantillaReporte plantilla = new ReporteMensual();
        PlantillaReporte plantillaClonada = plantilla.clone();
        
        // 2. BUILDER: Construir reporte paso a paso
        ReporteBuilder builder = ReporteBuilder.desdePlantilla(plantillaClonada)
            .conEncabezado(config.getEncabezadoReportes());
        
        // 3. COMPOSITE: Construir estructura jerárquica
        SeccionReporte seccionIngresos = construirSeccionIngresos(filialId, mes, anio);
        SeccionReporte seccionGastos = construirSeccionGastos(filialId, mes, anio);
        
        builder.conSeccion(seccionIngresos)
               .conSeccion(seccionGastos);
        
        Reporte reporte = builder.construir();
        
        // 4. DECORATOR: Aplicar seguridad
        if (conMarcaAgua) {
            reporte = new MarcaAguaDecorator(reporte);
        }
        if (conFirma) {
            reporte = new FirmaDigitalDecorator(reporte, "Auditor CFO");
        }
        
        return reporte;
    }
}
```

**📊 Uso Real en SERF:**
```java
// En ReporteController.java
@Autowired
private ReporteFinancieroFacade facade;

@GetMapping("/reportes/mensual")
public String generarReporteMensual(
        @RequestParam Long filialId,
        @RequestParam int mes,
        Model model) {
    
    // ¡Una sola línea! El Facade oculta toda la complejidad
    Reporte reporte = facade.generarReporteMensual(
        filialId, mes, 2026, true, true
    );
    
    model.addAttribute("reporte", reporte);
    return "reportes/visualizacion";
}
```

**💡 Beneficio:** Interfaz simple que oculta la complejidad de 6 patrones coordinados

---

## 📐 Diagrama de Integración de Patrones en SERF

```
┌─────────────────────────────────────────────────────────────┐
│                  FACADE (Punto de Entrada)                  │
│         ReporteFinancieroFacade.generarReporte()            │
└──────────────────────┬──────────────────────────────────────┘
                       │
         ┌─────────────┴──────────────┐
         ▼                            ▼
    ┌─────────┐                  ┌─────────┐
    │SINGLETON│                  │PROTOTYPE│
    │Config   │◄─────────────────│Plantilla│
    │Global   │  Usa tasas       │Reporte  │
    └─────────┘  de cambio       └────┬────┘
         │                            │
         │                            ▼
         │                       ┌─────────┐
         │                       │BUILDER  │
         │                       │Reporte  │
         │                       │Builder  │
         │                       └────┬────┘
         │                            │
         │                            ▼
         │                       ┌─────────┐
         └──────────────────────►│COMPOSITE│
                                 │Secciones│
                                 │Elementos│
                                 └────┬────┘
                                      │
                                      ▼
                                 ┌─────────┐
                                 │DECORATOR│
                                 │Marca Agua│
                                 │Firma    │
                                 └─────────┘
```

---

## ✅ Conclusiones

### Fundamentos Aplicados en SERF:

1. **SINGLETON**: Una única configuración global garantiza consistencia
2. **PROTOTYPE**: Clonación de plantillas optimiza la creación de reportes
3. **BUILDER**: API fluida facilita construcción compleja paso a paso
4. **COMPOSITE**: Estructura jerárquica permite reportes flexibles
5. **DECORATOR**: Extensión dinámica cumple principio Open/Closed
6. **FACADE**: Interfaz simple oculta complejidad de coordinación

### Relación con SOLID:

| Patrón | Principio SOLID |
|--------|----------------|
| Singleton | SRP, DIP |
| Prototype | OCP, LSP |
| Builder | SRP, ISP |
| Composite | OCP, LSP, ISP |
| Decorator | OCP, SRP |
| Facade | SRP, DIP |

**Resultado:** Arquitectura robusta, mantenible y extensible para SERF

| Año  | Hito                                              |
|------|---------------------------------------------------|
| 1977 | Christopher Alexander - "A Pattern Language"     |
| 1987 | Kent Beck y Ward Cunningham - Patrones en UI     |
| 1994 | **Gang of Four** - "Design Patterns" (23 patrones)|
| 1995 | Inicio de Pattern Languages of Programs (PLoP)   |
| 2000s| Patrones empresariales (Martin Fowler, etc.)     |
| 2020s| Patrones modernos (Microservices, Reactive, etc.)|

---

## 📐 Clasificación Fundamental

### SEGÚN EL GANG OF FOUR (GoF)

```
╔═══════════════════════════════════════════════════════════════╗
║              CLASIFICACIÓN DE PATRONES DE DISEÑO              ║
╚═══════════════════════════════════════════════════════════════╝

┌─────────────────────┬─────────────────────┬─────────────────────┐
│  CREACIONALES       │  ESTRUCTURALES      │  COMPORTAMIENTO     │
│  (Cómo crear)       │  (Cómo componer)    │  (Cómo interactúan) │
├─────────────────────┼─────────────────────┼─────────────────────┤
│                     │                     │                     │
│  • Singleton        │  • Adapter          │  • Strategy         │
│  • Factory Method   │  • Bridge           │  • Observer         │
│  • Abstract Factory │  • Composite        │  • Command          │
│  • Builder          │  • Decorator        │  • Iterator         │
│  • Prototype        │  • Facade           │  • Mediator         │
│                     │  • Flyweight        │  • Memento          │
│                     │  • Proxy            │  • State            │
│                     │                     │  • Template Method  │
│                     │                     │  • Visitor          │
│                     │                     │  • Chain of Resp.   │
│                     │                     │  • Interpreter      │
│                     │                     │                     │
│  PROPÓSITO:         │  PROPÓSITO:         │  PROPÓSITO:         │
│  Abstraer el        │  Organizar clases   │  Definir cómo       │
│  proceso de         │  y objetos para     │  interactúan y      │
│  instanciación      │  formar estructuras │  distribuyen        │
│                     │  más grandes        │  responsabilidades  │
└─────────────────────┴─────────────────────┴─────────────────────┘
```

---

## 🧩 Componentes de un Patrón

### Estructura Estándar (Según GoF)

```
┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓
┃  COMPONENTES DE UN PATRÓN DE DISEÑO                        ┃
┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛

1. NOMBRE
   └─► Vocabulario común para diseñadores
   
2. PROBLEMA
   ├─► Contexto de aplicación
   ├─► Restricciones del problema
   └─► Cuándo existe el problema
   
3. SOLUCIÓN
   ├─► Elementos participantes
   ├─► Relaciones entre elementos
   ├─► Responsabilidades
   └─► Colaboraciones
   
4. CONSECUENCIAS
   ├─► Trade-offs (ventajas y desventajas)
   ├─► Impacto en flexibilidad
   ├─► Impacto en rendimiento
   └─► Impacto en reusabilidad
```

---

## 🎓 Fundamentos Teóricos

### Principios Subyacentes

| Principio | Descripción | Relación con Patrones |
|-----------|-------------|----------------------|
| **Abstracción** | Separar "qué" del "cómo" | Factory, Bridge, Strategy |
| **Encapsulación** | Ocultar detalles de implementación | Facade, Proxy, Adapter |
| **Modularidad** | Dividir en componentes independientes | Composite, Decorator, Chain |
| **Jerarquía** | Organizar clases en estructuras | Template Method, Composite |
| **Polimorfismo** | Múltiples formas de un tipo | Strategy, State, Command |
| **Composición** | "Has-a" sobre "Is-a" | Decorator, Composite, Strategy |

---

## 💡 Beneficios Fundamentales

```
              BENEFICIOS DE LOS PATRONES DE DISEÑO
                            │
        ┌───────────────────┼───────────────────┐
        │                   │                   │
    TÉCNICOS          COMUNICACIÓN         EDUCATIVOS
        │                   │                   │
        ▼                   ▼                   ▼
        
  • Reutilización      • Vocabulario       • Aprenden mejores
  • Flexibilidad         común               prácticas
  • Mantenibilidad     • Documentación     • Entienden diseño
  • Escalabilidad        clara               OO avanzado
  • Testabilidad       • Comunicación      • Evitan reinventar
  • Desacoplamiento      efectiva            la rueda
```

### Impacto en el Ciclo de Vida del Software

```
┌─────────────┐     ┌─────────────┐     ┌─────────────┐     ┌─────────────┐
│   DISEÑO    │────▶│ DESARROLLO  │────▶│ MANTENIMIENTO────▶│  EVOLUCIÓN  │
└─────────────┘     └─────────────┘     └─────────────┘     └─────────────┘
      │                   │                    │                   │
      ▼                   ▼                    ▼                   ▼
  Decisiones          Código              Cambios             Extensiones
  documentadas        estandarizado       localizados         sin romper
                                                              existente
```

---

## 📚 Catálogo de Patrones Fundamentales

### Patrón Creacional: SINGLETON

```
PROBLEMA: ¿Cómo garantizar una única instancia global?

SOLUCIÓN:
    ┌──────────────────────┐
    │   Singleton          │
    ├──────────────────────┤
    │ -instance: Singleton │
    ├──────────────────────┤
    │ -Singleton()         │
    │ +getInstance()       │
    └──────────────────────┘
           │
           └─► Solo una instancia
```

### Patrón Estructural: DECORATOR

```
PROBLEMA: ¿Cómo agregar funcionalidad sin modificar código?

SOLUCIÓN:
    ┌─────────────┐
    │  Component  │◄─────────┐
    └─────────────┘          │
          △                  │
          │                  │
          ├──────────┬───────┤
          │          │       │
    ┌─────────┐ ┌────────────┴──┐
    │Concrete │ │  Decorator    │
    └─────────┘ └───────────────┘
                       △
                       │
                  ┌────┴────┐
                  │Concrete │
                  │Decorator│
                  └─────────┘
```

### Patrón de Comportamiento: STRATEGY

```
PROBLEMA: ¿Cómo cambiar algoritmos dinámicamente?

SOLUCIÓN:
    ┌─────────────┐         ┌──────────────┐
    │  Context    │────────▶│  Strategy    │
    └─────────────┘         └──────────────┘
                                   △
                                   │
                     ┌─────────────┼─────────────┐
                     │             │             │
              ┌──────────┐  ┌──────────┐  ┌──────────┐
              │StrategyA │  │StrategyB │  │StrategyC │
              └──────────┘  └──────────┘  └──────────┘
```

---

## 🔑 Conceptos Clave

### 1. Acoplamiento vs. Cohesión

```
ACOPLAMIENTO (Bajo es mejor)
┌─────────┐  débil  ┌─────────┐
│ Clase A │◇-------○│ Clase B │  ✓ Patrones reducen acoplamiento
└─────────┘         └─────────┘

COHESIÓN (Alta es mejor)
┌────────────────────┐
│  Clase Cohesiva    │
│ ┌────────────────┐ │
│ │ Métodos        │ │  ✓ Patrones aumentan cohesión
│ │ relacionados   │ │
│ └────────────────┘ │
└────────────────────┘
```

### 2. Composición sobre Herencia

```
❌ HERENCIA (Rígido)          ✅ COMPOSICIÓN (Flexible)

    ┌─────────┐                   ┌─────────┐
    │  Base   │                   │ Cliente │
    └─────────┘                   └─────────┘
         △                              │
         │                              ○ tiene un
    ┌────┴────┐                         │
    │         │                    ┌────▼────┐
┌───┴───┐ ┌───┴───┐               │Componente│
│Hijo A │ │Hijo B │               └─────────┘
└───────┘ └───────┘
                                  Patrones como Decorator,
                                  Strategy usan composición
```

### 3. Separación de Responsabilidades

```
SIN PATRÓN                           CON PATRÓN
┌─────────────────┐                  ┌──────────┐
│  ClaseGigante   │                  │  Clase1  │ (Una responsabilidad)
│  ─────────────  │                  └──────────┘
│  • Lógica A     │                  ┌──────────┐
│  • Lógica B     │       ────▶      │  Clase2  │ (Otra responsabilidad)
│  • Lógica C     │                  └──────────┘
│  • Lógica D     │                  ┌──────────┐
└─────────────────┘                  │  Clase3  │ (Otra más)
                                     └──────────┘
```

---

## 📖 Principios SOLID Relacionados

| Principio | Sigla | Relación con Patrones |
|-----------|-------|----------------------|
| **Single Responsibility** | S | Cada patrón tiene una responsabilidad clara |
| **Open/Closed** | O | Patrones extienden sin modificar (Decorator, Strategy) |
| **Liskov Substitution** | L | Polimorfismo en patrones (Template Method) |
| **Interface Segregation** | I | Interfaces específicas (Facade, Adapter) |
| **Dependency Inversion** | D | Dependencias de abstracciones (Factory, DI) |

---

## 🎯 Cuándo Usar Patrones

### Diagrama de Decisión

```
      ¿Existe un problema recurrente?
                  │
            ┌─────┴─────┐
           NO           SÍ
            │            │
            ▼            ▼
    No usar patrón   ¿Hay un patrón que aplique?
                          │
                    ┌─────┴─────┐
                   NO           SÍ
                    │            │
                    ▼            ▼
            Diseño custom   ¿Vale la pena la complejidad?
                                 │
                           ┌─────┴─────┐
                          NO           SÍ
                           │            │
                           ▼            ▼
                    Solución       APLICAR
                    simple         PATRÓN ✓
```

---

## ⚠️ Anti-patrones y Errores Comunes

```
╔════════════════════════════════════════════════════════╗
║  ERRORES AL USAR PATRONES                              ║
╚════════════════════════════════════════════════════════╝

❌ Pattern Happy (usar patrones innecesarios)
❌ Golden Hammer (aplicar el mismo patrón a todo)
❌ Over-Engineering (complejidad innecesaria)
❌ Cargo Cult Programming (copiar sin entender)
❌ Usar patrones para problemas simples

✅ MEJOR PRÁCTICA: Usa patrones cuando:
   • El problema es recurrente
   • La solución está probada
   • Los beneficios superan la complejidad
   • El equipo entiende el patrón
```

---

## 📊 Resumen Ejecutivo

```
┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓
┃  FUNDAMENTOS DE PATRONES DE DISEÑO - PUNTOS CLAVE        ┃
┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛

1. DEFINICIÓN
   Son soluciones reutilizables a problemas comunes de diseño

2. CLASIFICACIÓN
   • Creacionales (5)
   • Estructurales (7)
   • Comportamiento (11)

3. BENEFICIOS
   • Reutilización de código
   • Comunicación efectiva
   • Diseño flexible y mantenible

4. PRINCIPIOS BASE
   • Abstracción
   • Encapsulación
   • Composición sobre herencia
   • Separación de responsabilidades

5. RELACIÓN CON SOLID
   Los patrones implementan y refuerzan los principios SOLID

6. CUÁNDO USAR
   Cuando los beneficios superan la complejidad adicional
```

---

## 🌐 Ecosistema de Patrones

```
        PATRONES DE DISEÑO (GoF, 1994)
                    │
        ┌───────────┼───────────┐
        │           │           │
    PATRONES    PATRONES    PATRONES
  ARQUITECTURA EMPRESARI  CONCURRENCIA
  (MVC, MVP)   (DAO, DTO)  (Thread Pool)
        │           │           │
        └───────────┴───────────┘
                    │
            PATRONES MODERNOS
          (Microservices, Reactive)
```

---

## 📝 Conclusiones

Los **patrones de diseño** son herramientas fundamentales en el arsenal de todo desarrollador de software profesional. No son código específico, sino **plantillas conceptuales** que:

1. **Documentan conocimiento colectivo** de décadas de experiencia
2. **Facilitan la comunicación** mediante un vocabulario compartido
3. **Mejoran la calidad del software** al aplicar soluciones probadas
4. **Aceleran el desarrollo** al evitar reinventar soluciones
5. **Fomentan las mejores prácticas** de diseño orientado a objetos

Su correcta aplicación, combinada con los principios SOLID, resulta en sistemas software robustos, flexibles y mantenibles.

---

**Elaborado por:** [Tu nombre]  
**Fecha:** 11 de marzo de 2026  
**Proyecto:** SERF - Sistema Empresarial de Reportes Financieros  
**Tecnología:** Java 21 + Spring Boot 3.2.3
