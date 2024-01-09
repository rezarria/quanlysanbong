package io.rezarria.mapper;

import io.rezarria.dto.update.BillUpdateDTO;
import io.rezarria.dto.update.BillUpdateDTO.BillDetailUpdateDTO;
import io.rezarria.model.Bill;
import io.rezarria.model.BillDetail;
import io.rezarria.model.Customer;
import io.rezarria.repository.CustomerRepository;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring")
public abstract class BillUpdateDTOMapper {

    @Autowired
    private CustomerRepository customerRepository;

    @Mappings(value = {
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "customer", source = "customerId", qualifiedByName = "mapCustomer"),
            @Mapping(target = "totalPrice", ignore = true),
    })
    public abstract void convert(BillUpdateDTO dto, @MappingTarget Bill bill);

    public void convert(List<BillDetailUpdateDTO> dto, @MappingTarget List<BillDetail> details) {

    }

    @Named("mapCustomer")
    public void convert(UUID id, @MappingTarget Customer customer) {

    }

    @AfterMapping
    public void afterMapping(BillUpdateDTO dto, @MappingTarget Bill bill) {
        var history = bill.getFieldHistory();
        history.setFrom(dto.getFrom());
        history.setTo(dto.getTo());
        history.setUnitSize(dto.getUnitSize());
        if (dto.getUnitSettingId() == null) {

        }
        var currentCustomer = history.getCustomer();
        if (currentCustomer == null || currentCustomer.getId() != dto.getCustomerId()) {
            customerRepository.getReferenceById(dto.getCustomerId());
        }
    }

    public void apply(BillUpdateDTO dto, @MappingTarget Bill bill) {
        convert(dto, bill);
    }
}
