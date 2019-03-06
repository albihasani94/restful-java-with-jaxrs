package com.cert.jaxrs.service;

import com.cert.jaxrs.model.Customer;
import com.cert.jaxrs.service.api.CustomerResource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Path("/customers")
public class CustomerResourceService implements CustomerResource {

    private Map<Integer, Customer> customerDB = new ConcurrentHashMap<>();
    private AtomicInteger idCounter = new AtomicInteger();

    public Response createCustomer(InputStream inputStream) {
        Customer customer = readCustomer(inputStream);
        customer.setId(idCounter.incrementAndGet());
        customerDB.put(customer.getId(), customer);
        System.out.println("Created customer: " + customer.getId());
        return Response.created(URI.create("/customers/" + customer.getId())).build();
    }

    public StreamingOutput getCustomer(int id) {
        final Customer customer = customerDB.get(id);
        if (customer == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return new StreamingOutput() {
            @Override
            public void write(OutputStream outputStream) throws IOException, WebApplicationException {
                outputCustomer(outputStream, customer);
            }
        };
    }

    public void updateCustomer(int id, InputStream inputStream) {
        Customer updatedCustomer = readCustomer(inputStream);
        Customer currentCustomer = customerDB.get(id);
        if (currentCustomer == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        currentCustomer.setFirstName(updatedCustomer.getFirstName());
        currentCustomer.setLastName(updatedCustomer.getLastName());
        currentCustomer.setStreet(updatedCustomer.getStreet());
        currentCustomer.setState(updatedCustomer.getState());
        currentCustomer.setZip(updatedCustomer.getZip());
        currentCustomer.setCountry(updatedCustomer.getCountry());
    }

    private Customer readCustomer(InputStream inputStream) {
        try {
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = documentBuilder.parse(inputStream);
            Element root = document.getDocumentElement();

            Customer customer = new Customer();
            if (root.getAttribute("id") != null && !root.getAttribute("id").trim().equals("")) {
                customer.setId(Integer.valueOf(root.getAttribute("id")));
            }

            NodeList nodes = root.getChildNodes();
            for (int i = 0; i < nodes.getLength(); i++) {
                Element element = (Element) nodes.item(i);
                if (element.getTagName().equals("first-name")) {
                    customer.setFirstName(element.getTextContent());
                } else if (element.getTagName().equals("last-name")) {
                    customer.setLastName(element.getTextContent());
                } else if (element.getTagName().equals("street")) {
                    customer.setStreet(element.getTextContent());
                } else if (element.getTagName().equals("city")) {
                    customer.setCity(element.getTextContent());
                } else if (element.getTagName().equals("state")) {
                    customer.setState(element.getTextContent());
                } else if (element.getTagName().equals("zip")) {
                    customer.setZip(element.getTextContent());
                } else if (element.getTagName().equals("country")) {
                    customer.setCountry(element.getTextContent());
                }
            }

            return customer;

        } catch (Exception e) {
            throw new WebApplicationException(e, Response.Status.BAD_REQUEST);
        }
    }

    private void outputCustomer(OutputStream outputStream, Customer customer) {
        PrintStream writer = new PrintStream(outputStream);
        writer.println("<customer id=\"" + customer.getId() + "\">");
        writer.println("<customer id=\"" + customer.getId() + "\">");
        writer.println(" <first-name>" + customer.getFirstName() + "</first-name>");
        writer.println(" <last-name>" + customer.getLastName() + "</last-name>");
        writer.println(" <street>" + customer.getStreet() + "</street>");
        writer.println(" <city>" + customer.getCity() + "</city>");
        writer.println(" <state>" + customer.getState() + "</state>");
        writer.println(" <zip>" + customer.getZip() + "</zip>");
        writer.println(" <country>" + customer.getCountry() + "</country>");
        writer.println("</customer>");
    }
}
