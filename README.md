# API Gateway con NGINX - Microservicios gRPC

El cliente manda peticiones HTTP al gateway en el puerto 8080 y NGINX las manda a los microservicios en HTTP/2 usando grpc_pass. Ordenes le pregunta a Productos por gRPC si hay stock antes de crear la orden.

- `/api/productos` -> MSProductos (8081)
- `/api/ordenes` -> MSOrdenes (8082)

## Como correrlo

1. Ejecutar `script_bd.sql` en MySQL. Si la contraseña no es `root` hay que cambiarla en los `application.properties`.
2. Clean and Build y Run a MSProductos y despues a MSOrdenes.
3. Copiar `gateway-nginx/conf/nginx.conf` a la carpeta `conf` de NGINX y correr `start nginx` (para detenerlo `nginx -s stop`).

## Pruebas

```
curl -X POST http://localhost:8080/api/productos -H "Content-Type: application/json" -d "{\"nombre\":\"Teclado\",\"precio\":850,\"stock\":10}"
curl http://localhost:8080/api/productos
curl http://localhost:8080/api/productos/1
curl -X POST http://localhost:8080/api/ordenes -H "Content-Type: application/json" -d "{\"productoId\":1,\"cantidad\":3}"
curl http://localhost:8080/api/ordenes
```

En la consola de cada microservicio se ve que las peticiones llegan por HTTP_2.
