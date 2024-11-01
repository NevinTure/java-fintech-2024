package edu.java.kudagoapi.services.location;

import edu.java.kudagoapi.exceptions.SnapshotNotFoundApiException;
import edu.java.kudagoapi.model.Location;
import edu.java.kudagoapi.model.LocationSnapshot;
import edu.java.kudagoapi.repositories.JpaLocationRepository;
import edu.java.kudagoapi.repositories.JpaLocationSnapshotRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JpaLocationHistory implements LocationHistory {

    private final JpaLocationSnapshotRepository snapshotRepo;
    private final JpaLocationRepository repo;
    private final ModelMapper mapper;

    @Override
    public void push(Long originId, Location location) {
        LocationSnapshot snapshot = mapper.map(location, LocationSnapshot.class);
        snapshotRepo.save(snapshot);
    }

    @Override
    public Location poll(Long originId) {
        Optional<LocationSnapshot> snapshotOp = snapshotRepo.findFirstByOriginIdOrderByChangeAtDesc(originId);
        if (snapshotOp.isPresent()) {
            LocationSnapshot snapshot = snapshotOp.get();
            Location location = mapper.map(snapshot, Location.class);
            repo.save(location);
            snapshotRepo.deleteById(snapshot.getId());
            return location;
        }
        throw new SnapshotNotFoundApiException(String
                .format("No snapshot found for location id %d", originId));
    }
}
