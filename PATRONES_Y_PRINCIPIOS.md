# 📐 Patrones de Diseño y Principios SOLID - Sistema SERF

## 📋 Tabla de Contenidos
1. [Introducción](#introducción)
2. [Patrones de Diseño Implementados](#patrones-de-diseño-implementados)
   - [Patrones Creacionales](#patrones-creacionales)
   - [Patrones Estructurales](#patrones-estructurales)
3. [Principios SOLID Aplicados](#principios-solid-aplicados)
4. [Diagrama de Integración](#diagrama-de-integración)
5. [Beneficios de la Arquitectura](#beneficios-de-la-arquitectura)

---

## 🎯 Introducción

El **Sistema Empresarial de Reportes Financieros (SERF)** es una aplicación Spring Boot que implementa **6 patrones de diseño** de manera integrada para proporcionar una arquitectura robusta, escalable y mantenible. Este documento explica el **por qué** y el **cómo** de cada patrón, así como la aplicación de los **5 principios SOLID**.

---

## 🏗️ Patrones de Diseño Implementados

### 🔨 Patrones Creacionales

#### 1. **Singleton** 
**Ubicación:** `com.financorp.serf.model.config.ConfiguracionGlobal`

**¿Por qué se usa?**
- Garantiza que existe **una sola instancia** de la configuración global en toda la aplicación
- Evita inconsistencias al tener múltiples configuraciones
- Proporciona un punto de acceso global a tasas de cambio, formatos y configuraciones corporativas

**¿Cómo se implementa?**
```java
public class ConfiguracionGlobal {
    private static volatile ConfiguracionGlobal instance;
    
    private ConfiguracionGlobal() {
        inicializarConfiguracion();
    }
    
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
}
```

**Características clave:**
- ✅ **Thread-safe** usando Double-Check Locking
- ✅ Constructor privado previene instanciación directa
- ✅ Variable `volatile` garantiza visibilidad entre hilos
- ✅ Gestiona tasas de cambio de monedas, formatos de fecha y configuraciones de reportes

---

#### 2. **Prototype**
**Ubicación:** `com.financorp.serf.model.reportes.PlantillaReporte`

**¿Por qué se usa?**
- Permite **clonar plantillas de reportes** sin tener que reconstruirlas desde cero
- Optimiza la creación de reportes similares (mensual, trimestral, anual)
- Evita el acoplamiento de clases concretas

**¿Cómo se implementa?**
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
            throw new AssertionError("Cloneable no está siendo soportado", e);
        }
    }
}
```

**Subclases disponibles:**
- `ReporteMensual` - Plantilla para reportes mensuales
- `ReporteTrimestral` - Plantilla para reportes trimestrales
- `ReporteAnual` - Plantilla para reportes anuales

**Beneficios:**
- ✅ Reduce el costo de crear objetos complejos
- ✅ Facilita la creación de múltiples reportes del mismo tipo
- ✅ Permite copias profundas o superficiales según necesidad

---

#### 3. **Builder**
**Ubicación:** `com.financorp.serf.model.builder.ReporteBuilder`

**¿Por qué se usa?**
- Los reportes tienen **construcción compleja** con muchos parámetros opcionales
- Proporciona una **API fluida y legible** para construir reportes paso a paso
- Separa la construcción del objeto de su representación
- Permite crear diferentes representaciones del mismo proceso de construcción

**¿Cómo se implementa?**
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
        builder.reporte.setTitulo(plantilla.getTituloCompleto());
        return builder;
    }
    
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

**Ejemplo de uso:**
```java
Reporte reporte = ReporteBuilder.desdePlantilla(plantilla)
    .conEncabezado(config.getEncabezadoReportes())
    .conSeccion(seccionIngresos)
    .conSeccion(seccionGastos)
    .conConclusiones("Análisis completo")
    .conPiePagina(config.getPieReportes())
    .construir();
```

**Ventajas:**
- ✅ Código legible y autodocumentado
- ✅ Manejo de parámetros opcionales sin constructores sobrecargados
- ✅ Validación durante la construcción
- ✅ Inmutabilidad del objeto final

---

### 🏛️ Patrones Estructurales

#### 4. **Composite**
**Ubicación:** `com.financorp.serf.model.composite.*`

**¿Por qué se usa?**
- Los reportes tienen **estructura jerárquica** de secciones y elementos
- Permite tratar uniformemente objetos individuales y composiciones
- Facilita agregar/eliminar secciones dinámicamente
- Simplifica el renderizado recursivo del reporte

**¿Cómo se implementa?**

**Interfaz común:**
```java
public interface ComponenteReporte {
    String renderizar();
    void agregar(ComponenteReporte componente);
    void eliminar(ComponenteReporte componente);
    String getTitulo();
    boolean esCompuesto();
}
```

**Elemento hoja (leaf):**
```java
public class ElementoReporte implements ComponenteReporte {
    private String titulo;
    private String contenido;
    
    @Override
    public String renderizar() {
        return "<div class='elemento'>" + contenido + "</div>";
    }
    
    @Override
    public void agregar(ComponenteReporte c) {
        // Los elementos hoja no pueden tener hijos
    }
}
```

**Elemento compuesto:**
```java
public class SeccionReporte implements ComponenteReporte {
    private String titulo;
    private List<ComponenteReporte> hijos = new ArrayList<>();
    
    @Override
    public String renderizar() {
        StringBuilder html = new StringBuilder();
        html.append("<section><h2>").append(titulo).append("</h2>");
        for (ComponenteReporte hijo : hijos) {
            html.append(hijo.renderizar());
        }
        html.append("</section>");
        return html.toString();
    }
    
    @Override
    public void agregar(ComponenteReporte componente) {
        hijos.add(componente);
    }
}
```

**Estructura de árbol resultante:**
```
Reporte
├── Sección: Resumen Ejecutivo
│   ├── Elemento: Introducción
│   └── Elemento: KPIs principales
├── Sección: Análisis de Ingresos
│   ├── Elemento: Ingresos por producto
│   ├── Elemento: Comparativa mensual
│   └── Sub-sección: Análisis por región
│       ├── Elemento: América
│       └── Elemento: Europa
└── Sección: Conclusiones
    └── Elemento: Resumen final
```

**Beneficios:**
- ✅ Tratamiento uniforme de hojas y compuestos
- ✅ Fácil agregar nuevos tipos de componentes
- ✅ Operaciones recursivas simplificadas
- ✅ Estructura flexible y escalable

---

#### 5. **Decorator**
**Ubicación:** `com.financorp.serf.model.decorator.*`

**¿Por qué se usa?**
- Permite **agregar funcionalidades dinámicamente** a los reportes sin modificar su estructura
- Evita la explosión de subclases (ReporteConMarca, ReporteConFirma, ReporteConMarcaYFirma...)
- Los decoradores se pueden **apilar** para combinar funcionalidades
- Respeta el Principio Open/Closed (abierto para extensión, cerrado para modificación)

**¿Cómo se implementa?**

**Clase base abstracta:**
```java
public abstract class ReporteDecorator {
    protected Reporte reporteBase;
    
    public ReporteDecorator(Reporte reporte) {
        if (reporte == null) {
            throw new IllegalArgumentException("El reporte no puede ser nulo");
        }
        this.reporteBase = reporte;
    }
    
    public abstract String renderizar();
    public abstract void aplicar();
    
    public Reporte getReporteBase() {
        return reporteBase;
    }
}
```

**Decorador concreto - Marca de agua:**
```java
public class MarcaAguaDecorator extends ReporteDecorator {
    private String textoMarca;
    
    public MarcaAguaDecorator(Reporte reporte) {
        super(reporte);
        this.textoMarca = "CONFIDENCIAL - FinanCorp S.A.";
    }
    
    @Override
    public void aplicar() {
        reporteBase.setMarcaAgua(textoMarca);
    }
    
    @Override
    public String renderizar() {
        aplicar();
        return reporteBase.renderizarContenido();
    }
}
```

**Decorador concreto - Firma digital:**
```java
public class FirmaDigitalDecorator extends ReporteDecorator {
    private String firmaDigital;
    
    public FirmaDigitalDecorator(Reporte reporte) {
        super(reporte);
        this.firmaDigital = generarFirma();
    }
    
    @Override
    public void aplicar() {
        reporteBase.setFirmaDigital(firmaDigital);
        reporteBase.setFirmado(true);
    }
    
    @Override
    public String renderizar() {
        aplicar();
        return reporteBase.renderizarContenido();
    }
    
    private String generarFirma() {
        // Genera hash SHA-256 del contenido
        return "SHA256:..." + System.currentTimeMillis();
    }
}
```

**Ejemplo de uso (apilando decoradores):**
```java
Reporte reporte = builder.construir();

// Aplicar marca de agua
new MarcaAguaDecorator(reporte).aplicar();

// Aplicar firma digital
new FirmaDigitalDecorator(reporte).aplicar();
```

**Ventajas:**
- ✅ Extensión de funcionalidad sin modificar código existente
- ✅ Decoradores reutilizables y combinables
- ✅ Responsabilidades separadas
- ✅ Alternativa flexible a la herencia

---

#### 6. **Facade**
**Ubicación:** `com.financorp.serf.facade.ReporteFinancieroFacade`

**¿Por qué se usa?**
- **Simplifica la interfaz compleja** de generar reportes que involucra múltiples patrones
- Proporciona un **punto de entrada único** para los controladores
- **Coordina la interacción** entre Singleton, Prototype, Builder, Composite y Decorator
- Oculta la complejidad del subsistema

**¿Cómo se implementa?**
```java
@Component
public class ReporteFinancieroFacade {
    
    private final ReporteService reporteService;
    private final ConfiguracionGlobal config;
    
    @Autowired
    public ReporteFinancieroFacade(ReporteService reporteService) {
        this.reporteService = reporteService;
        this.config = ConfiguracionGlobal.getInstance(); // SINGLETON
    }
    
    /**
     * Método principal que coordina todos los patrones
     */
    public Reporte generarReporteCompleto(TipoReporte tipoReporte, 
                                         boolean conMarcaAgua, 
                                         boolean conFirma) {
        
        // PASO 1: SINGLETON - Configuración global
        ConfiguracionGlobal config = ConfiguracionGlobal.getInstance();
        
        // PASO 2: PROTOTYPE - Clonar plantilla
        PlantillaReporte plantilla = clonarPlantilla(tipoReporte);
        
        // PASO 3: Obtener datos
        Map<String, Object> estadisticas = reporteService.calcularEstadisticas(
            plantilla.getFechaInicio(), 
            plantilla.getFechaFin()
        );
        
        // PASO 4: BUILDER - Construir reporte
        Reporte reporte = ReporteBuilder.desdePlantilla(plantilla)
            .conEncabezado(config.getEncabezadoReportes())
            .conSeccion(crearSeccionResumen(estadisticas))      // COMPOSITE
            .conSeccion(crearSeccionIngresos(estadisticas))     // COMPOSITE
            .conConclusiones(generarConclusiones(estadisticas))
            .construir();
        
        // PASO 5: DECORATOR - Aplicar decoradores
        if (conMarcaAgua) {
            new MarcaAguaDecorator(reporte).aplicar();
        }
        if (conFirma) {
            new FirmaDigitalDecorator(reporte).aplicar();
        }
        
        return reporte;
    }
    
    private ComponenteReporte crearSeccionResumen(Map<String, Object> datos) {
        SeccionReporte seccion = new SeccionReporte("Resumen Ejecutivo");
        seccion.agregar(new ElementoReporte("KPIs", renderizarKPIs(datos)));
        return seccion;
    }
}
```

**Uso desde el controlador:**
```java
@Controller
public class ReporteController {
    
    @Autowired
    private ReporteFinancieroFacade facade; // ¡Solo una dependencia!
    
    @GetMapping("/reportes/generar")
    public String generarReporte(@RequestParam TipoReporte tipo,
                                @RequestParam boolean marcaAgua,
                                @RequestParam boolean firma) {
        // Interfaz simple - la complejidad está oculta
        Reporte reporte = facade.generarReporteCompleto(tipo, marcaAgua, firma);
        return "reportes/visualizacion";
    }
}
```

**Beneficios:**
- ✅ Interfaz simplificada para clientes (controladores)
- ✅ Reducción de acoplamiento con subsistemas complejos
- ✅ Coordinación centralizada de múltiples patrones
- ✅ Fácil de probar y mantener

---

## 🎯 Principios SOLID Aplicados

### **S - Single Responsibility Principle (Principio de Responsabilidad Única)**

**Definición:** *Cada clase debe tener una única razón para cambiar.*

**Aplicación en SERF:**

| Clase | Responsabilidad Única |
|-------|----------------------|
| `ConfiguracionGlobal` | Solo gestiona configuración global |
| `ReporteBuilder` | Solo construye reportes |
| `ComponenteReporte` | Solo define estructura de componentes |
| `ReporteDecorator` | Solo decora reportes |
| `ReporteFinancieroFacade` | Solo coordina generación de reportes |
| `ProductoService` | Solo maneja lógica de negocio de productos |
| `VentaRepository` | Solo accede a datos de ventas |

**Ejemplo:** Si necesitamos cambiar cómo se almacenan las ventas, solo modificamos `VentaRepository`. Si cambia el formato de reportes, solo modificamos `ReporteBuilder`.

---

### **O - Open/Closed Principle (Principio Abierto/Cerrado)**

**Definición:** *Las entidades de software deben estar abiertas para extensión pero cerradas para modificación.*

**Aplicación en SERF:**

#### **Extensión sin modificación:**

1. **Nuevos tipos de reportes** - Sin modificar `PlantillaReporte`:
```java
public class ReporteSemestral extends PlantillaReporte {
    public ReporteSemestral() {
        super(TipoReporte.SEMESTRAL);
        calcularPeriodoSemestral();
    }
    // Nuevo tipo agregado sin modificar la clase base
}
```

2. **Nuevos decoradores** - Sin modificar `ReporteDecorator`:
```java
public class EncriptacionDecorator extends ReporteDecorator {
    @Override
    public void aplicar() {
        reporteBase.setEncriptado(true);
    }
    // Nueva funcionalidad sin tocar código existente
}
```

3. **Nuevos componentes Composite** - Sin modificar `ComponenteReporte`:
```java
public class TablaReporte implements ComponenteReporte {
    @Override
    public String renderizar() {
        return "<table>...</table>";
    }
    // Nuevo componente sin cambiar la interfaz
}
```

**Beneficios:**
- ✅ Extensible sin romper código existente
- ✅ Reduce riesgo de introducir bugs
- ✅ Facilita agregar características

---

### **L - Liskov Substitution Principle (Principio de Sustitución de Liskov)**

**Definición:** *Los objetos de una clase derivada deben poder sustituir a objetos de la clase base sin alterar el comportamiento del programa.*

**Aplicación en SERF:**

#### **Sustitución de plantillas:**
```java
// Cualquier PlantillaReporte puede ser usada
PlantillaReporte plantilla;

plantilla = new ReporteMensual();    // ✅ Funciona
plantilla = new ReporteTrimestral(); // ✅ Funciona
plantilla = new ReporteAnual();      // ✅ Funciona

// El código cliente no cambia
PlantillaReporte clonada = plantilla.clone();
```

#### **Sustitución de componentes:**
```java
ComponenteReporte componente;

componente = new ElementoReporte("Título", "Contenido");  // ✅ Funciona
componente = new SeccionReporte("Sección Principal");     // ✅ Funciona

// Ambos se pueden renderizar de la misma forma
String html = componente.renderizar();
```

**Garantías:**
- ✅ Precondiciones no pueden ser fortalecidas
- ✅ Poscondiciones no pueden ser debilitadas
- ✅ Invariantes se mantienen
- ✅ Comportamiento consistente en jerarquía

---

### **I - Interface Segregation Principle (Principio de Segregación de Interfaces)**

**Definición:** *Los clientes no deben ser forzados a depender de interfaces que no usan.*

**Aplicación en SERF:**

#### **Interfaces específicas y enfocadas:**

```java
// ✅ Interface pequeña y específica
public interface ComponenteReporte {
    String renderizar();
    void agregar(ComponenteReporte componente);
    void eliminar(ComponenteReporte componente);
    String getTitulo();
    boolean esCompuesto();
}

// ❌ Evitamos interfaces "gordas" como:
// public interface ReporteCompleto {
//     String renderizar();
//     void agregar(...);
//     void eliminar(...);
//     void aplicarMarcaAgua(...);
//     void aplicarFirma(...);
//     void encriptar(...);
//     void comprimir(...);
//     void exportarPDF(...);
//     void exportarExcel(...);
//     // ... 20 métodos más
// }
```

**Separación de responsabilidades:**

```java
// En lugar de una interfaz grande, usamos varias específicas:
public interface Renderizable {
    String renderizar();
}

public interface Compuesto {
    void agregar(Componente c);
    void eliminar(Componente c);
}

public interface Decorable {
    void aplicarDecoracion(Decorador d);
}
```

**Beneficios:**
- ✅ Clases implementan solo lo que necesitan
- ✅ Cambios en una interface no afectan clases no relacionadas
- ✅ Código más cohesivo

---

### **D - Dependency Inversion Principle (Principio de Inversión de Dependencias)**

**Definición:** *Los módulos de alto nivel no deben depender de módulos de bajo nivel. Ambos deben depender de abstracciones.*

**Aplicación en SERF:**

#### **Inyección de dependencias en Facade:**
```java
@Component
public class ReporteFinancieroFacade {
    
    private final ReporteService reporteService; // Abstracción, no implementación
    
    @Autowired
    public ReporteFinancieroFacade(ReporteService reporteService) {
        this.reporteService = reporteService; // Dependencia inyectada
    }
}
```

#### **Controladores dependen de abstracciones:**
```java
@Controller
public class ReporteController {
    
    // ✅ Depende de la interfaz (abstracción)
    private final ReporteFinancieroFacade facade;
    
    @Autowired
    public ReporteController(ReporteFinancieroFacade facade) {
        this.facade = facade;
    }
    
    // ❌ NO depende directamente de implementaciones concretas como:
    // private ReporteBuilder builder;
    // private ConfiguracionGlobal config;
    // private ReporteMensual plantillaMensual;
}
```

#### **Builder usa abstracciones:**
```java
public class ReporteBuilder {
    
    // ✅ Trabaja con interfaces
    public ReporteBuilder conSeccion(ComponenteReporte seccion) {
        // Acepta cualquier implementación de ComponenteReporte
        this.reporte.agregarSeccion(seccion);
        return this;
    }
    
    // ❌ NO específico: conSeccion(SeccionReporte seccion)
}
```

**Diagrama de dependencias:**
```
Controlador (alto nivel)
    ↓ (depende de abstracción)
ReporteFinancieroFacade (abstracción)
    ↓ (depende de abstracción)
ReporteService (abstracción)
    ↓ (implementa)
ReporteServiceImpl (bajo nivel)
```

**Beneficios:**
- ✅ Fácil cambiar implementaciones
- ✅ Testeable con mocks
- ✅ Reducción de acoplamiento
- ✅ Mayor flexibilidad

---

## 🔗 Diagrama de Integración

```
┌─────────────────────────────────────────────────────────┐
│              ReporteController                          │
│         (Depende solo de Facade)                        │
└──────────────────────┬──────────────────────────────────┘
                       │
                       ↓
┌─────────────────────────────────────────────────────────┐
│        ReporteFinancieroFacade (FACADE)                 │
│   Coordina todos los patrones de diseño                 │
└──┬──────┬──────┬──────┬──────┬────────────────────────┘
   │      │      │      │      │
   ↓      ↓      ↓      ↓      ↓
┌──────┐ ┌────┐ ┌────┐ ┌────┐ ┌────────┐
│Config│ │Prot│ │Buil│ │Comp│ │Decorat │
│Global│ │otype│ │der │ │osite│ │or      │
│(SING)│ │     │ │    │ │     │ │        │
└──────┘ └────┘ └────┘ └────┘ └────────┘
```

**Flujo de generación de reporte:**

1. **Controlador** llama a `facade.generarReporteCompleto()`
2. **Facade** obtiene instancia de `ConfiguracionGlobal` (Singleton)
3. **Facade** clona `PlantillaReporte` según tipo (Prototype)
4. **Facade** usa `ReporteBuilder` para construir reporte (Builder)
5. **Builder** agrega `SeccionReporte` y `ElementoReporte` (Composite)
6. **Facade** aplica `MarcaAguaDecorator` y `FirmaDigitalDecorator` (Decorator)
7. **Reporte** completo retornado al controlador

---

## 🎁 Beneficios de la Arquitectura

### **Mantenibilidad** 🔧
- Cada clase tiene una responsabilidad clara (SRP)
- Fácil localizar y corregir bugs
- Código autodocumentado

### **Extensibilidad** 🚀
- Nuevo tipo de reporte → Nueva subclase de `PlantillaReporte`
- Nueva decoración → Nueva subclase de `ReporteDecorator`
- Nuevo componente → Nueva implementación de `ComponenteReporte`
- Sin modificar código existente (OCP)

### **Testabilidad** ✅
- Dependencias inyectadas (DIP)
- Fácil crear mocks de servicios
- Cada patrón testeable independientemente

### **Reusabilidad** ♻️
- Decoradores combinables
- Componentes Composite reutilizables
- Builder reutilizable para diferentes tipos

### **Escalabilidad** 📈
- Arquitectura preparada para crecer
- Singleton thread-safe para alta concurrencia
- Facade simplifica integración de nuevos patrones

### **Legibilidad** 📖
- API fluida del Builder
- Código expresivo y declarativo
- Nombres significativos

---

## 📚 Referencias

- **Gang of Four**: Design Patterns: Elements of Reusable Object-Oriented Software
- **Robert C. Martin**: Clean Architecture / SOLID Principles
- **Spring Framework**: Dependency Injection y Enterprise Patterns
- **Joshua Bloch**: Effective Java (Builder Pattern)

---

## 👥 Autor

**FinanCorp S.A. - Equipo de Arquitectura de Software**  
Sistema SERF v1.0.0  
*Migrado a Java 21 LTS - Marzo 2026*

---

## 📝 Notas Finales

Este documento demuestra cómo **6 patrones de diseño trabajan en armonía** con los **5 principios SOLID** para crear una arquitectura empresarial robusta. Cada patrón tiene un propósito específico y se integra naturalmente con los demás, evitando over-engineering mientras mantiene la flexibilidad necesaria para evolucionar el sistema.

**La clave está en usar patrones cuando resuelven problemas reales, no por usarlos.**
