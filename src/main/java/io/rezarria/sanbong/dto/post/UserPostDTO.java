package io.rezarria.sanbong.dto.post;

import java.util.Date;
import java.util.UUID;

import jakarta.annotation.Nullable;
import lombok.Data;

@Data
public class UserPostDTO {
    protected String name;
    protected String avatar;
    @Nullable
    protected UUID account;
    protected Date dob;
}
