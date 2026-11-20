/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.msproductos.negocio;

import com.itson.msproductos.entidades.Producto;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.util.List;

/**
 *
 * @author Jesús Rodrigo Villegas Argüelles - 261186
 */
@ApplicationScoped
public class ProductoBO {

    @Transactional
    public List<Producto> consultarTodos() {
        return Producto.listAll();
    }

    @Transactional
    public Producto consultarPorId(Long id) {
        return Producto.findById(id);
    }

    @Transactional
    public Producto agregar(Producto producto) {
        if (producto.nombre == null || producto.nombre.isBlank()) {
            throw new NegocioException("El nombre del producto es obligatorio");
        }
        if (producto.precio <= 0) {
            throw new NegocioException("El precio debe ser mayor a 0");
        }
        if (producto.stock < 0) {
            throw new NegocioException("El stock no puede ser negativo");
        }
        producto.persist();
        return producto;
    }

    @Transactional
    public Producto descontarStock(Long id, int cantidad) {
        Producto producto = Producto.findById(id);
        if (producto == null) {
            throw new NegocioException("No existe el producto con id " + id);
        }
        if (cantidad <= 0) {
            throw new NegocioException("La cantidad debe ser mayor a 0");
        }
        if (producto.stock < cantidad) {
            throw new NegocioException("No hay stock suficiente de " + producto.nombre
                    + ", solo quedan " + producto.stock);
        }
        producto.stock = producto.stock - cantidad;
        return producto;
    }
}
