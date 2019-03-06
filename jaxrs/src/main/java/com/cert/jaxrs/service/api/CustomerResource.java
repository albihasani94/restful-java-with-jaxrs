package com.cert.jaxrs.service.api;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import java.io.InputStream;

@Path("/customers")
public interface CustomerResource {
    @POST
    @Consumes(MediaType.APPLICATION_XML)
    Response createCustomer(InputStream inputStream);

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_XML)
    StreamingOutput getCustomer(@PathParam("id") int id);

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_XML)
    void updateCustomer(@PathParam("id") int id, InputStream inputStream);
}
