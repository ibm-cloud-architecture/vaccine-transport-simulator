package ibm.gse.eda.vaccine.transport.domain;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.jboss.logging.Logger;

import io.smallrye.mutiny.Multi;
import io.smallrye.reactive.messaging.kafka.KafkaRecord;

@ApplicationScoped
public class TransportationService {
    Logger logger = Logger.getLogger(TransportationService.class.getName());

    static ObjectMapper mapper = new ObjectMapper();
    private List<TransportDefinition> currentTransportationDefinitions = new ArrayList<TransportDefinition>();

    @Inject
    @Channel("transportations")
    Emitter<TransportDefinition> emitter;
    
    public TransportationService() {
        InputStream is = getClass().getClassLoader().getResourceAsStream("transportations.json");
        try {
            currentTransportationDefinitions = mapper.readValue(is, mapper.getTypeFactory().constructCollectionType(List.class, TransportDefinition.class));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String greeting(String name) {
        return "hello " + name;
    }

    public List<TransportDefinition> getAllTransportDefinitions(){
        return currentTransportationDefinitions;
    }

   public void sendCurrentTransportDefinitions() {
        for (TransportDefinition item: getAllTransportDefinitions()) {
            KafkaRecord<String, TransportDefinition> record = KafkaRecord.of(item.lane_id,item);
            emitter.send(record);
        };
	}


}
