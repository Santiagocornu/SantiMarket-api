apiMarket - ProductoTicket API
La APIMARKET de ProductoTicket gestiona la relación entre productos y tickets/ventas dentro del sistema. Incluye validaciones profesionales, manejo consistente de errores .

Endpoints disponibles
Crear registro ProductoTicket
POST /apiMarket/producto-ticket Crea un nuevo registro que asocia un producto a una venta.

Obtener todos los registros ProductoTicket
GET /apiMarket/producto-ticket Retorna una lista completa de registros ProductoTicket.

Obtener registro por ID
GET /apiMarket/producto-ticket/{id} Devuelve la información del registro. Si el ID no existe, el backend envía un error con mensaje descriptivo.

Actualizar registro
PUT /apiMarket/producto-ticket/{id} Actualiza datos de un registro existente. Permite modificar:

Cantidad, producto, venta
Errores posibles:

Registro inexistente
Eliminar registro
DELETE /apiMarket/producto-ticket/{id} Elimina el registro. Retorna 204 No Content en caso de éxito.

Obtener productos de una venta
GET /apiMarket/producto-ticket/venta/{ventaId} Devuelve todos los productos asociados a una venta específica.

Manejo de errores
Todos los endpoints retornan errores de forma consistente, en uno de estos formatos:

JSON con detalle
json

Copy code
{ "error": "ProductoTicket no encontrado con ID: 5" }
o

json

Copy code
{ "message": "Datos inválidos." }
Texto plano (Spring o RuntimeException)

Copy code
ProductoTicket no encontrado con ID: 5