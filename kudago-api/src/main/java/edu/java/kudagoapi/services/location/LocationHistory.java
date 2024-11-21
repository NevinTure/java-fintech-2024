package edu.java.kudagoapi.services.location;

import edu.java.kudagoapi.model.Location;

public interface LocationHistory {

    void push(Long id, Location location);

    Location poll(Long originId);
}
