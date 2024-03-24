package com.haze.demos.web.service.impl;

import com.haze.demos.web.dto.BaseDTOAbstract;
import com.haze.demos.web.dto.DeviceDTO;
import com.haze.demos.web.dto.ProductDTO;
import com.haze.demos.web.entity.BaseEntityAbstract;
import com.haze.demos.web.entity.Device;
import com.haze.demos.web.entity.Product;
import com.haze.demos.web.service.BaseService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.Tuple;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
import javax.persistence.metamodel.SingularAttribute;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;

public abstract class BaseServiceImplAbstract <T extends BaseEntityAbstract, D extends BaseDTOAbstract, I extends Serializable,M extends JpaRepository<T,I>> implements BaseService<T,I> {

    @Autowired
    protected M repository;

    /**
     * 逻辑删除
     * @param id 主键id
     * @return bool
     */
    @Override
    public boolean removeById(I id) {
        Optional<T> byId = repository.findById(id);
        if(byId.isPresent()){
            T t = byId.get();
            t.setIsDeleted(1);
            repository.save(t);
            return true;
        }
        return false;
    }


    @Override
    public T save(T entity) {
        return repository.save(entity);
    }

    protected List<String> getPopulationFields(String populate) {
        if (populate != null && !populate.isEmpty()) {
            return Arrays.asList(populate.split(","));
        }
        return null;
    }

    protected List<Selection<?>> getSelections(Root<T> root,
                                             List<String> populationFields,
                                             List<String> supportedPopulationFields) {
        List<Selection<?>> selections = new ArrayList<>();
        for (SingularAttribute<? super T, ?> attribute : root.getModel().getSingularAttributes()) {
            if (!supportedPopulationFields.contains(attribute.getName())) {
                selections.add(root.get(attribute).alias(attribute.getName()));
                continue;
            }

            if (populationFields != null && populationFields.contains(attribute.getName())) {
                selections.add(root.get(attribute).alias(attribute.getName()));
            }
        }
        return selections;
    }

    protected List<D> ConvertTuplesToDTOsWithPopulationFields(
            List<Tuple> selectedResults,
            Set<SingularAttribute<? super T, ?>> entityAttributes,
            Class<T> entityClazz,
            Class<D> DTOClazz,
            List<String> populationFields,
            List<String> supportedPopulationFields
    ) throws InstantiationException, IllegalAccessException {

        List<D> dtos = new ArrayList<>();

        for (Tuple result : selectedResults) {
            D dto = DTOClazz.newInstance();
            T entity = entityClazz.newInstance();

            // 遍历 Entity 的每个属性
            for (SingularAttribute<? super T, ?> attribute : entityAttributes) {

                if (supportedPopulationFields.contains(attribute.getName())) {
                    continue;
                }

                Object attributeValue = result.get(attribute.getName());
                if (attributeValue == null) {
                    continue;
                }

                try {
                    Field field = entityClazz.getField(attribute.getName());
                    field.setAccessible(true);
                    field.set(entity, attributeValue);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

            BeanUtils.copyProperties(entity, dto);

            copyPopulationEntityToDTO(result, entity, dto, populationFields, supportedPopulationFields);

            dtos.add(dto);
        }

        return dtos;
    }

    protected abstract void copyPopulationEntityToDTO(
            Tuple selectedResult,
            T entity,
            D dto,
            List<String> populationFields,
            List<String> supportedPopulationFields);
}
