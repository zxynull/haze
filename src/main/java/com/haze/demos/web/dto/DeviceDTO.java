package com.haze.demos.web.dto;

import lombok.Data;

import java.util.Map;

@Data
public class DeviceDTO extends BaseDTOAbstract {

    private String name;
    private String code;
    private String productId;
    private String deviceTypeId;
    private String protocolConfigId;
    private String spaceId;
    private Map<String,String> attributes;
    private String description;
    private String labels;
    private String externalCode;
    private ProductDTO product;
}
