package edu.java.kudagoapi.services.location;

import edu.java.kudagoapi.model.Location;
import edu.java.kudagoapi.utils.LocationRequestOperation;

public interface LocationEventListener {

    void update(LocationRequestOperation op, Location location);
}
