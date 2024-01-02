package io.rezarria.dto.update;

import java.util.Date;
import java.util.UUID;

import com.fasterxml.jackson.databind.JsonNode;

import io.rezarria.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateDTO {
    private UUID id;
    private String name;
    private String avatar;
    private Date dob;
    private JsonNode data;

    public static UserUpdateDTO create(User user) {
        return UserUpdateDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .avatar(user.getAvatar())
                .dob(user.getDob())
                .data(user.getData())
                .build();
    }
}
