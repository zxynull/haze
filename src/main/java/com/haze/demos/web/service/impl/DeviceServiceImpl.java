package com.haze.demos.web.service.impl;

import com.haze.demos.web.dto.DeviceDTO;
import com.haze.demos.web.dto.ProductDTO;
import com.haze.demos.web.entity.Device;
import com.haze.demos.web.entity.Product;
import com.haze.demos.web.repository.DeviceRepository;
import com.haze.demos.web.service.DeviceService;
import com.haze.demos.web.util.LoggerUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import javax.persistence.criteria.*;
import javax.persistence.metamodel.SingularAttribute;
import java.lang.reflect.Field;
import java.util.*;

@Slf4j
@Service
public class DeviceServiceImpl extends BaseServiceImplAbstract<Device, DeviceDTO, String, DeviceRepository> implements DeviceService {
    @Autowired
    private DeviceRepository deviceRepository;
    @PersistenceContext
    private EntityManager entityManager;

    List<String> supportedPopulationFields;

    public DeviceServiceImpl() {
        supportedPopulationFields = Arrays.asList("product");
    }

    protected void copyPopulationEntityToDTO(
            Tuple selectedResult,
            Device device,
            DeviceDTO deviceDTO,
            List<String> populationFields,
            List<String> supportedPopulationFields) {
        // 如果 populate 中包含 "product"，则将 Product 对象的属性值设置到 ProductDTO 中
        if (populationFields != null
                && populationFields.contains("product")
                && supportedPopulationFields.contains("product")) {

            Product product = selectedResult.get("product", Product.class);
            if (product != null) {
                ProductDTO productDTO = new ProductDTO();
                BeanUtils.copyProperties(product, productDTO);
                deviceDTO.setProduct(productDTO);
            }
        }
    }

    public Page<DeviceDTO> getDevices(String populate, Pageable pageable) {
        log.info(LoggerUtil.printMethodStartWithParameters(
                "populate", populate, "pageable", pageable));
        Long beginTime = System.currentTimeMillis();

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> cq = cb.createQuery(Tuple.class);
        Root<Device> root = cq.from(Device.class);

        List<String> populationFields = getPopulationFields(populate);
        List<Selection<?>> selections = getSelections(root, populationFields, supportedPopulationFields);

        cq.multiselect(selections);

        List<Tuple> results = entityManager.createQuery(cq)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        List<DeviceDTO> deviceDTOs = new ArrayList<>();
        try {
            Set<SingularAttribute<? super Device, ?>> attributes = root.getModel().getSingularAttributes();
            deviceDTOs = ConvertTuplesToDTOsWithPopulationFields(
                    results, attributes, Device.class, DeviceDTO.class, populationFields, supportedPopulationFields);
        }  catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        CriteriaQuery<Long> countCq = cb.createQuery(Long.class);
        countCq.select(cb.count(countCq.from(Device.class)));
        Long totalCount = entityManager.createQuery(countCq).getSingleResult();

        Long endTime = System.currentTimeMillis();
        log.info(LoggerUtil.printExecutionTime(endTime, beginTime));

        return new PageImpl<>(deviceDTOs, pageable, totalCount);
    }

    public Page<DeviceDTO> getDevicesWithPopulate(String populate, Pageable pageable) {
        log.info(LoggerUtil.printMethodStartWithParameters("populate", populate, "pageable", pageable));
        Long beginTime = System.currentTimeMillis();

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> cq = cb.createQuery(Tuple.class);
        Root<Device> root = cq.from(Device.class);

        // 选择除了product以外的所有字段
        List<Selection<?>> selections = new ArrayList<>();
        for (SingularAttribute<? super Device, ?> attribute : root.getModel().getSingularAttributes()) {
            // 如果populate中包含product，则选择product字段
            if (populate != null && populate.contains("product") && attribute.getName().equals("product")) {
                selections.add(root.get(attribute).alias(attribute.getName()));
            }
            // 如果populate中不包含product，选择除了product以外的所有字段
            else if (!attribute.getName().equals("product")) {
                selections.add(root.get(attribute).alias(attribute.getName()));
            }
        }

        cq.multiselect(selections);


        List<Tuple> results = entityManager.createQuery(cq)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        List<DeviceDTO> deviceDTOs = new ArrayList<>();
        // 获取 Device Entity 的所有属性
        Set<SingularAttribute<? super Device, ?>> attributes = root.getModel().getSingularAttributes();

        for (Tuple result : results) {
            DeviceDTO deviceDTO = new DeviceDTO();
            Device device = new Device();

            // 遍历 Device Entity 的每个属性
            for (SingularAttribute<? super Device, ?> attribute : attributes) {
                if (supportedPopulationFields.contains(attribute.getName())) {
                    continue;
                }

                // 从 Tuple 中获取属性值，并设置到 Device 对象中
                Object attributeValue = result.get(attribute.getName());
                if (attributeValue == null) {
                    continue;
                }

                // 使用反射设置属性值
                try {
                    Field field = Device.class.getField(attribute.getName());
                    field.setAccessible(true);
                    field.set(device, attributeValue);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

            BeanUtils.copyProperties(device, deviceDTO);

            // 如果 populate 中包含 "product"，则将 Product 对象的属性值设置到 ProductDTO 中
            if (populate != null && populate.contains("product")) {
                Product product = result.get("product", Product.class);
                if (product != null) {
                    ProductDTO productDTO = new ProductDTO();
                    BeanUtils.copyProperties(product, productDTO);
                    deviceDTO.setProduct(productDTO);
                }
            }

            // 将 Device 对象添加到 DeviceDTO 列表中
            deviceDTOs.add(deviceDTO);
        }

        CriteriaQuery<Long> countCq = cb.createQuery(Long.class);
        countCq.select(cb.count(countCq.from(Device.class)));
        Long totalCount = entityManager.createQuery(countCq).getSingleResult();

        Long endTime = System.currentTimeMillis();
        log.info(LoggerUtil.printExecutionTime(endTime, beginTime));

        return new PageImpl<>(deviceDTOs, pageable, totalCount);
    }
}
