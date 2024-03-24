package com.haze.demos.web.repository;

import com.haze.demos.web.entity.Device;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityGraph;
import java.util.Optional;

@Repository
public interface DeviceRepository extends BaseRepository<Device, String> {
    //@EntityGraph(attributePaths ={ "device.all" })
//    Optional<Device> findById(String id, EntityGraph entityGraph);

}
