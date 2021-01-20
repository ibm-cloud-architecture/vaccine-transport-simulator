package ibm.gse.eda.vaccine.transport.domain;

// {"lane_id":"1","from_loc":"Beerse, Belgium","to_loc":"Paris, France","transit_time":2,"reefer_cost":10,"fixed_cost":20}

public class TransportDefinition {
    public String lane_id;
    public String from_loc;
    public String to_loc;
    public double transit_time;
    public double fixed_cost;
    public double reefer_cost;

    public TransportDefinition(){}
}