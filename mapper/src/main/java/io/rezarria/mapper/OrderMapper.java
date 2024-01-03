package io.rezarria.mapper;

import io.rezarria.dto.post.OrderPostDTO;
import io.rezarria.model.Customer;
import io.rezarria.model.Field;
import io.rezarria.model.FieldHistory;
import io.rezarria.model.FieldUnitSetting;
import io.rezarria.repository.CustomerRepository;
import io.rezarria.repository.FieldRepository;
import io.rezarria.repository.FieldUnitSettingRepository;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.util.UUID;

@Mapper(componentModel = "spring")
public abstract class OrderMapper {

    @Autowired
    @Lazy
    public CustomerRepository customerRepository;

    @Autowired
    @Lazy
    public FieldRepository fieldRepository;

    @Autowired
    @Lazy
    public FieldUnitSettingRepository fieldUnitSettingRepository;

    @Mapping(target = "customer", source = "customerId", qualifiedByName = "mapCustomer")
    @Mapping(target = "field", source = "fieldId")
    @Mapping(target = "unitSize", source = "unit")
    @Mapping(target = "unitSetting", source = "fieldUnitSettingId")
    @BeanMapping(ignoreByDefault = true)
    public abstract FieldHistory convert(OrderPostDTO dto);

    @Named("mapCustomer")
    public Customer convertCustomer(UUID id) {
        return customerRepository.findById(id).orElse(null);
    }

    public Field convertField(UUID id) {
        return fieldRepository.findById(id).orElse(null);
    }

    public FieldUnitSetting convertFieldUnitSetting(UUID id) {
        return fieldUnitSettingRepository.findById(id).orElse(null);
    }
}
