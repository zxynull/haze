package com.haze.demos.web.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Accessors(chain = true)
@Table(name = "products")
public class Product extends BaseEntityAbstract {
    public String name;
    public String code;
    public String brand;
    public String model;
    public String nodeType;
}