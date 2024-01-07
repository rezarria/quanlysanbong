package io.rezarria.dto.update;

import com.fasterxml.jackson.databind.JsonNode;
import io.rezarria.model.Staff;
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
public class StaffUpdateDTO {
    private UUID id;
    private String name;
    private String avatar;
    private Date dob;
    private JsonNode data;

    public static StaffUpdateDTO create(Staff staff) {
        return StaffUpdateDTO.builder()
                .id(staff.getId())
                .name(staff.getName())
                .avatar(staff.getAvatar())
                .dob(staff.getDob())
                .data(staff.getData())
                .build();
    }
}
