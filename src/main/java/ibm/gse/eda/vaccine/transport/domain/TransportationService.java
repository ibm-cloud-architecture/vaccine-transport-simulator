package ibm.gse.eda.vaccine.transport.domain;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.jboss.logging.Logger;

import ibm.gse.eda.vaccine.transport.infrastructure.TransportRepository;
import io.smallrye.reactive.messaging.kafka.KafkaRecord;

/**
 * As a demo the 
 */
@ApplicationScoped
public class TransportationService {
    Logger logger = Logger.getLogger(TransportationService.class.getName());

    @Inject
    TransportRepository repository;
    
    @Inject
    @Channel("transportations")
    Emitter<TransportDefinition> emitter;
    
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
            KafkaRecord<String, TransportDefinition> record = KafkaRecord.of(item.lane_id,item);
            emitter.send(record);
        };
        
	}


}
