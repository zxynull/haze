package com.haze.demos.web.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@Data
@Accessors(chain = true)
@MappedSuperclass
public class BaseEntityAbstract {
    @Id
    @Column
    public String id;

    public Integer isDeleted = 0;

    public Long createdAt;

    public Long updatedAt;
}
