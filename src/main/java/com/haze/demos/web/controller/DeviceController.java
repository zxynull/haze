package com.haze.demos.web.controller;

import com.haze.demos.web.dto.DeviceDTO;
import com.haze.demos.web.service.DeviceService;
import com.haze.demos.web.util.LoggerUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class DeviceController {

    @Autowired
    private DeviceService deviceService;

    @GetMapping("/v1/devices")
    public Page<DeviceDTO> getDevicesWithPopulate(
            Pageable pageable,
            @RequestParam(required = false) String populate) {
        Long beginTime = System.currentTimeMillis();
        log.info(LoggerUtil.printMethodStartWithParameters("populate",populate));
        Page<DeviceDTO> devicesWithPage = deviceService.getDevicesWithPopulate(populate, pageable);
        Long endTime = System.currentTimeMillis();
        log.info(LoggerUtil.printExecutionTime(endTime, beginTime));
        return devicesWithPage;
    }

    @GetMapping("/v2/devices")
    public Page<DeviceDTO> getDevices(
            Pageable pageable,
            @RequestParam(required = false) String populate) {
        Long beginTime = System.currentTimeMillis();
        log.info(LoggerUtil.printMethodStartWithParameters("populate",populate));
        Page<DeviceDTO> devicesWithPage = deviceService.getDevices(populate, pageable);
        Long endTime = System.currentTimeMillis();
        log.info(LoggerUtil.printExecutionTime(endTime, beginTime));
        return devicesWithPage;
    }
}
