package com.haze.demos.web.service;

import com.haze.demos.web.dto.DeviceDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DeviceService {
    Page<DeviceDTO> getDevicesWithPopulate(String populate, Pageable pageable);

    Page<DeviceDTO> getDevices(String populate, Pageable pageable);
}
