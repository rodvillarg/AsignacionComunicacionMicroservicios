/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itson.msordenes.filtros;

import io.vertx.core.http.HttpServerRequest;
import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.server.ServerRequestFilter;

/**
 *
 * @author Jesús Rodrigo Villegas Argüelles - 261186
 */
public class RegistroPeticiones {

    private static final Logger LOG = Logger.getLogger(RegistroPeticiones.class);

    @ServerRequestFilter
    public void registrar(HttpServerRequest request) {
        LOG.info("[MSOrdenes] " + request.method() + " " + request.uri()
                + " llego por " + request.version());
    }
}
