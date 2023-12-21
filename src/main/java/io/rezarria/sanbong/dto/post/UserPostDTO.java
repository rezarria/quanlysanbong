package io.rezarria.sanbong.dto.post;

import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class UserPostDTO {
    private String name;
    private String avatar;
    private UUID account;
    private Date dob;
}
