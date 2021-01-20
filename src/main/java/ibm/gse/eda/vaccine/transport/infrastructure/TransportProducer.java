package ibm.gse.eda.vaccine.transport.infrastructure;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import javax.inject.Singleton;

import com.ibm.eventstreams.serdes.SchemaInfo;
import com.ibm.eventstreams.serdes.SchemaRegistry;
import com.ibm.eventstreams.serdes.SchemaRegistryConfig;
import com.ibm.eventstreams.serdes.exceptions.InvalidPropertyValueException;
import com.ibm.eventstreams.serdes.exceptions.PropertyNotFoundException;
import com.ibm.eventstreams.serdes.exceptions.SchemaNotFoundException;
import com.ibm.eventstreams.serdes.exceptions.SchemaRegistryApiException;
import com.ibm.eventstreams.serdes.exceptions.SchemaRegistryAuthException;
import com.ibm.eventstreams.serdes.exceptions.SchemaRegistryConnectionException;
import com.ibm.eventstreams.serdes.exceptions.SchemaRegistryServerErrorException;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.jboss.logging.Logger;

import ibm.gse.eda.vaccine.orderoptimizer.Transportation;

@Singleton
public class TransportProducer {
    Logger logger = Logger.getLogger(TransportProducer.class.getName());

    private SchemaRegistry schemaRegistry = null;
    private SchemaInfo schema = null;
    private KafkaProducer<String, Transportation> kafkaProducer = null;
    private KafkaConfiguration configuration = null;

    public TransportProducer() {
        super();
        configuration = new KafkaConfiguration();
    }

    public void sendTransportationEvents(List<Transportation> l) {
        for (Transportation t : l) {
            sendOneTransportationEvent(t);
        }
    }

    public void sendOneTransportationEvent(Transportation transportation) {

        // Prepare the record, adding the Schema Registry headers
        ProducerRecord<String, Transportation> producerRecord = new ProducerRecord<String, Transportation>(
                configuration.getTopicName(), transportation.getLaneId(), transportation);
        producerRecord.headers().add(SchemaRegistryConfig.HEADER_MSG_SCHEMA_ID, schema.getIdAsBytes());
        producerRecord.headers().add(SchemaRegistryConfig.HEADER_MSG_SCHEMA_VERSION, schema.getVersionAsBytes());

        logger.info("sending to " + configuration.getTopicName() + " item " + transportation.toString());
        try {
            getProducer().send(producerRecord, new Callback() {

                @Override
                public void onCompletion(RecordMetadata metadata, Exception exception) {
                    if (exception != null) {
                        exception.printStackTrace();
                    } else {
                        logger.info("The offset of the record just sent is: " + metadata.offset());
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        // logger.info("Partition:" + resp.partition());
    }

    public KafkaProducer<String, Transportation> getProducer() {
        if (kafkaProducer == null) {
            Properties props = configuration.getProducerProperties("TransportationProducer_" + UUID.randomUUID());
            // Get a new connection to the Schema Registry
            try {
                schemaRegistry = new SchemaRegistry(props);
                // Get the schema from the registry
                schema = schemaRegistry.getSchema("Transportation", "1.0.0");
            } catch (KeyManagementException | NoSuchAlgorithmException | SchemaNotFoundException
                    | PropertyNotFoundException | InvalidPropertyValueException | SchemaRegistryAuthException
                    | SchemaRegistryServerErrorException | SchemaRegistryApiException
                    | SchemaRegistryConnectionException e) {
               
                e.printStackTrace();
            }

            
            kafkaProducer = new KafkaProducer<String, Transportation>(configuration.getProducerProperties("TransportationProducer_" + UUID.randomUUID()));
        }
        return kafkaProducer;
    }

    public void close(){
        kafkaProducer.close();
        kafkaProducer = null;
    }
}
