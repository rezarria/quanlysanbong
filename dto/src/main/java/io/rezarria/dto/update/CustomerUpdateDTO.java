package io.rezarria.dto.update;

import com.fasterxml.jackson.databind.JsonNode;
import io.rezarria.model.Customer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerUpdateDTO {
    private UUID id;
    private String name;
    private String avatar;
    private Date dob;
    private JsonNode data;

    public static CustomerUpdateDTO create(Customer customer) {
        return CustomerUpdateDTO.builder()
                .id(customer.getId())
                .name(customer.getName())
                .avatar(customer.getAvatar())
                .dob(customer.getDob())
                .data(customer.getData())
                .build();
    }
}
