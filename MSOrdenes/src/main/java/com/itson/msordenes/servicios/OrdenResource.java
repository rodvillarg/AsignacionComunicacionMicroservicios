/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.msordenes.servicios;

import com.itson.msordenes.dtos.MensajeDTO;
import com.itson.msordenes.dtos.NuevaOrdenDTO;
import com.itson.msordenes.entidades.Orden;
import com.itson.msordenes.negocio.NegocioException;
import com.itson.msordenes.negocio.OrdenBO;
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
@Path("/ordenes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrdenResource {

    @Inject
    OrdenBO ordenBO;

    @GET
    public List<Orden> consultarTodas() {
        return ordenBO.consultarTodas();
    }

    @GET
    @Path("/{id}")
    public Response consultarPorId(@PathParam("id") Long id) {
        Orden orden = ordenBO.consultarPorId(id);
        if (orden == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new MensajeDTO("No existe la orden con id " + id))
                    .build();
        }
        return Response.ok(orden).build();
    }

    @POST
    public Response crearOrden(NuevaOrdenDTO nuevaOrden) {
        if (nuevaOrden == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new MensajeDTO("Faltan los datos de la orden"))
                    .build();
        }
        try {
            Orden orden = ordenBO.crear(nuevaOrden.productoId, nuevaOrden.cantidad);
            return Response.status(Response.Status.CREATED).entity(orden).build();
        } catch (NegocioException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new MensajeDTO(e.getMessage()))
                    .build();
        }
    }
}
