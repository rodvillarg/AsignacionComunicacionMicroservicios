/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.msproductos.rpc;

import com.itson.grpc.ProductoRequest;
import com.itson.grpc.ProductoResponse;
import com.itson.grpc.ProductoService;
import com.itson.grpc.StockRequest;
import com.itson.msproductos.entidades.Producto;
import com.itson.msproductos.negocio.NegocioException;
import com.itson.msproductos.negocio.ProductoBO;
import io.grpc.Status;
import io.quarkus.grpc.GrpcService;
import io.smallrye.common.annotation.Blocking;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;

/**
 *
 * @author Jesús Rodrigo Villegas Argüelles - 261186
 */
@GrpcService
public class ProductoGrpcServiceImpl implements ProductoService {

    @Inject
    ProductoBO productoBO;

    @Override
    @Blocking
    public Uni<ProductoResponse> verificarYObtener(ProductoRequest request) {
        Producto producto = productoBO.consultarPorId(request.getId());
        if (producto == null) {
            return Uni.createFrom().failure(Status.NOT_FOUND
                    .withDescription("No existe el producto con id " + request.getId())
                    .asRuntimeException());
        }
        return Uni.createFrom().item(convertir(producto));
    }

    @Override
    @Blocking
    public Uni<ProductoResponse> descontarStock(StockRequest request) {
        try {
            Producto producto = productoBO.descontarStock(request.getId(), request.getCantidad());
            return Uni.createFrom().item(convertir(producto));
        } catch (NegocioException e) {
            return Uni.createFrom().failure(Status.FAILED_PRECONDITION
                    .withDescription(e.getMessage())
                    .asRuntimeException());
        }
    }

    private ProductoResponse convertir(Producto producto) {
        return ProductoResponse.newBuilder()
                .setId(producto.id)
                .setNombre(producto.nombre)
                .setPrecio(producto.precio)
                .setStockDisponible(producto.stock)
                .build();
    }
}
