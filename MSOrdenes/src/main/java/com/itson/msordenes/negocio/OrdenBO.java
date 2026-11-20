/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.msordenes.negocio;

import com.itson.grpc.ProductoRequest;
import com.itson.grpc.ProductoResponse;
import com.itson.grpc.ProductoServiceGrpc;
import com.itson.grpc.StockRequest;
import com.itson.msordenes.entidades.Orden;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.quarkus.grpc.GrpcClient;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author Jesús Rodrigo Villegas Argüelles - 261186
 */
@ApplicationScoped
public class OrdenBO {

    @GrpcClient("producto-service")
    ProductoServiceGrpc.ProductoServiceBlockingStub clienteProductos;

    @Transactional
    public List<Orden> consultarTodas() {
        return Orden.listAll();
    }

    @Transactional
    public Orden consultarPorId(Long id) {
        return Orden.findById(id);
    }

    @Transactional
    public Orden crear(Long productoId, int cantidad) {
        if (productoId == null || productoId <= 0) {
            throw new NegocioException("El id del producto no es valido");
        }
        if (cantidad <= 0) {
            throw new NegocioException("La cantidad debe ser mayor a 0");
        }

        ProductoResponse producto;
        try {
            ProductoRequest request = ProductoRequest.newBuilder()
                    .setId(productoId)
                    .build();
            producto = clienteProductos.verificarYObtener(request);

            if (producto.getStockDisponible() < cantidad) {
                throw new NegocioException("Sin stock suficiente de " + producto.getNombre()
                        + ", disponibles: " + producto.getStockDisponible());
            }

            StockRequest stockRequest = StockRequest.newBuilder()
                    .setId(productoId)
                    .setCantidad(cantidad)
                    .build();
            producto = clienteProductos.descontarStock(stockRequest);
        } catch (StatusRuntimeException e) {
            if (e.getStatus().getCode() == Status.Code.UNAVAILABLE) {
                throw new NegocioException("El microservicio de productos no responde");
            }
            throw new NegocioException(e.getStatus().getDescription());
        }

        Orden orden = new Orden();
        orden.productoId = productoId;
        orden.nombreProducto = producto.getNombre();
        orden.cantidad = cantidad;
        orden.precioUnitario = producto.getPrecio();
        orden.total = producto.getPrecio() * cantidad;
        orden.fecha = LocalDateTime.now();
        orden.persist();
        return orden;
    }
}
