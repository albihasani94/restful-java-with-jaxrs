package com.cert.jaxrs;

import com.cert.jaxrs.service.CustomerResourceService;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/")
public class JAXRSConfiguration extends Application {

    private Set<Object> singletons = new HashSet<>();

    public JAXRSConfiguration() {
        singletons.add(new CustomerResourceService());
    }

    @Override
    public Set<Object> getSingletons() {
        return singletons;
    }

}
