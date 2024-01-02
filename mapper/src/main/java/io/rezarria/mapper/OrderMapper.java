package io.rezarria.mapper;

import io.rezarria.dto.post.OrderPostDTO;
import io.rezarria.model.Customer;
import io.rezarria.model.FieldHistory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping(target = "customer", source = "customerId", qualifiedByName = "mapCustomer")
    FieldHistory convert(OrderPostDTO dto);

    @Named("mapCustomer")
    default Customer convert(UUID id) {
        return Customer.builder().id(id).build();
    }
}
