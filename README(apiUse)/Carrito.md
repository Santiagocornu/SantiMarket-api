APIMARKET — Carrito
La APIMARKET de **CarritoCompras** gestiona el ciclo completo de creación, asignación, actualización y eliminación de carritos de compras dentro del sistema.
Incluye validaciones profesionales, manejo consistente de errores.

---

## Endpoints disponibles

### Obtener todos los carritos
**GET** `/apiMarket/carrito`
Retorna una lista completa de carritos registrados.

### Obtener un carrito por ID
**GET** `/apiMarket/carrito/{id}`
Devuelve la información del carrito.
Si el ID no existe, el backend envía un error con mensaje descriptivo.

### Obtener carrito por ID de usuario
**GET** `/apiMarket/carrito/usuario/{usuarioId}`
Devuelve el carrito asociado al usuario.
Si el usuario no tiene carrito, el backend envía un error con mensaje descriptivo.

### Crear carrito
**POST** `/apiMarket/carrito`
Crea un carrito nuevo.
Validaciones incluidas:
- Usuario obligatorio (no puede ser nulo)

Errores posibles:
- `400` usuario faltante

### Actualizar carrito
**PUT** `/apiMarket/carrito/{id}`
Actualiza datos de un carrito existente.
Permite modificar:
- Total

Errores posibles:
- Carrito inexistente

### Asignar usuario a carrito
**PUT** `/apiMarket/carrito/{carritoId}/asignarUsuario/{usuarioId}`
Asigna un usuario a un carrito existente.
Validaciones incluidas:
- Carrito y usuario deben existir

Errores posibles:
- Carrito inexistente
- Usuario inexistente

### Eliminar carrito
**DELETE** `/apiMarket/carrito/{id}`
Elimina el carrito.
Retorna `204 No Content` en caso de éxito.

### Actualizar total del carrito
**PUT** `/apiMarket/carrito/{id}/total/{total}`
Actualiza solo el total de un carrito existente.

Errores posibles:
- Carrito inexistente

---

## Manejo de errores

Todos los endpoints retornan errores de forma **consistente**, en uno de estos formatos:

### JSON con detalle
```json
{ "error": "Carrito no encontrado con ID: 5" }
```
o
```json
{ "message": "El usuario no puede ser nulo." }
```

### Texto plano (Spring o RuntimeException)
```
Carrito no encontrado con ID: 5
```