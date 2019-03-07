package com.cert.jaxrs.marshaller;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

@Provider
@Produces("application/xml")
public class JAXBMarshaller implements MessageBodyWriter {
    @Override
    public boolean isWriteable(Class type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return type.isAnnotationPresent(XmlRootElement.class);
    }

    @Override
    public long getSize(Object target, Class type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return -1;
    }

    @Override
    public void writeTo(Object target, Class type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException {
        try {
            JAXBContext ctx = JAXBContext.newInstance(type);
            ctx.createMarshaller().marshal(target, entityStream);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }
}
