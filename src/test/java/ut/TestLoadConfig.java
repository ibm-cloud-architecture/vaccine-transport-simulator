package ut;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import ibm.gse.eda.vaccine.transport.infrastructure.TransportProducer;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class TestLoadConfig {
    
   //@Test
    public void loadConfiguration(){
        TransportProducer producer = new TransportProducer();
        assertNotNull(producer);
        assertNotNull(producer.getProducer());
    }
}
