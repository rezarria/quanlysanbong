package io.rezarria.sanbong.dto.update;

import io.rezarria.sanbong.model.Organization;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationUpdateDTO {
    private UUID id;
    private String name;
    private String address;
    private String email;
    private String phone;
    private String image;

    public static OrganizationUpdateDTO create(Organization src) {
        return OrganizationUpdateDTO.builder()
                .id(src.getId())
                .name(src.getName())
                .address(src.getAddress())
                .email(src.getEmail())
                .phone(src.getPhone())
                .image(src.getImage())
                .build();
    }
}
