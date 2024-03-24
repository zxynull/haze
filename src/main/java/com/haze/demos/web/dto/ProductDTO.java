package com.haze.demos.web.dto;

import lombok.Data;

@Data
public class ProductDTO extends BaseDTOAbstract{
    private String id;
    private String name;
    private String code;
    private String brand;
    private String model;
    private String nodeType;
}
