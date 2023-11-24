package io.rezarria.sanbong.dto;

import java.util.Date;
import java.util.UUID;

import lombok.Data;

@Data
public class UserPostDTO {
    private String name;
    private String avatar;
    private UUID account;
    private Date dob;
}
