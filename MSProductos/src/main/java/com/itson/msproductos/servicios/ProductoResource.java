/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.msproductos.servicios;

import com.itson.msproductos.dtos.MensajeDTO;
import com.itson.msproductos.entidades.Producto;
import com.itson.msproductos.negocio.NegocioException;
import com.itson.msproductos.negocio.ProductoBO;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

/**
 *
 * @author Jesús Rodrigo Villegas Argüelles - 261186
 */
@Path("/productos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductoResource {

    @Inject
    ProductoBO productoBO;

    @GET
    public List<Producto> consultarTodos() {
        return productoBO.consultarTodos();
    }

    @GET
    @Path("/{id}")
    public Response consultarPorId(@PathParam("id") Long id) {
        Producto producto = productoBO.consultarPorId(id);
        if (producto == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new MensajeDTO("No existe el producto con id " + id))
                    .build();
        }
        return Response.ok(producto).build();
    }

    @POST
    public Response agregar(Producto producto) {
        try {
            Producto nuevo = productoBO.agregar(producto);
            return Response.status(Response.Status.CREATED).entity(nuevo).build();
        } catch (NegocioException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new MensajeDTO(e.getMessage()))
                    .build();
        }
    }
}
