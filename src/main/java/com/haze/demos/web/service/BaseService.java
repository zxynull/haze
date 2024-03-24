package com.haze.demos.web.service;

public interface BaseService<T,I> {


    /**
     * @param id 主键id
     * @return boolean
     */
    boolean removeById(I id);


    T save(T entity);
}
