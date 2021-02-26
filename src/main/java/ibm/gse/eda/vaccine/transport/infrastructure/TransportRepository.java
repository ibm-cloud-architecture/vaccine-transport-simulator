package ibm.gse.eda.vaccine.transport.infrastructure;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Singleton;

import com.fasterxml.jackson.databind.ObjectMapper;

import ibm.gse.eda.vaccine.transport.domain.TransportDefinition;

@Singleton
public class TransportRepository {
    private static HashMap<String,TransportDefinition> repo = new HashMap<String,TransportDefinition>();

    private static ObjectMapper mapper = new ObjectMapper();
    

    public TransportRepository() {
        super();
        InputStream is = getClass().getClassLoader().getResourceAsStream("transportations.json");
        if (is == null) 
            throw new IllegalAccessError("file not found for transportation");
        try {
            List<TransportDefinition> currentTransportationDefinitions = mapper.readValue(is, mapper.getTypeFactory().constructCollectionType(List.class, TransportDefinition.class));
            currentTransportationDefinitions.stream().forEach( (t) -> repo.put(t.lane_id,t));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<TransportDefinition> getAll(){
        return new ArrayList<TransportDefinition>(repo.values());
    }

    public void addTransport(TransportDefinition p) {
        repo.put(p.lane_id, p);
    }
}
