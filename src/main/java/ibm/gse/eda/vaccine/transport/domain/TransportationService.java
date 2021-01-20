package ibm.gse.eda.vaccine.transport.domain;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;

import ibm.gse.eda.vaccine.orderoptimizer.Transportation;
import ibm.gse.eda.vaccine.transport.infrastructure.TransportProducer;
import ibm.gse.eda.vaccine.transport.infrastructure.TransportRepository;

/**
 * As a demo the 
 */
@ApplicationScoped
public class TransportationService {
    Logger logger = Logger.getLogger(TransportationService.class.getName());

    @Inject
    TransportRepository repository;
   
    /*
    Looks there is a problem to connect to schema registry with ssl with smallrye
    @Inject
    @Channel("transportations")
    Emitter<Transportation> emitter;
    */
    // may be temporary
    @Inject
    TransportProducer producer;
    
    public TransportationService() {
 
    }

    public List<TransportDefinition> getAllTransportDefinitions(){
        return repository.getAll();
    }

    public void saveNewTransportation(TransportDefinition td) {
        repository.addTransport(td);
    }

   public void sendCurrentTransportDefinitions() {
        logger.info("Start sending currentTransportDefinition");
        for (TransportDefinition item: getAllTransportDefinitions()) {
            logger.info("Send: " + item.lane_id);
            Transportation transportationEvent = new Transportation(item.lane_id,
                item.from_loc,
                item.to_loc,
                item.transit_time,
                item.reefer_cost,
                item.fixed_cost);
            producer.sendOneTransportationEvent(transportationEvent);
            // KafkaRecord<String, Transportation> record = KafkaRecord.of(item.lane_id,transportationEvent);
            //emitter.send(record);
        };
        
	}


}
