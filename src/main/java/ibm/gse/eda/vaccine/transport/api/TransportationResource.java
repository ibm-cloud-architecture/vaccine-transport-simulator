package ibm.gse.eda.vaccine.transport.api;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.annotations.jaxrs.PathParam;

import ibm.gse.eda.vaccine.transport.domain.TransportDefinition;
import ibm.gse.eda.vaccine.transport.domain.TransportationService;

@Path("/transportation")
public class TransportationResource {

    @Inject
    TransportationService service;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/greeting/{name}")
    public String greeting(@PathParam String name) {
        return service.greeting(name);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<TransportDefinition> getCurrentTransportDefinitions() {
        return service.getAllTransportDefinitions();
    }

    @POST
    @Path("/start")
    public void startSendingTransportation(){
        service.sendCurrentTransportDefinitions();
    }
}