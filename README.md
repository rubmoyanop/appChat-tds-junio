# AppChat - Proyecto Final TDS

[![Java](https://img.shields.io/badge/Java-8+-orange.svg)](https://www.oracle.com/java/)
[![Maven](https://img.shields.io/badge/Maven-3.6+-blue.svg)](https://maven.apache.org/)
[![Completado](https://img.shields.io/badge/Estado-Completado-green.svg)](https://github.com)

**AppChat** es una aplicación de mensajería instantánea desarrollada en Java como proyecto final para la asignatura de Tecnologías de Desarrollo de Software (TDS) de 4º PCEO en la Universidad de Murcia.

## 📱 Características Principales

- ✅ **Mensajería instantánea** con texto y emojis
- ✅ **Gestión de contactos** individuales
- ✅ **Grupos de difusión** para envío masivo
- ✅ **Búsqueda avanzada** de mensajes
- ✅ **Funciones premium** con exportación a PDF
- ✅ **Interfaz gráfica intuitiva** con Swing
- ✅ **Persistencia** con base de datos H2

## 🚀 Inicio Rápido

### Requisitos del Sistema

- **Java 8** o superior
- **Maven 3.6+** (para compilación)
- Sistema operativo: Windows, macOS o Linux

### Instalación

```bash
# Clonar el repositorio
git clone [URL_DEL_REPOSITORIO]
cd appChat-tds-junio

# Compilar el proyecto
mvn clean compile

# Ejecutar la aplicación
mvn exec:java -Dexec.mainClass="umu.tds.appchat.App"
```

### Primer Uso

1. **Ejecuta la aplicación** - Se abrirá la ventana de bienvenida
2. **Regístrate** - Crea una nueva cuenta con tu número de teléfono
3. **Inicia sesión** - Accede con tus credenciales
4. **Agrega contactos** - Comienza a chatear

## 📚 Funcionalidades

### Funciones Básicas (Gratuitas)
- Registro e inicio de sesión
- Envío de mensajes de texto y emojis
- Gestión de contactos individuales
- Creación y modificación de grupos
- Búsqueda de mensajes

### Funciones Premium
- ⭐ Exportación de chats a PDF
- ⭐ Insignia premium visible
- ⭐ Descuentos automáticos aplicados

### Grupos de Difusión
Los grupos en AppChat funcionan como **grupos de difusión**:
- Los mensajes se envían individualmente a cada miembro
- No hay historial grupal compartido
- Cada usuario ve los mensajes en sus chats individuales

## 🏗️ Arquitectura

La aplicación sigue el patrón **MVC (Modelo-Vista-Controlador)** con una arquitectura en capas:

```
src/main/java/umu/tds/appchat/
├── controlador/     # Lógica de negocio (AppChat - Singleton)
├── modelo/          # Entidades del dominio
├── vista/           # Interfaz gráfica (Swing)
├── dao/             # Interfaces de acceso a datos
└── persistencia/    # Implementación de persistencia (TDS)
```

### Patrones de Diseño Implementados

- **Singleton**: `AppChat`, `GestorVentanas`, Pools de objetos
- **Factory**: `FactoriaDAO`, `FactoriaDescuentos`
- **Strategy**: Sistema de descuentos premium
- **Observer**: Comunicación entre componentes de UI
- **DAO**: Capa de acceso a datos

## 🛠️ Tecnologías Utilizadas

| Tecnología | Propósito |
|------------|-----------|
| **Java 8+** | Lenguaje principal |
| **Swing** | Interfaz gráfica |
| **Maven** | Gestión de dependencias |
| **H2 Database** | Base de datos embebida |
| **TDS Driver** | Sistema de persistencia |
| **iTextPDF** | Generación de documentos PDF |
| **JCalendar** | Selector de fechas |

## 📖 Manual de Usuario

### Registro e Inicio de Sesión
1. **Registro**: Completa el formulario con nombre, teléfono, contraseña y fecha de nacimiento
2. **Login**: Usa tu número de teléfono y contraseña

### Envío de Mensajes
- Selecciona un contacto de la lista
- Escribe tu mensaje o selecciona un emoji
- Presiona Enter o clic en "Enviar"

### Gestión de Contactos
- **Agregar**: Botón "Agregar nuevo contacto" → nombre y teléfono
- **Grupos**: Botón "Crear nuevo grupo" → seleccionar miembros

### Búsqueda
- Usa "Buscar Mensajes" para filtrar por texto, contacto o tipo de mensaje

### Premium
- Clic en "Premium" → pagar suscripción → acceso a exportación PDF

## 👨‍💻 Desarrollo

### Estructura del Proyecto Maven

```xml
<groupId>umu.tds</groupId>
<artifactId>appchat</artifactId>
<version>1.0.0</version>
<packaging>jar</packaging>
```

### Compilación y Ejecución

```bash
# Compilar
mvn clean compile

# Ejecutar tests
mvn test

# Crear JAR ejecutable
mvn package

# Ejecutar JAR
java -jar target/appchat-1.0.0.jar
```

### Dependencias Principales

- `tds.driver.FactoriaServicioPersistencia` - Persistencia TDS proporcionada por los profesores
- `com.itextpdf:itextpdf` - Generación PDF
- `com.toedter:jcalendar` - Selector fechas

## 📄 Documentación

Para información detallada sobre arquitectura, patrones de diseño, diagramas UML y manual completo, consulta:
- [`docs/documentacion.md`](docs/documentacion.md) - Documentación técnica completa

## 👥 Autores

**Proyecto desarrollado por:**
- **Rubén Moyano Palazón**
- **Alejandro Cerezal Jiménez**

**Universidad de Murcia** - 4º PCEO  
**Asignatura:** Tecnologías de Desarrollo de Software (TDS)  
**Periodo:** 28 de abril - 22 de junio de 2025 (55 días)