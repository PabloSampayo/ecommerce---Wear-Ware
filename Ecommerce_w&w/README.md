# EcommerceApp - Sistema de Comercio Electrónico Wear & Ware

## Descripción
Sistema de e-commerce desarrollado en Java con Swing y persistencia en archivos. Permite gestión de productos, carrito de compras, procesamiento de pedidos y roles de usuario (Cliente/Administrador).

## Integrantes
- Pablo Cesar Sampayo Arnedo
- Jesus David Sampayo Arnedo    
- Anthony Samuel Giraldo Gutierrez

## Tecnologías Utilizadas
- Java 17
- Swing (Interfaz gráfica)
- Persistencia con archivos .dat (Object serialization)
- NetBeans IDE

## Instrucciones de Ejecución

### Requisitos previos
- Java JDK 11 o superior instalado
- NetBeans IDE (opcional, puede ejecutarse desde terminal)

### Desde NetBeans
1. Clonar
git clone (https://github.com/PabloSampayo/ecommerce---Wear-Ware)
2. Abrir el proyecto en NetBeans
3. Ejecutar la clase `ui.LoginUI.java`

Categoría	Tecnología elegida
Lenguaje	Java
UI / Framework	Swing
Persistencia	Java Object Serialization (.dat)
IDE             Netbeans


Requisito	Estado	Dónde está implementado
Herencia	✅	Usuario → Cliente/Admin, Producto → Electrónico/Ropa
Encapsulación	✅	Todos los atributos son private con getters/setters
Polimorfismo	✅	getRol() y getTipo() sobrescritos
Abstracción	✅	Clases abstractas Usuario y Producto
Colecciones	✅	ArrayList en todos los DAOs y servicios
Manejo de excepciones	✅	try/catch en toda la UI
Persistencia	✅	Archivos .dat en carpeta data/
Gestión productos	✅	CRUD completo (Admin)
Gestión usuarios	✅	Registro y login
Carrito de compras	✅	Agregar/eliminar/modificar cantidades
Flujo de pedido	✅	Confirmar carrito + método pago
Historial de pedidos	✅	Cliente puede ver sus pedidos
Interfaz gráfica	✅	Swing con tabs y tablas

-------------------------------------------------------------------------------------------------------------------------------------------
Concepto	Clase / Método donde se aplica	Explicación
Herencia	Usuario → Cliente, Administrador
Producto → ProductoElectronico, ProductoRopa	Las clases hijas heredan atributos (id, nombre, email, password) de las clases padres
Encapsulación	TODAS las clases del paquete model
Ej: Usuario.java, Producto.java, Carrito.java	Todos los atributos son private y se accede mediante getters y setters
Polimorfismo	getRol() en Cliente y Administrador
getTipo() en ProductoElectronico y ProductoRopa	Mismo método pero con comportamiento diferente según la clase que lo ejecuta
Abstracción	Usuario (clase abstracta)
Producto (clase abstracta)	Clases que no se pueden instanciar directamente, solo sirven como plantilla para sus hijas
Colecciones	ArrayList<ItemCarrito> en Carrito.java
ArrayList<Pedido> en Cliente.java
ArrayList<Producto> en ProductoDAO	Uso de listas para manejar conjuntos de objetos
Excepciones	try/catch en LoginUI.java
try/catch en ClienteUI.java
throws Exception en Carrito.agregarProducto()	Manejo de errores como stock insuficiente, credenciales inválidas, archivos no encontrados







