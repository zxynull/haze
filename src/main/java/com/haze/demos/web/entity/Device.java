package com.haze.demos.web.entity;


import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Accessors(chain = true)
@Table(name = "devices")
public class Device extends BaseEntityAbstract implements Serializable {

    public String name;
    public String code;

    @Column(name="product_id")
    public String productId;

    public String deviceTypeId;
    public String spaceId;
    public String labels;
    public String description;
    public String protocolConfigId;
    public String externalCode;

    @ManyToOne
    @JoinColumn(name="product_id", referencedColumnName="id", insertable = false, updatable = false)
    public Product product;

}
