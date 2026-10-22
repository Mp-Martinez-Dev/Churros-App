#  ChurrosApp - Control Local de Negocio

> Aplicación móvil Android nativa diseñada para la gestión operativa y financiera en tiempo real de un negocio de churros en Chile (CLP).

---

##  Descripción General

**ChurrosApp** es una solución móvil integral orientada al control interno de inventario, recetas y finanzas diarias. La interfaz integra componentes modernos de Material 3 y animaciones interactivas como una **alcancía diaria reactiva**, diseñada para motivar el cumplimiento de metas de venta mediante retroalimentación visual inmediata.

---

##  Características Principales

-  **Alcancía Diaria e Indicador de Metas:**
    - Seguimiento en tiempo real de la misión de ventas diarias.
    - Animación interactiva de caída de moneda dorada sobre alcancía al registrar ventas.
    - Píldora de estado con **cambios de color dinámicos** (*Rojo ➔ Naranja ➔ Azul ➔ Verde*) según el porcentaje de avance hacia la meta en CLP.

-  **Control de Bodega e Insumos:**
    - Gestión completa (CRUD) de stock de materia prima (Harina, Aceite, Azúcar, Manjar, etc.).
    - Actualización y descuento automático de insumos al preparar nuevas tandas.

-  **Historial Financiero y Balances:**
    - Registro centralizado de transacciones (ingresos por ventas y egresos por compras o gastos operativos).
    - Cálculo automático del balance neto en CLP y resumen visual diario.

-  **Calculadora de Recetas y Tandas:**
    - Configuración flexible de ingredientes requeridos por tanda de producción (40 churros).
    - Verificación inteligente de disponibilidad de stock en bodega antes de iniciar la preparación.

---

##  Stack Tecnológico

- **Lenguaje:** [Kotlin](https://kotlinlang.org/)
- **UI Framework:** [Jetpack Compose](https://developer.android.com/jetpack/compose) (Material 3)
- **Persistencia de Datos:** [Room Database](https://developer.android.com/training/data-storage/room) + SQLite
- **Procesamiento de Anotaciones:** KSP (*Kotlin Symbol Processing*)
- **Arquitectura:** MVVM (*Model-View-ViewModel*) + `StateFlow` & Coroutines
- **Animaciones UI:** `Canvas`, `Animatable`, `animateColorAsState` & Jetpack Compose Animations

---

##  Arquitectura del Proyecto

El proyecto está estructurado bajo la arquitectura recomendada por Google para aplicaciones modernas en Android:

```text
com.example.churros_app/
├── data/           # Entidades Room (Producto, Insumo, MisionDiaria, Movimiento, RecetaItem), DAOs y AppDatabase
├── ui/             # DashboardScreen, componentes composables, animaciones y DashboardViewModel
└── MainActivity.kt # Punto de entrada de la aplicación

```
##  Instalación y Ejecución Local
1. **Clonar el repositorio:**
   ```text 
   git clone [https://github.com/Mp-Martinez-Dev/Churros-App.git](https://github.com/Mp-Martinez-Dev/Churros-App.git)
    ```
   2. **Abrir en Android Studio:**

       a. Abre Android Studio (se recomienda versión Hedgehog o superior).

       b. Selecciona Open y navega hasta la carpeta clonada.

       c. Sincronizar y Ejecutar:
          Deja que Gradle descargue las dependencias y compile con KSP.

Ejecuta la aplicación en un emulador o dispositivo físico con Android 8.0 (API 26) o superior.

Autor
Desarrollado por Mp-Martinez-Dev

GitHub: @Mp-Martinez-Dev
