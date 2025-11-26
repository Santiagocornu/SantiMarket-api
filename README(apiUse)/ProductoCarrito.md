APIMARKET — ProductoCarrito
La APIMARKET de ProductoCarrito gestiona el ciclo completo de adición, actualización, eliminación y consulta de productos dentro de carritos de compras.
Incluye validaciones profesionales como control de stock y cantidad, manejo consistente de errores.

Endpoints disponibles
Obtener todos los productos en carritos
GET /apiMarket/productoCarrito
Retorna una lista completa de productos en carritos registrados.

Obtener un producto en carrito por ID
GET /apiMarket/productoCarrito/{id}
Devuelve la información del producto en carrito.
Si el ID no existe, el backend envía un error con mensaje descriptivo.

Obtener productos en carrito por ID de carrito
GET /apiMarket/productoCarrito/carrito/{carritoId}
Devuelve la lista de productos en un carrito específico.
Si no hay productos, el backend envía un error con mensaje descriptivo.

Crear producto en carrito
POST /apiMarket/productoCarrito
Crea un nuevo producto en carrito o actualiza la cantidad si ya existe.
Validaciones incluidas:

Cantidad obligatoria y mayor a 0
Producto obligatorio
Carrito obligatorio
Stock suficiente disponible
Errores posibles:

400 cantidad inválida, producto/carrito faltante o stock insuficiente
Actualizar producto en carrito
PUT /apiMarket/productoCarrito/{id}
Actualiza datos de un producto en carrito existente.
Permite modificar:

Cantidad (con validación de stock)
Producto
Carrito
Recalcula el total del carrito automáticamente.

Errores posibles:

Producto en carrito inexistente
Cantidad inválida o stock insuficiente
Eliminar producto en carrito
DELETE /apiMarket/productoCarrito/{id}
Elimina el producto en carrito.
Recalcula el total del carrito restando el subtotal.
Retorna 204 No Content en caso de éxito.

Vaciar carrito
DELETE /apiMarket/productoCarrito/carrito/{carritoId}/vaciar
Elimina todos los productos de un carrito y resetea el total a 0.
Retorna 204 No Content en caso de éxito.

Errores posibles:

Carrito sin productos
Manejo de errores
Todos los endpoints retornan errores de forma consistente, en uno de estos formatos:

JSON con detalle
json

Copy code
{ "error": "ProductoCarrito no encontrado con ID: 5" }
o

json

Copy code
{ "message": "Stock insuficiente. Stock disponible: 10, solicitado: 15" }
Texto plano (Spring o RuntimeException)

Copy code
ProductoCarrito no encontrado con ID: 5


